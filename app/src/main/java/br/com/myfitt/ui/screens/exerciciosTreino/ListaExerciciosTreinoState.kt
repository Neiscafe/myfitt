package br.com.myfitt.ui.screens.exerciciosTreino

import br.com.myfitt.domain.models.TreinoExercicioComNome
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

data class ListaExerciciosTreinoState(
    val exercicios: List<TreinoExercicioComNome>,
) {
    fun edit(action: MutableList<TreinoExercicioComNome>.() -> Unit): ListaExerciciosTreinoState {
        val list = exercicios.toMutableList()
        val result = action(list)
        return copy(exercicios = list)
    }

    fun update(
        state: MutableStateFlow<ListaExerciciosTreinoState>,
        action: (MutableList<TreinoExercicioComNome>) -> Unit
    ) {
        state.update { this.edit(action) }
    }
}