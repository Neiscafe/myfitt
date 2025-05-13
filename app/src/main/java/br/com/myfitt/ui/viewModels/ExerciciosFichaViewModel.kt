package br.com.myfitt.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.myfitt.data.repository.ExercicioRepository
import br.com.myfitt.data.repository.FichaRepository
import br.com.myfitt.domain.models.Exercicio
import br.com.myfitt.domain.models.TipoExercicio
import br.com.myfitt.ui.utils.io
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ExerciciosFichaViewModel(
    private val fichaId: Int,
    private val fichaRepository: FichaRepository,
    private val exercicioRepository: ExercicioRepository
) : ViewModel() {
    val exerciciosFicha = fichaRepository.getFichaExerciciosFlow(fichaId).stateIn(
        viewModelScope, SharingStarted.Eagerly, emptyList()
    )
    val tiposExercicio = exercicioRepository.getTiposFlow()
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())


    suspend fun getExerciciosSugestao(query: String) =
        exercicioRepository.getSugeridosExercicios(query)

    fun insertExercicioFicha(exercicioId: Int) = io {
        fichaRepository.addExerciseToFicha(
            Exercicio(
                id = exercicioId, posicao = exerciciosFicha.value.size
            ), fichaId
        )
    }

    fun insertExercicio(exercicioNome: String) {
        io {
            val exercicioAdicionado =
                exercicioRepository.insertExercicio(Exercicio(nome = exercicioNome))
            fichaRepository.addExerciseToFicha(exercicioAdicionado, fichaId)
        }
    }

    fun moveExerciseDown(exercicio: Exercicio) {
        viewModelScope.launch(Dispatchers.IO) {
            fichaRepository.increasePosition(exercicio)
        }
    }

    fun moveExerciseUp(exercicio: Exercicio) {
        io {
            fichaRepository.decreasePosition(exercicio)
        }
    }

    fun removeExercise(exercicio: Exercicio) {
        io {
            fichaRepository.removeExercise(exercicio)
        }
    }

    fun setTipoExercicio(exercicio: Exercicio, tipoExercicio: TipoExercicio?) {
        io {
            exercicioRepository.updateExercicio(exercicio.copy(tipo = tipoExercicio))
        }
    }

    fun deleteExercicio(exercicio: Exercicio) {
        io {
            exercicioRepository.deleteExercicio(exercicio)
        }
    }
}