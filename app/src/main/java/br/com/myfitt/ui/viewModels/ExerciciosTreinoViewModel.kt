package br.com.myfitt.ui.viewModels

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.myfitt.data.repository.ExercicioRepository
import br.com.myfitt.data.repository.FichaRepository
import br.com.myfitt.data.repository.TreinoExercicioRepository
import br.com.myfitt.domain.models.Exercicio
import br.com.myfitt.domain.models.ExercicioTreino
import br.com.myfitt.domain.models.Ficha
import br.com.myfitt.domain.models.HistoricoExercicioTreinos
import br.com.myfitt.ui.components.Loadable
import br.com.myfitt.ui.screens.exerciciosTreino.ListaExerciciosActions
import br.com.myfitt.ui.viewModels.ExerciciosTreinoViewModel.ListaExerciciosTreinoState.Dialog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOf
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

    @Stable
    data class ListaExerciciosTreinoState(
        val items: List<ExercicioTreino> = emptyList(),
        val showingDialog: Dialog? = null,
        val sugestoes: List<Exercicio> = emptyList()
    ) {
        sealed class Dialog {
            data class DisableExercise(val exercise: Exercicio): Dialog()
            data class History(
                val loading: Boolean, val items: List<HistoricoExercicioTreinos>? = null
            ) : Dialog()
        }
    }

    init {
        viewModelScope.launch {
            treinoExercicioRepository.getExerciciosDeUmTreino(treinoId).collect { value ->
                    _state.update { it.copy(items = value) }
                }
        }
    }

    val _state = MutableStateFlow(ListaExerciciosTreinoState())
    val exerciciosByTreino: StateFlow<ListaExerciciosTreinoState> = _state.asStateFlow()

    fun closeDialog() {
        _state.update { it.copy(showingDialog = null) }
    }

    val fichas = fichaRepository.getTodasFichasFlow()
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
    fun actions(action: ListaExerciciosActions) {
        val updateScope = CoroutineScope(Dispatchers.IO)
        updateScope.launch {
            when (val it = action) {
                is ListaExerciciosActions.AddSerie -> treinoExercicioRepository.addSeries(it.serie)
                is ListaExerciciosActions.DeleteSerie -> treinoExercicioRepository.removeSeries(it.serie)
                is ListaExerciciosActions.UpdateSerie -> treinoExercicioRepository.updateSeries(it.serie)
                is ListaExerciciosActions.AddAndInsertExercicio -> treinoExercicioRepository.addExercicioAoTreino(
                    treinoId, Exercicio(
                        id = 0,
                        posicao = exerciciosByTreino.value.items.size,
                        nome = it.name,
                    )
                )

                is ListaExerciciosActions.DeleteExercicio -> exercicioRepository.deleteExercicio(it.exercicio)
                is ListaExerciciosActions.AddExercicioTreino -> treinoExercicioRepository.addExercicioAoTreino(
                    treinoId, it.exercicio.copy(posicao = exerciciosByTreino.value.items.size)
                )

                is ListaExerciciosActions.DeleteExercicioTreino -> treinoExercicioRepository.removeExercicioDoTreino(
                    it.exercicioTreino
                )

                is ListaExerciciosActions.MoveDownExercicioTreino -> treinoExercicioRepository.aumentarPosicao(
                    it.exercicioTreino
                )

                is ListaExerciciosActions.MoveUpExercicioTreino -> treinoExercicioRepository.diminuirPosicao(
                    it.exercicioTreino
                )

                is ListaExerciciosActions.GetAllExercicioTreino -> {
                    _state.update {
                        it.copy(
                            showingDialog = History(
                                true, null
                            )
                        )
                    }
                    treinoExercicioRepository.getHistorico(it.exercicio.id).collect { historico ->
                        _state.update {
                            it.copy(
                                showingDialog = History(
                                    false, historico
                                )
                            )
                        }
                        this.cancel()
                    }
                }

                is ListaExerciciosActions.GetSugestoes -> {
                    _state.update { state ->
                        state.copy(
                            sugestoes = exercicioRepository.getSugeridosExercicios(
                                it.query
                            )
                        )
                    }
                }
            }
            updateScope.cancel()
        }
    }

    fun applyFicha(selectedFicha: Ficha) = viewModelScope.launch(Dispatchers.IO) {
        val exerciciosFicha = fichaRepository.getFichaExercicios(selectedFicha.id)
        treinoExercicioRepository.addFromFicha(exerciciosFicha, treinoId)
    }
}