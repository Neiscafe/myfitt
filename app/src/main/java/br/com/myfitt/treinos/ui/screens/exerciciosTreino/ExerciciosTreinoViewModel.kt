package br.com.myfitt.treinos.ui.screens.exerciciosTreino

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.myfitt.common.domain.ExercicioTreino
import br.com.myfitt.common.domain.Treino
import br.com.myfitt.treinos.domain.repository.ExercicioTreinoRepository
import br.com.myfitt.treinos.domain.repository.SeriesRepository
import br.com.myfitt.treinos.domain.repository.TreinoRepository
import br.com.myfitt.treinos.ui.CronometroFacade
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.LocalDateTime

class ExerciciosTreinoViewModel(
    val treinoId: Int,
    val exercicioTreinoRepository: ExercicioTreinoRepository,
    val cronometroFacade: CronometroFacade,
    val seriesRepository: SeriesRepository,
    val treinoRepository: TreinoRepository
) : ViewModel() {
    private val _state = MutableStateFlow(ExerciciosTreinoState())
    val state = _state.asStateFlow()
    private var _treino: Treino? = null
    val treino get() = _treino!!
    val cronometroState = cronometroFacade.ticksCronometro

    init {
        viewModelScope.launch(Dispatchers.IO) {
            while (true) {
                val result = seriesRepository.todasDoTreino(treinoId)
                val inicioTreino =
                    result.dataOrNull?.firstOrNull { it.dhInicioDescanso == null }?.dhInicioExecucao
                val minutosDuracao = inicioTreino?.let {
                    Duration.between(it, LocalDateTime.now()).toMinutes()
                }
                val mensagemDuracao = minutosDuracao?.let { "Duração: ${it / 60}h${it % 60}m" }
                    ?: "Duração: Não iniciado"
                _state.update {
                    it.copy(
                        erro = it.erro ?: result.erroOrNull,
                        mensagemDuracao = mensagemDuracao
                    )
                }
                delay(1500L)
            }
        }
        viewModelScope.launch(Dispatchers.IO) {
            _state.update { it.copy(carregando = true) }
            val treinoResult = async {
                treinoRepository.busca(treinoId)
            }
            val exerciciosResult = async {
                exercicioTreinoRepository.lista(treinoId)
            }
            awaitAll(treinoResult, exerciciosResult)
            _treino = treinoResult.await().dataOrNull
            _state.update {
                it.copy(
                    erro = it.erro ?: treinoResult.await().erroOrNull
                    ?: exerciciosResult.await().erroOrNull,
                    exercicios = exerciciosResult.await().dataOrNull ?: it.exercicios,
                    carregando = false
                )
            }
        }
    }

    fun finalizarTreino() {
        viewModelScope.launch(Dispatchers.IO) {
            _state.update { it.copy(carregando = true) }
            treinoRepository.altera(treino.copy(finalizado = true))
            _state.update { it.copy(carregando = false, mostrarTreinoFinalizado = true) }
        }
    }

    fun interagir(interacao: Interacao) {
        when (val it = interacao) {
            is Interacao.Remover -> viewModelScope.launch(Dispatchers.IO) {
                _state.update { it.copy(carregando = true) }
                val result = exercicioTreinoRepository.remove(it.exercicioTreino)
                _state.update {
                    it.copy(
                        carregando = false,
                        erro = result.erroOrNull,
                        exercicios = result.dataOrNull ?: it.exercicios
                    )
                }
            }

            is Interacao.Reposicionar -> viewModelScope.launch(Dispatchers.IO) {
                _state.update { it.copy(carregando = true) }
                val result = exercicioTreinoRepository.reordena(it.reposicionar, it.posicao)
                _state.update {
                    it.copy(
                        carregando = false,
                        erro = result.erroOrNull,
                        exercicios = result.dataOrNull ?: it.exercicios
                    )
                }
            }

            is Interacao.Substituir -> viewModelScope.launch(Dispatchers.IO) {
                _state.update { it.copy(carregando = true) }
                val result = exercicioTreinoRepository.substitui(
                    ExercicioTreino(
                        exercicioTreinoId = 0,
                        exercicioId = it.novo.exercicioId,
                        treinoId = treinoId,
                        compostoId = null,
                        nomeExercicio = it.novo.nome,
                        ordem = it.antigo.ordem
                    )
                )
                _state.update {
                    it.copy(
                        carregando = false,
                        erro = result.erroOrNull,
                        exercicios = result.dataOrNull ?: it.exercicios
                    )
                }
            }

            is Interacao.Adicionar -> viewModelScope.launch(Dispatchers.IO) {
                _state.update { it.copy(carregando = true) }
                val result = exercicioTreinoRepository.adiciona(
                    ExercicioTreino(
                        exercicioTreinoId = 0,
                        exercicioId = it.novo.exercicioId,
                        treinoId = treinoId,
                        ordem = state.value.exercicios.size,
                        compostoId = null,
                        nomeExercicio = it.novo.nome
                    )
                )
                _state.update {
                    it.copy(
                        carregando = false,
                        erro = result.erroOrNull,
                        exercicios = result.dataOrNull ?: it.exercicios
                    )
                }
            }
        }
    }

    fun limpaEvents() {
        _state.update { it.copy(erro = null) }
    }
}
