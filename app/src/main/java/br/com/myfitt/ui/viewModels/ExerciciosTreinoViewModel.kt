package br.com.myfitt.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.myfitt.data.repository.ExercicioRepository
import br.com.myfitt.data.repository.FichaRepository
import br.com.myfitt.data.repository.TreinoExercicioRepository
import br.com.myfitt.domain.models.Exercicio
import br.com.myfitt.domain.models.ExercicioMudou
import br.com.myfitt.domain.models.Ficha
import br.com.myfitt.domain.models.HistoricoExercicioTreinos
import br.com.myfitt.domain.models.TreinoExercicioComNome
import br.com.myfitt.ui.components.Loadable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ExerciciosTreinoViewModel(
    private val treinoId: Int,
    private val treinoExercicioRepository: TreinoExercicioRepository,
    private val exercicioRepository: ExercicioRepository,
    private val fichaRepository: FichaRepository
) : ViewModel() {
    val exerciciosByTreino = treinoExercicioRepository.getExerciciosDeUmTreino(treinoId).stateIn(
        scope = viewModelScope, started = SharingStarted.Eagerly, initialValue = emptyList()
    )

    fun getHistorico(treinoExercicio: TreinoExercicioComNome): Flow<Loadable<List<HistoricoExercicioTreinos>?>> {
        return merge(
            flowOf(Loadable.Loading),
            treinoExercicioRepository.getHistorico(treinoExercicio.exercicioId).map {
                Loadable.Loaded(it)
            })
    }

    val fichas = fichaRepository.getTodasFichasFlow()
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

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
                treinoId, exercicio.copy(posicao = exerciciosByTreino.value.size)
            )
        }
    }

    suspend fun getSugestoes(query: String) = exercicioRepository.getSugeridosExercicios(query)


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

    fun moveExerciseUpByOne(exercicio: TreinoExercicioComNome) = viewModelScope.launch {
        treinoExercicioRepository.diminuirPosicao(exercicio)
    }

    fun moveExerciseDownByOne(exercicio: TreinoExercicioComNome) = viewModelScope.launch {
        treinoExercicioRepository.aumentarPosicao(exercicio)
    }

    fun applyFicha(selectedFicha: Ficha) = viewModelScope.launch() {
        val exerciciosFicha = fichaRepository.getFichaExercicios(selectedFicha.id)
        treinoExercicioRepository.addFromFicha(exerciciosFicha, treinoId)
    }
}