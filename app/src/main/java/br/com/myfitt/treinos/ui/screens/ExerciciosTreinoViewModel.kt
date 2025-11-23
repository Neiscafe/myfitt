package br.com.myfitt.treinos.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.myfitt.common.domain.ExercicioTreino
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ExerciciosTreinoViewModel(treinoId: Int) : ViewModel() {
    private val _state = MutableStateFlow(ExerciciosTreinoState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            delay(1500L)
            val erro = "Bla bla bla bla"
            _state.update {
                it.copy(
                    erro = erro, exercicios = listOf(
                        ExercicioTreino(1, 1, 1),
                        ExercicioTreino(2, 1, 1),
                        ExercicioTreino(3, 1, 1),
                    ), carregando = false
                )
            }
        }
    }

    fun clicks(i: Int, exercicioItem: ExercicioTreino) {
        when (i) {
            cardClick -> {
                _state.update { it.copy(erro = "Séries detalhadas: Não implementado.") }
            }
            removerClick -> {
//                _state.update { it.copy(exercicios = it.exercicios.minus(exercicioItem)) }
                _state.update { it.copy(erro = "Remover: Não implementado.") }
            }

            substituirClick -> {
                _state.update { it.copy(erro = "Substituir: Não implementado.") }
            }
            arrastarClick -> {
                _state.update { it.copy(erro = "Arrastar: Não implementado.") }
            }
        }
    }

    fun limpaEvents() {
        _state.update { it.copy(erro = null) }
    }

    companion object {
        const val cardClick = 0
        const val removerClick = 1
        const val substituirClick = 2
        const val arrastarClick = 3
    }
}
