package br.com.myfitt.treinos.ui.screens.exerciciosTreino

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    fun interagir(interacao: Interacao) {
        when (val it = interacao) {
            is Interacao.Remover -> viewModelScope.launch(Dispatchers.IO) {
                _state.update { it.copy(carregando = true) }
                val result = treinoRepository.remove(it.exercicioTreino)
                _state.update {
                    it.copy(
                        carregando = false,
                        erro = result.erroOrNull,
                        exercicios = result.dataOrNull ?: it.exercicios
                    )
                }
            }

            is Interacao.Reposicionar -> viewModelScope.launch(Dispatchers.IO) {
                _state.update { it.copy(carregando = true) }
                val result = treinoRepository.reordena(it.reposicionar, it.posicao)
                _state.update {
                    it.copy(
                        carregando = false,
                        erro = result.erroOrNull,
                        exercicios = result.dataOrNull ?: it.exercicios
                    )
                }
            }

            is Interacao.Substituir -> viewModelScope.launch(Dispatchers.IO) {
                _state.update { it.copy(carregando = true) }
                val result = treinoRepository.substitui(it.novo)
                _state.update {
                    it.copy(
                        carregando = false,
                        erro = result.erroOrNull,
                        exercicios = result.dataOrNull ?: it.exercicios
                    )
                }
            }

            is Interacao.Adicionar -> viewModelScope.launch(Dispatchers.IO) {
                _state.update { it.copy(carregando = true) }
                val result = treinoRepository.adiciona(it.novo)
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
        _state.update { it.copy(erro = null, irParaSeries = null, irParaSubstituicao = false) }
    }
}
