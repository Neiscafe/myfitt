package br.com.myfitt.ui.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.myfitt.data.repository.ExercicioRepository
import br.com.myfitt.data.repository.TreinoExercicioRepository
import br.com.myfitt.domain.models.Exercicio
import br.com.myfitt.domain.models.ExercicioMudou
import br.com.myfitt.domain.models.TreinoExercicioComNome
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ExerciciosTreinoViewModel(
    private val treinoId: Int,
    private val treinoExercicioRepository: TreinoExercicioRepository,
    private val exercicioRepository: ExercicioRepository,
) : ViewModel() {
    val exerciciosByTreino = treinoExercicioRepository.getExerciciosDeUmTreino(treinoId).stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = emptyList()
    )

    fun insertExercicio(nomeExercicio: String) {
        viewModelScope.launch {
            treinoExercicioRepository.addExercicioAoTreino(
                treinoId, Exercicio(
                    id = 0,
                    posicao = exerciciosByTreino.value.size,
                    nome = nomeExercicio,
                )
            )
        }
    }

    fun insertExercicio(exercicio: Exercicio) {
        viewModelScope.launch(Dispatchers.IO) {
            treinoExercicioRepository.addExercicioAoTreino(
                treinoId, exercicio.copy(posicao = exerciciosByTreino.value.size,)
            )
        }
    }

    fun getSugestoes(query: String) = exercicioRepository.getSugeridosExercicios(query)


    fun updateTreinoExercicio(exercicio: TreinoExercicioComNome, mudou: ExercicioMudou) =
        viewModelScope.launch() {
            treinoExercicioRepository.updateExercicioDoTreino(exercicio, mudou)
        }

    fun deleteExercicio(exercicio: Exercicio) {
        viewModelScope.launch {
            exercicioRepository.deleteExercicio(exercicio)
        }
    }

    fun deleteExercicioDoTreino(exercicio: TreinoExercicioComNome) {
        viewModelScope.launch {
            treinoExercicioRepository.removeExercicioDoTreino(exercicio)
        }
    }

    fun moveExerciseUpByOne(exercicio: TreinoExercicioComNome) =
        viewModelScope.launch {
            treinoExercicioRepository.diminuirPosicao(exercicio)
        }

    fun moveExerciseDownByOne(exercicio: TreinoExercicioComNome) =
        viewModelScope.launch {
            treinoExercicioRepository.aumentarPosicao(exercicio)
        }
}