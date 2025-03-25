package br.com.myfitt.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.myfitt.data.repository.ExercicioRepository
import br.com.myfitt.data.repository.TreinoExercicioRepository
import br.com.myfitt.domain.models.Exercicio
import br.com.myfitt.domain.models.ExercicioMudou
import br.com.myfitt.domain.models.TreinoExercicioComNome
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ExercicioViewModel(
    private val treinoId: Int,
    private val treinoExercicioRepository: TreinoExercicioRepository,
    private val exercicioRepository: ExercicioRepository,
) : ViewModel() {

    fun getExerciciosByTreino() = treinoExercicioRepository.getExerciciosDeUmTreino(treinoId)

    fun insertExercicio(exercicio: TreinoExercicioComNome) {
        viewModelScope.launch {
            treinoExercicioRepository.addExercicioAoTreino(exercicio)
        }
    }

    fun getSugestoes(query: String) = exercicioRepository.getSugeridosExercicios(query)


    fun updateTreinoExercicio(exercicio: TreinoExercicioComNome, mudou: ExercicioMudou) = viewModelScope.launch() {
        treinoExercicioRepository.updateExercicioDoTreino(exercicio, mudou)
    }

    fun updateNome(exercicio: TreinoExercicioComNome) = viewModelScope.launch() {
        exercicioRepository.updateExercicio(
            Exercicio(
                exercicio.exercicioNome, exercicio.exercicioId
            )
        )
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
            treinoExercicioRepository.subirUmaPosicao(exercicio)
        }

    fun moveExerciseDownByOne(exercicio: TreinoExercicioComNome) =
        viewModelScope.launch {
            treinoExercicioRepository.descerUmaPosicao(exercicio)
        }
}