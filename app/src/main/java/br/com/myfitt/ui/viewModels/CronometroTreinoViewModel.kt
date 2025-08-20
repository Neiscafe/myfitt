package br.com.myfitt.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.myfitt.data.repository.CronometroRepository
import br.com.myfitt.data.repository.ExercicioRepository
import br.com.myfitt.data.repository.TreinoExercicioRepository
import br.com.myfitt.domain.models.Exercicio
import br.com.myfitt.domain.models.ExercicioTreino
import br.com.myfitt.ui.screens.cronometro.CronometroActions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CronometroTreinoViewModel(
    val treinoId: Int,
    val lastExercicioTreinoId: Int?,
    val repository: TreinoExercicioRepository,
    val exercicioRepository: ExercicioRepository,
    val cronometroRepository: CronometroRepository,
) : ViewModel() {
    data class CronometroTreinoState(
        val selectedExercicioTreino: ExercicioTreino? = null,
        val exercicios: List<Exercicio> = emptyList()
    )

    fun onCronometroAction(cronometroActions: CronometroActions) {
        when (val it = cronometroActions) {
            is CronometroActions.StartAndListenCronometro -> {
                cronometroRepository.startTimerAndListen(it.type, it.callback)
            }

            is CronometroActions.StopCronometro -> {
                cronometroRepository.stopCurrentTimer()
            }

            is CronometroActions.StopListeningCronometro -> {
                cronometroRepository.stopListening()
            }

            is CronometroActions.StartListeningCronometro -> {
                cronometroRepository.startListening(it.callback)
            }
        }
    }

    fun selectExercise(exercicio: br.com.myfitt.domain.models.Exercicio) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addExercicioAoTreino(
                treinoId, exercicio
            )?.let {
                repository.getTreinoExercicioSeriesById(
                    it.toInt()
                ).take(1).collect {
                    _state.update { _ ->
                        _state.value.copy(selectedExercicioTreino = it)
                    }
                }
            }
        }
    }

    val _state = MutableStateFlow<CronometroTreinoState>(CronometroTreinoState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            launch {
                lastExercicioTreinoId?.let {
                    repository.getTreinoExercicioSeriesById(it).take(1).collect {
                        _state.update { _ -> _state.value.copy(selectedExercicioTreino = it) }
                    }
                }
            }
            launch {
                exercicioRepository.getAllExercicios().take(1).collect {
                    _state.update { _ -> _state.value.copy(exercicios = it) }
                }
            }
        }

    }


}