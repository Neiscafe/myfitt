package br.com.myfitt.treinos.ui.screens.seriesExercicio

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.myfitt.common.domain.Exercicio
import br.com.myfitt.common.domain.ExercicioTreino
import br.com.myfitt.common.domain.builder.SerieExercicioFacade
import br.com.myfitt.common.domain.onSucesso
import br.com.myfitt.treinos.domain.repository.ExercicioRepository
import br.com.myfitt.treinos.domain.repository.ExercicioTreinoRepository
import br.com.myfitt.treinos.domain.repository.SeriesRepository
import br.com.myfitt.treinos.ui.CronometroFacade
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class SeriesExercicioViewModel(
    val exercicioTreinoId: Int,
    private val seriesRepository: SeriesRepository,
    private val cronometroFacade: CronometroFacade,
    private val exercicioTreinoRepository: ExercicioTreinoRepository,
    private val exercicioRepository: ExercicioRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(SeriesExercicioState())
    val state = _state.asStateFlow()


    //    private val _cronometroState = MutableStateFlow(CronometroState())
//    val cronometroState = _cronometroState.asStateFlow()
    val cronometroState = cronometroFacade.ticksCronometro
    private var pesoKg: Float = 0f
    private var repeticoes = 0
    private lateinit var serieExercicioFacade: SerieExercicioFacade
    private var _exercicioTreino: ExercicioTreino? = null
    val exercicioTreino get() = _exercicioTreino!!
    private val dadosIniciaisDeferred: Deferred<Boolean>

    init {
        dadosIniciaisDeferred = viewModelScope.async(Dispatchers.IO) {
            _state.update { it.copy(carregando = true) }

            val exercicioTreinoResult = exercicioTreinoRepository.busca(exercicioTreinoId)
            state.value.copy(
                erro = exercicioTreinoResult.erroOrNull, carregando = exercicioTreinoResult.sucesso
            )
            if (!exercicioTreinoResult.sucesso) {
                return@async false
            }
            _exercicioTreino = exercicioTreinoResult.dataOrNull

            val exercicioDeferred = async {
                nomeObservacaoExercicio()
            }
            val seriesDeferred = async {
                val seriesResult = seriesRepository.todasDoTreino(exercicioTreino.treinoId)
                val seriesDoExercicio =
                    seriesResult.dataOrNull?.filter { it.exercicioTreinoId == exercicioTreinoId }
                _state.update {
                    it.copy(
                        series = seriesDoExercicio ?: state.value.series,
                        erro = seriesResult.erroOrNull,
                        carregando = false
                    )
                }
                if (!seriesResult.sucesso) {
                    return@async false
                }
                val seriesDoTreino = seriesResult.dataOrNull!!
                val outroExercicioEmAndamento =
                    seriesDoTreino.firstOrNull { it.exercicioTreinoId != exercicioTreinoId && it.serieEmAndamento }
                val emAndamentoExercicio = seriesDoExercicio!!.firstOrNull { it.serieEmAndamento }
                if (outroExercicioEmAndamento != null) {
                    _state.update { it.copy(erro = "Uma série já está em andamento: finalize ela para poder continuar.") }
                } else if (emAndamentoExercicio != null) {
                    serieExercicioFacade = SerieExercicioFacade.iniciar(emAndamentoExercicio)
                } else {
                    serieExercicioFacade = SerieExercicioFacade.iniciar(
                        exercicioId = exercicioTreino.exercicioId,
                        exercicioTreinoId = exercicioTreinoId,
                        treinoId = exercicioTreino.treinoId,
                    )
                }
                return@async true
            }
            listOf(exercicioDeferred, seriesDeferred).awaitAll().all { it }
        }
    }

    fun atualiza(exercicio: Exercicio){
        _state.update { it.copy(observacaoExercicio = exercicio.observacao?:"") }
    }

    private suspend fun nomeObservacaoExercicio(): Boolean {
        val exercicioResult = exercicioRepository.busca(exercicioTreino.exercicioId)
        _state.update {
            it.copy(
                erro = exercicioResult.erroOrNull, carregando = exercicioResult.sucesso
            )
        }
        if (!exercicioResult.sucesso) {
            return false
        }
        val exercicio = exercicioResult.dataOrNull
        _state.update {
            it.copy(
                observacaoExercicio = exercicio?.observacao ?: "",
                carregando = false,
                nomeExercicio = exercicio?.nome ?: ""
            )
        }
        return true
    }

    fun fimExecucao() {
        viewModelScope.launch(Dispatchers.IO) {
            if (!dadosIniciaisDeferred.await()) {
                return@launch
            }
            val fimUltimaSerie = LocalDateTime.now()
            val serie = serieExercicioFacade.fimExecucao(fimUltimaSerie).get()
            _state.update { it.copy(carregando = true) }
            val result = seriesRepository.altera(serie)
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
            val serieExercicio = serieExercicioFacade.repeticoes(repeticoes).get()
            val result = seriesRepository.altera(serieExercicio)
            serieExercicioFacade = SerieExercicioFacade.iniciar(
                exercicioTreino.exercicioId,
                exercicioTreinoId,
                exercicioTreino.treinoId
            ).descanso(serieExercicio.dhFimExecucao!!)
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
            if (!dadosIniciaisDeferred.await()) {
                return@launch
            }
            serieExercicioFacade.execucao(pesoKg).get().let {
                _state.update { it.copy(carregando = true) }
                val result = seriesRepository.cria(it)
                _state.update {
                    it.copy(
                        carregando = false,
                        series = result.dataOrNull ?: it.series,
                        erro = result.erroOrNull
                    )
                }
                result.onSucesso { seriesExercicio ->
                    val adicionada = seriesExercicio.last()
                    serieExercicioFacade.serieId(adicionada.serieId)
                    cronometroFacade.iniciaExercicio(it.dhInicioExecucao!!)
                }
            }
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
}