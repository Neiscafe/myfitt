package br.com.myfitt.treinos.ui.screens.seriesExercicio

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.myfitt.common.domain.Exercicio
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
    private var exercicio: Exercicio? = null
    private var exercicioTreino: ExercicioTreino? = null

    init {
        viewModelScope.launch(Dispatchers.IO) {
            _state.update { it.copy(carregando = true) }
            launch {
                val result = seriesRepository.lista(exercicioTreinoId)
                _state.update {
                    it.copy(
                        series = result.dataOrNull ?: state.value.series,
                        erro = result.erroOrNull,
                        carregando = result.sucesso
                    )
                }
            }
            val result = exercicioTreinoRepository.busca(exercicioTreinoId)
            state.value.copy(
                erro = result.erroOrNull,
                carregando = result.sucesso
            )
            if (!result.sucesso) {
                return@launch
            }
            exercicioTreino = result.dataOrNull
            val result2 = exercicioRepository.busca(exercicioTreino!!.exercicioId)
            _state.update { it.copy(erro = result2.erroOrNull, carregando = result2.sucesso) }
            if (!result2.sucesso) {
                return@launch
            }
            exercicio = result2.dataOrNull
            _state.update {
                it.copy(
                    observacaoExercicio = exercicio?.observacao ?: "",
                    carregando = false,
                    nomeExercicio = exercicio?.nome ?: ""
                )
            }
        }
    }

    fun iniciaDescanso() {
        val cronometroAtual = cronometroState.value
        serieEmAndamento = serieEmAndamento?.copy(
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
            }
        }
    }

    fun iniciaSerie() {
        val anterior = state.value.series.lastOrNull()
        serieEmAndamento = SerieExercicio(
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