package br.com.myfitt.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.myfitt.data.repository.ExercicioRepository
import br.com.myfitt.data.repository.FichaRepository
import br.com.myfitt.data.repository.TreinoExercicioRepository
import br.com.myfitt.domain.models.Exercicio
import br.com.myfitt.domain.models.ExercicioTreino
import br.com.myfitt.domain.models.Ficha
import br.com.myfitt.domain.models.HistoricoExercicioTreinos
import br.com.myfitt.domain.models.Serie
import br.com.myfitt.ui.components.Loadable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
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
        viewModelScope,
        SharingStarted.Eagerly, emptyList()
    )

    fun getHistorico(treinoExercicio: ExercicioTreino): Flow<Loadable<List<HistoricoExercicioTreinos>?>> {
        return merge(
            flowOf(Loadable.Loading),
            treinoExercicioRepository.getHistorico(treinoExercicio.exercicio.id).map {
                Loadable.Loaded(it)
            })
    }

    val fichas = fichaRepository.getTodasFichasFlow()
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    fun addAndInsertExercicio(nomeExercicio: String) {
        viewModelScope.launch(Dispatchers.IO) {
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

    fun updateSerie(serie: Serie) {
        val updateScope = CoroutineScope(Dispatchers.IO)
        updateScope.launch(Dispatchers.IO) {
            treinoExercicioRepository.updateSeries(serie)
            updateScope.cancel()
        }
    }

    fun deleteSerie(serie: Serie) {
        val updateScope = CoroutineScope(Dispatchers.IO)
        updateScope.launch(Dispatchers.IO) {
            treinoExercicioRepository.removeSeries(serie)
            updateScope.cancel()
        }
    }

    fun addSerie(serie: Serie) {
        val updateScope = CoroutineScope(Dispatchers.IO)
        updateScope.launch(Dispatchers.IO) {
            treinoExercicioRepository.addSeries(serie)
            updateScope.cancel()
        }
    }


    fun deleteExercicio(exercicio: Exercicio) {
        viewModelScope.launch(Dispatchers.IO) {
            exercicioRepository.deleteExercicio(exercicio)
        }
    }

    fun deleteExercicioDoTreino(exercicio: ExercicioTreino) {
        viewModelScope.launch(Dispatchers.IO) {
            treinoExercicioRepository.removeExercicioDoTreino(exercicio)
        }
    }

    fun moveExerciseUpByOne(exercicio: ExercicioTreino) {
        viewModelScope.launch(Dispatchers.IO) {
            treinoExercicioRepository.diminuirPosicao(exercicio)
        }
    }

    fun moveExerciseDownByOne(exercicio: ExercicioTreino) {
        viewModelScope.launch(Dispatchers.IO) {
            treinoExercicioRepository.aumentarPosicao(exercicio)
        }
    }

    fun applyFicha(selectedFicha: Ficha) = viewModelScope.launch(Dispatchers.IO) {
        val exerciciosFicha = fichaRepository.getFichaExercicios(selectedFicha.id)
        treinoExercicioRepository.addFromFicha(exerciciosFicha, treinoId)
    }
}