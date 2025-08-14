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
import br.com.myfitt.ui.screens.exerciciosTreino.ListaExerciciosTreinoState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ExerciciosTreinoViewModel(
    private val treinoId: Int,
    private val treinoExercicioRepository: TreinoExercicioRepository,
    private val exercicioRepository: ExercicioRepository,
    private val fichaRepository: FichaRepository
) : ViewModel() {
    val _exerciciosByTreino = MutableStateFlow(ListaExerciciosTreinoState(emptyList()))
    val exerciciosByTreino = _exerciciosByTreino.asStateFlow()

    init {
        _exerciciosByTreino.update {
            it.copy(
                treinoExercicioRepository.getExerciciosDeUmTreino(
                    treinoId
                )
            )
        }
    }

    fun getHistorico(treinoExercicio: TreinoExercicioComNome): Flow<Loadable<List<HistoricoExercicioTreinos>?>> {
        return merge(
            flowOf(Loadable.Loading),
            treinoExercicioRepository.getHistorico(treinoExercicio.exercicioId).map {
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

    fun updateTreinoExercicio(exercicio: TreinoExercicioComNome, mudou: ExercicioMudou) {
        _exerciciosByTreino.edit { this[exercicio.posicao] = exercicio }
        val updateScope = CoroutineScope(Dispatchers.IO)
        updateScope.launch(Dispatchers.IO) {
            treinoExercicioRepository.updateExercicioDoTreino(exercicio, mudou)
            updateScope.cancel()
        }
    }

    fun deleteExercicio(exercicio: Exercicio) {
        viewModelScope.launch(Dispatchers.IO) {
            exercicioRepository.deleteExercicio(exercicio)
        }
    }

    fun deleteExercicioDoTreino(exercicio: TreinoExercicioComNome) {
        _exerciciosByTreino.edit { removeAt(exercicio.posicao) }
        viewModelScope.launch(Dispatchers.IO) {
            treinoExercicioRepository.removeExercicioDoTreino(exercicio)
        }
    }

    fun moveExerciseUpByOne(exercicio: TreinoExercicioComNome) {
        _exerciciosByTreino.edit {
            if (exercicio.posicao > 0) {
                val previous = get(exercicio.posicao - 1)
                set(exercicio.posicao - 1, exercicio)
                set(previous.posicao + 1, previous)
            }
        }
        viewModelScope.launch(Dispatchers.IO) {
            treinoExercicioRepository.diminuirPosicao(exercicio)
        }
    }

    fun moveExerciseDownByOne(exercicio: TreinoExercicioComNome) {
        _exerciciosByTreino.edit {
            if (exercicio.posicao < (size - 1)) {
                val next = get(exercicio.posicao + 1)
                set(exercicio.posicao + 1, exercicio)
                set(next.posicao - 1, next)
            }
        }
        viewModelScope.launch(Dispatchers.IO) {
            treinoExercicioRepository.aumentarPosicao(exercicio)
        }
    }

    fun applyFicha(selectedFicha: Ficha) = viewModelScope.launch(Dispatchers.IO) {
        val exerciciosFicha = fichaRepository.getFichaExercicios(selectedFicha.id)
        treinoExercicioRepository.addFromFicha(exerciciosFicha, treinoId)
    }

    private fun MutableStateFlow<ListaExerciciosTreinoState>.edit(action: MutableList<TreinoExercicioComNome>.() -> Unit) {
        val list = this.value.exercicios.toMutableList()
        val update = action(list)
        this.update { it.copy(exercicios = list) }
    }
}