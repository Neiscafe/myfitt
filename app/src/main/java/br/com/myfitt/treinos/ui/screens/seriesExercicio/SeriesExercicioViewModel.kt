package br.com.myfitt.treinos.ui.screens.seriesExercicio

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.myfitt.common.domain.SerieExercicio
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

class SeriesExercicioViewModel(val exercicioTreinoId: Int, val seriesRepository: SeriesRepository) :
    ViewModel() {
    private var cronometroJob: Job? = null
    private val _state = MutableStateFlow(SeriesExercicioState())
    val state = _state.asStateFlow()

    private val _cronometroState = MutableStateFlow(CronometroState())
    val cronometroState = _cronometroState.asStateFlow()

    private var pesoKg: Int = 0
    private var repeticoes = 0
    private var serieAtual: SerieExercicio? = null


    init {
        viewModelScope.launch(Dispatchers.IO) {
            _state.update { it.copy(carregando = true) }
            val result = seriesRepository.lista(exercicioTreinoId)
            _state.update {
                it.copy(
                    series = result.dataOrNull ?: it.series,
                    erro = result.erroOrNull,
                    carregando = false
                )
            }
        }
    }

    fun iniciaDescanso() {
        val cronometroAtual = cronometroState.value
        serieAtual = serieAtual?.copy(
            dhFimSerie = LocalDateTime.now(),
            duracaoSegundos = cronometroAtual.segundosSerie,
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
            serieAtual = serieAtual?.copy(repeticoes = repeticoes)
            serieAtual?.let {
                val result = seriesRepository.cria(it)
                _state.update {
                    it.copy(
                        erro = result.erroOrNull,
                        series = result.dataOrNull ?: it.series,
                    )
                }
                serieAtual = null
            }
        }
    }

    fun iniciaSerie() {
        val anterior = state.value.series.lastOrNull()
        serieAtual = SerieExercicio(
            serieId = 0,
            exercicioTreinoId = exercicioTreinoId,
            exercicioId = 0,
            dhInicioSerie = LocalDateTime.now(),
            dhFimSerie = LocalDateTime.now(),
            duracaoSegundos = 0,
            segundosDescanso = anterior?.dhFimSerie?.let {
                abs(Duration.between(LocalDateTime.now(), it).seconds).toInt()
            } ?: 0,
            pesoKg = this.pesoKg,
            repeticoes = 0,
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