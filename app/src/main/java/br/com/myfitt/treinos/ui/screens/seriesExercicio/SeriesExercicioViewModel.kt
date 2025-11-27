package br.com.myfitt.treinos.ui.screens.seriesExercicio

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.myfitt.treinos.domain.repository.SeriesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SeriesExercicioViewModel(val exercicioTreinoId: Int, val seriesRepository: SeriesRepository) :
    ViewModel() {
    private var cronometroJob: Job? = null
    private val _state = MutableStateFlow(SeriesExercicioState())
    val state = _state.asStateFlow()

    private val _descansoState = MutableStateFlow(CronometroState())
    val descansoState = _descansoState.asStateFlow()

    private val _duracaoSerieState = MutableStateFlow(CronometroState())
    val duracaoSerieState = _duracaoSerieState.asStateFlow()

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

    fun repeticoesMudou(repeticoes: String) {

    }

    fun iniciaDescanso(cronometroState: CronometroState? = null) {
        cronometroJob?.cancel()
        _duracaoSerieState.update { it.copy(ativo = false) }
        _descansoState.update { it.copy(ativo = true, segundos = cronometroState?.segundos ?: 0) }
        cronometroJob = viewModelScope.launch(Dispatchers.Default) {
            while (true) {
                delay(1000L)
                _descansoState.update { it.copy(segundos = it.segundos + 1) }
                Log.d("Teste", "${descansoState.value.segundos} de descanso")
            }
        }
    }

    fun paraCronometros(): CronometroState? {
        cronometroJob?.cancel()
        val cronometroAtual =
            if (duracaoSerieState.value.ativo) duracaoSerieState.value else if (descansoState.value.ativo) descansoState.value else null
        _duracaoSerieState.update { it.copy(ativo = false) }
        _descansoState.update { it.copy(ativo = false) }
        return cronometroAtual
    }

    fun iniciaSerie(cronometroState: CronometroState? = null) {
        cronometroJob?.cancel()
        _descansoState.update { it.copy(ativo = false) }
        _duracaoSerieState.update {
            it.copy(
                ativo = true, segundos = cronometroState?.segundos ?: 0
            )
        }
        cronometroJob = viewModelScope.launch(Dispatchers.Default) {
            var duracao: Int
            while (true) {
                delay(1000L)
                duracao = duracaoSerieState.value.segundos + 1
                _duracaoSerieState.update { it.copy(segundos = duracao) }
                Log.d("Teste", "${duracao} de serie")
            }
        }
    }


    fun pesoMudou(texto: String) {

    }

    fun resetaEventos() {
        _state.update { it.resetaEventos() }
    }

    fun aplicaRepeticoes() {

    }
}