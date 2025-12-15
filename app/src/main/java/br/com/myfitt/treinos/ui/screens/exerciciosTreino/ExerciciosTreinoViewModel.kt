package br.com.myfitt.treinos.ui.screens.exerciciosTreino

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.myfitt.common.domain.ExercicioTreino
import br.com.myfitt.treinos.domain.facade.TreinoFacade
import br.com.myfitt.treinos.domain.repository.ExercicioTreinoRepository
import br.com.myfitt.treinos.domain.repository.TreinoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.LocalDateTime
import kotlin.math.abs

class ExerciciosTreinoViewModel(
    val treinoId: Int,
    val exercicioTreinoRepository: ExercicioTreinoRepository,
    val treinoRepository: TreinoRepository,
    val treinoFacade: TreinoFacade,
) : ViewModel() {
    private val _state = MutableStateFlow(ExerciciosTreinoState())
    val state = _state.asStateFlow()
    private var inicializacao = true

    init {
        viewModelScope.launch(Dispatchers.IO) {
            _state.update { it.copy(carregando = true) }
            val result = treinoFacade.buscar(treinoId)
            _state.update {
                it.copy(
                    erro = result.erroOrNull,
                    carregando = false,
                    exercicios = result.dataOrNull?.exerciciosTreino ?: it.exercicios,
                    mensagemDuracao = result.dataOrNull?.series?.firstOrNull()?.let {
                        abs(
                            Duration.between(
                                it.dhInicioExecucao, LocalDateTime.now()
                            ).toMinutes()
                        ).let {
                            "${it / 60}h ${it % 60}m"
                        }
                    } ?: "Não iniciado",
                    exercicioEmAndamento = result.dataOrNull?.exerciciosTreino?.firstOrNull { result.dataOrNull?.series?.lastOrNull()?.exercicioTreinoId == it.exercicioTreinoId },
                    proximoExercicio = result.dataOrNull?.exerciciosTreino?.firstOrNull {
                        result.dataOrNull?.series?.filter { serie -> it.exercicioTreinoId == serie.exercicioTreinoId }
                            .isNullOrEmpty()
                    },
                )
            }
            while (true) {
                delay(60000L)
                atualizaTreino()
            }
        }
    }

    private suspend fun atualizaTreino() {
        val result = treinoFacade.buscar(treinoId)
        _state.update {
            it.copy(
                erro = result.erroOrNull,
                mensagemDuracao = result.dataOrNull?.series?.firstOrNull()?.let {
                    abs(
                        Duration.between(
                            it.dhInicioExecucao, LocalDateTime.now()
                        ).toMinutes()
                    ).let {
                        "${it / 60}h ${it % 60}m"
                    }
                } ?: "Não iniciado",
                exercicioEmAndamento = result.dataOrNull?.exerciciosTreino?.firstOrNull { result.dataOrNull?.series?.lastOrNull()?.exercicioTreinoId == it.exercicioTreinoId },
            )
        }
    }

    fun forcaAtualizaTreino() {
        if (inicializacao) {
            inicializacao = false
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            atualizaTreino()
        }
    }

    fun finalizarTreino() {
        viewModelScope.launch(Dispatchers.IO) {
            _state.update { it.copy(carregando = true) }
            val result1 = treinoRepository.busca(treinoId)
            if (!result1.sucesso) {
                _state.update { it.copy(carregando = false, erro = result1.erroOrNull) }
                return@launch
            }
            val result2 =
                treinoRepository.altera(result1.dataOrNull!!.copy(dhFim = LocalDateTime.now()))
            _state.update {
                it.copy(
                    carregando = false,
                    mostrarTreinoFinalizado = result2.sucesso,
                    erro = result2.erroOrNull
                )
            }
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
