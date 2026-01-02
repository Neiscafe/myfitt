package br.com.myfitt.treinos.ui.screens.seriesExercicio

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.myfitt.common.domain.Exercicio
import br.com.myfitt.common.domain.ExercicioTreino
import br.com.myfitt.common.domain.SerieExercicio
import br.com.myfitt.common.domain.Treino
import br.com.myfitt.common.domain.onErro
import br.com.myfitt.common.domain.onSucesso
import br.com.myfitt.treinos.domain.facade.TreinoFacade
import br.com.myfitt.treinos.domain.repository.SeriesRepository
import br.com.myfitt.treinos.domain.repository.TreinoRepository
import br.com.myfitt.treinos.ui.CronometroFacade
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class SeriesExercicioViewModel(
    val exercicioTreinoId: Int,
    val treinoId: Int,
    private val seriesRepository: SeriesRepository,
    private val cronometroFacade: CronometroFacade,
    private val treinoFacade: TreinoFacade,
    private val treinoRepository: TreinoRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(SeriesExercicioState())
    val state = _state.asStateFlow()

    val cronometroState = cronometroFacade.ticksCronometro
    private var pesoKg: Float = 0f
    private var repeticoes = 0
    private var serieMaisRecente: SerieExercicio? = null
        @Synchronized get
        @Synchronized set
    private var _exercicioTreino: ExercicioTreino? = null
    val exercicioTreino get() = _exercicioTreino!!
    private var _treino: Treino? = null
    val treino get() = _treino!!
    private var _exercicio: Exercicio? = null
    val exercicio get() = _exercicio!!
    private var primeiroExercicio = false

    init {
        viewModelScope.launch(Dispatchers.IO) {
            _state.update { it.copy(carregando = true) }
            treinoFacade.buscar(treinoId).onErro {
                _state.update { state -> state.copy(erro = it) }
                return@launch
            }.onSucesso {
                _exercicioTreino =
                    it.exerciciosTreino.firstOrNull { it.exercicioTreinoId == exercicioTreinoId }
                _exercicio =
                    it.exercicios.firstOrNull { it.exercicioId == exercicioTreino.exercicioId }
                _treino = it.treino
                val seriesExercicio = it.series.filter { it.exercicioTreinoId == exercicioTreinoId }
                serieMaisRecente =
                    it.series.lastOrNull()
                _state.update { state ->
                    state.copy(
                        series = seriesExercicio,
                        nomeExercicio = exercicio.nome,
                        observacaoExercicio = exercicio.observacao ?: "",
                        carregando = false,
                    )
                }
                val result =
                    seriesRepository.seriesDestaqueExercicio(exercicio.exercicioId, treino.treinoId)
                _state.update { it.copy(serieDestaques = result.dataOrNull) }
            }
            if (serieMaisRecente?.serieEmAndamento == true) {
                cronometroFacade.iniciaExercicio(serieMaisRecente?.dhInicioExecucao!!)
            } else if (serieMaisRecente?.descansando == true) {
                cronometroFacade.iniciaDescanso(serieMaisRecente?.dhInicioDescanso!!)
            } else if (serieMaisRecente != null) {
                cronometroFacade.iniciaDescanso(serieMaisRecente?.dhFimExecucao!!)
            }
        }
    }

    fun atualiza(exercicio: Exercicio) {
        _state.update { it.copy(observacaoExercicio = exercicio.observacao ?: "") }
    }

    fun fimExecucao() {
        viewModelScope.launch(Dispatchers.IO) {
            val fimUltimaSerie = LocalDateTime.now()
            serieMaisRecente = serieMaisRecente?.finaliza(fimUltimaSerie)
            _state.update { it.copy(carregando = true) }
            val result = seriesRepository.altera(serieMaisRecente!!)
            _state.update {
                it.copy(
                    erro = result.erroOrNull,
                    series = result.dataOrNull ?: it.series,
                    carregando = false
                )
            }
            if (result.sucesso) {
                cronometroFacade.iniciaDescanso(fimUltimaSerie)
            }
        }
    }

    fun informaRepeticoes() {
        viewModelScope.launch(Dispatchers.IO) {
            _state.update { it.copy(carregando = true) }
            serieMaisRecente = serieMaisRecente?.copy(repeticoes = repeticoes)!!
            val result = seriesRepository.altera(serieMaisRecente!!)
            _state.update {
                it.copy(
                    carregando = false,
                    series = result.dataOrNull ?: it.series,
                    erro = result.erroOrNull
                )
            }
        }
    }

    fun inicioExecucao() {
        viewModelScope.launch(Dispatchers.IO) {
            _state.update { it.copy(carregando = true) }
            if (serieMaisRecente == null) {
                val buscaTreinoResult = treinoRepository.busca(exercicioTreino.treinoId)
                buscaTreinoResult.erroOrNull?.let {
                    _state.update { it2 -> it2.copy(erro = it) }
                    return@launch
                }
                val atualizaTreinoResult =
                    treinoRepository.altera(buscaTreinoResult.dataOrNull!!.copy(dhInicio = LocalDateTime.now()))
                atualizaTreinoResult.erroOrNull?.let {
                    _state.update { it2 -> it2.copy(erro = it) }
                    return@launch
                }
            }
            val result = seriesRepository.cria(
                SerieExercicio(
                    serieId = 0,
                    exercicioTreinoId = exercicioTreino.exercicioTreinoId,
                    exercicioId = exercicio.exercicioId,
                    treinoId = treino.treinoId
                ).iniciaExecucao(pesoKg, serieMaisRecente?.dhFimExecucao)
            )
            _state.update {
                it.copy(
                    carregando = false,
                    series = result.dataOrNull ?: it.series,
                    erro = result.erroOrNull
                )
            }
            serieMaisRecente = result.dataOrNull?.lastOrNull() ?: return@launch
            cronometroFacade.iniciaExercicio(serieMaisRecente?.dhInicioExecucao!!)
        }
    }


    fun repeticoesMudou(repeticoes: String) {
        this.repeticoes = repeticoes.toIntOrNull() ?: 0
    }

    fun pesoMudou(texto: String) {
        this.pesoKg = texto.toFloatOrNull() ?: 0f
    }

    fun resetaEventos() {
        _state.update { it.resetaEventos() }
    }

    fun atualizaEstado(seriesDoExercicio: List<SerieExercicio>) {
        _state.update {
            it.copy(
                series = seriesDoExercicio,
            )
        }
    }
}