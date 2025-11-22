package br.com.myfitt.treinos.ui.screens

import androidx.lifecycle.ViewModel
import br.com.myfitt.common.domain.ExercicioTreino
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ExerciciosTreinoViewModel(treinoId: Int) : ViewModel() {
    private val _state = MutableStateFlow(ExerciciosTreinoState())
    val state = _state.asStateFlow()
    fun clicks(i: Int, exercicioItem: ExercicioTreino) {
        when (i) {
            cardClick->{}
            removerClick->{}
            substituirClick->{}
            arrastarClick->{}
        }
    }

    companion object {
        const val cardClick = 0
        const val removerClick = 1
        const val substituirClick = 2
        const val arrastarClick = 3
    }
}
