package br.com.myfitt.treinos.ui.screens.seriesExercicio

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.myfitt.common.domain.ExercicioTreino
import br.com.myfitt.common.domain.SerieExercicio
import br.com.myfitt.treinos.domain.repository.ExercicioRepository
import br.com.myfitt.treinos.domain.repository.ExercicioTreinoRepository
import br.com.myfitt.treinos.domain.repository.SeriesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.LocalDateTime
import kotlin.math.abs

class SeriesExercicioViewModel(
    val exercicioTreinoId: Int,
    val seriesRepository: SeriesRepository,
    val exercicioTreinoRepository: ExercicioTreinoRepository,
    val exercicioRepository: ExercicioRepository,
) : ViewModel() {
    private var cronometroJob: Job? = null
    private val _state = MutableStateFlow(SeriesExercicioState())
    val state = _state.asStateFlow()

    private val _cronometroState = MutableStateFlow(CronometroState())
    val cronometroState = _cronometroState.asStateFlow()

    private var pesoKg: Int = 0
    private var repeticoes = 0
    private var serieEmAndamento: SerieExercicio? = null
    private var _exercicioTreino: ExercicioTreino? = null
    private val exercicioTreino get() = _exercicioTreino!!
    private val seriesDoTreino: MutableList<SerieExercicio> = mutableListOf()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            _state.update { it.copy(carregando = true) }
            val exercicioTreinoResult = exercicioTreinoRepository.busca(exercicioTreinoId)
            state.value.copy(
                erro = exercicioTreinoResult.erroOrNull,
                carregando = exercicioTreinoResult.sucesso
            )
            if (!exercicioTreinoResult.sucesso) {
                return@launch
            }
            _exercicioTreino = exercicioTreinoResult.dataOrNull
            launch {
                val exercicioResult = exercicioRepository.busca(exercicioTreino.exercicioId)
                _state.update {
                    it.copy(
                        erro = exercicioResult.erroOrNull,
                        carregando = exercicioResult.sucesso
                    )
                }
                if (!exercicioResult.sucesso) {
                    return@launch
                }
                val exercicio = exercicioResult.dataOrNull
                _state.update {
                    it.copy(
                        observacaoExercicio = exercicio?.observacao ?: "",
                        carregando = false,
                        nomeExercicio = exercicio?.nome ?: ""
                    )
                }
            }
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
                return@launch
            }
            seriesDoTreino.addAll(seriesResult.dataOrNull!!)
        }
    }

    override fun onCleared() {
        super.onCleared()
    }

    fun iniciaDescanso() {
        val now = LocalDateTime.now()
        serieEmAndamento = serieEmAndamento?.copy(
            dhFimSerie = now,
            duracaoSegundos = Duration.between(
                serieEmAndamento!!.dhInicioSerie,
                now
            ).seconds.toInt(),
        )
        cronometroJob?.cancel()
        _cronometroState.update {
            it.copy(
                descansoAtivo = true, serieAtiva = false, segundosDescanso = 0
            )
        }
        cronometroJob = viewModelScope.launch(Dispatchers.Default) {
            while (true) {
                delay(1000L)
                _cronometroState.update { it.copy(segundosDescanso = it.segundosDescanso + 1) }
            }
        }
    }

    fun registraSerie() {
        viewModelScope.launch(Dispatchers.IO) {
            serieEmAndamento = serieEmAndamento?.copy(repeticoes = repeticoes)
            serieEmAndamento?.let {
                val result = seriesRepository.cria(it)
                _state.update {
                    it.copy(
                        erro = result.erroOrNull,
                        series = result.dataOrNull ?: it.series,
                    )
                }
                serieEmAndamento = null
                seriesRepository.todasDoTreino(exercicioTreino.treinoId).dataOrNull?.let {
                    seriesDoTreino.clear()
                    seriesDoTreino.addAll(it)
                }
            }
        }
    }

    fun iniciaSerie() {
        serieEmAndamento = SerieExercicio(
            serieId = 0,
            exercicioTreinoId = exercicioTreinoId,
            exercicioId = exercicioTreino.exercicioId,
            dhInicioSerie = LocalDateTime.now(),
            dhFimSerie = LocalDateTime.now(),
            duracaoSegundos = 0,
            segundosDescanso = seriesDoTreino.lastOrNull()?.dhFimSerie?.let {
                abs(Duration.between(LocalDateTime.now(), it).seconds).toInt()
            } ?: 0,
            pesoKg = this.pesoKg,
            repeticoes = 0,
            treinoId = exercicioTreino.treinoId,
        )
        cronometroJob?.cancel()
        _cronometroState.update {
            it.copy(
                descansoAtivo = false, serieAtiva = true, segundosSerie = 0
            )
        }
        cronometroJob = viewModelScope.launch(Dispatchers.Default) {
            while (true) {
                delay(1000L)
                _cronometroState.update { it.copy(segundosSerie = it.segundosSerie + 1) }
            }
        }
    }


    fun repeticoesMudou(repeticoes: String) {
        this.repeticoes = repeticoes.toIntOrNull() ?: 0
    }

    fun pesoMudou(texto: String) {
        this.pesoKg = texto.toIntOrNull() ?: 0
    }

    fun resetaEventos() {
        _state.update { it.resetaEventos() }
    }
}