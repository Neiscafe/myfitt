package br.com.myfitt.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.myfitt.data.repository.ExercicioRepository
import br.com.myfitt.data.repository.FichaRepository
import br.com.myfitt.domain.models.Exercicio
import br.com.myfitt.ui.utils.io
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ExerciciosFichaViewModel(
    private val fichaId: Int,
    private val fichaRepository: FichaRepository,
    private val exercicioRepository: ExercicioRepository
) : ViewModel() {
    val ficha = fichaRepository.getFichaExerciciosFlow(fichaId).stateIn(
        viewModelScope, SharingStarted.Eagerly, emptyList()
    )

    fun getExerciciosSugestao(query: String) = exercicioRepository.getSugeridosExercicios(query)
    fun insertExercicioFicha(id: Int) {}
    fun insertExercicio(exercicio: Exercicio) = io {
        fichaRepository.addExercise(exercicio)
    }

    fun moveExercisePositionDown(exercicio: Exercicio) = viewModelScope.launch(Dispatchers.IO) {
        fichaRepository.decreasePosition(exercicio)
    }

    fun moveExercisePositionUp(exercicio: Exercicio) = io {
        fichaRepository.increasePosition(exercicio)
    }

    fun removeExercise(exercicio: Exercicio) = io {
        fichaRepository.removeExercise(exercicio)
    }
}