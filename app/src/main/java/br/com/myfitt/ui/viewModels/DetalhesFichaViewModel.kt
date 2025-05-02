package br.com.myfitt.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.myfitt.data.entity.ExercicioComTipo
import br.com.myfitt.data.repository.ExercicioRepository
import br.com.myfitt.data.repository.FichaRepository
import br.com.myfitt.domain.models.Exercicio
import br.com.myfitt.ui.utils.io
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DetalhesFichaViewModel(
    private val fichaId: Int,
    private val fichaRepository: FichaRepository,
    private val exercicioRepository: ExercicioRepository
) : ViewModel() {
    val ficha = fichaRepository.getFichaByIdFlow(fichaId)
    fun getExerciciosSugestao(query: String) = exercicioRepository.getSugeridosExercicios(query)
    fun insertExercicioFicha(id: Int) {}
    fun insertExercicio(exercicio: Exercicio) = io{
        fichaRepository.addExercise(ficha.last(), exercicio)
    }

    fun moveExercisePositionDown(exercicio: Exercicio) =
        viewModelScope.launch(Dispatchers.IO) {
            fichaRepository.moveDown(ficha.last(), exercicio)
        }

    fun moveExercisePositionUp(exercicio: Exercicio) = io {
        fichaRepository.moveUp(ficha.last(), exercicio)
    }

    fun removeExercise(exercicio: Exercicio) = io {
        fichaRepository.removeExercise(ficha.last(), exercicio)
    }
}