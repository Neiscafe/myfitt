package br.com.myfitt.treinos.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.myfitt.common.domain.ExercicioTreino
import br.com.myfitt.treinos.domain.repository.ExercicioTreinoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ExerciciosTreinoViewModel(treinoId: Int, val treinoRepository: ExercicioTreinoRepository) :
    ViewModel() {
    private val _state = MutableStateFlow(ExerciciosTreinoState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            _state.update { it.copy(carregando = true) }
            val result = treinoRepository.lista(treinoId)
            _state.update {
                it.copy(
                    erro = result.erroOrNull,
                    exercicios = result.dataOrNull ?: it.exercicios,
                    carregando = false
                )
            }
        }
    }

    fun clicks(i: Int, exercicioItem: ExercicioTreino) {
        viewModelScope.launch(Dispatchers.IO) {
            _clicks(i, exercicioItem)
        }
    }

    suspend fun _clicks(i: Int, exercicioItem: ExercicioTreino) {
        when (i) {
            cardClick -> {
                _state.update { it.copy(irParaSeries = exercicioItem) }
            }

            removerClick -> {
                _state.update { it.copy(carregando = true) }
                val result = treinoRepository.remove(exercicioItem)
                _state.update {
                    it.copy(
                        carregando = false,
                        erro = result.erroOrNull,
                        exercicios = result.dataOrNull ?: it.exercicios
                    )
                }
            }

            substituirClick -> {
                _state.update { it.copy(irParaSubstituicao = exercicioItem) }
            }

            arrastarClick -> {
                _state.update { it.copy(carregando = true) }
                val result = treinoRepository.reordena(exercicioItem, exercicioItem.ordem)
                _state.update {
                    it.copy(
                        carregando = false,
                        erro = result.erroOrNull,
                        exercicios = result.dataOrNull ?: it.exercicios
                    )
                }
            }
        }
    }

    fun limpaEvents() {
        _state.update { it.copy(erro = null, irParaSeries = null, irParaSubstituicao = null) }
    }

    companion object {
        const val cardClick = 0
        const val removerClick = 1
        const val substituirClick = 2
        const val arrastarClick = 3
    }
}
