package br.com.myfitt.treinos.ui.screens.menuPrincipal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.myfitt.common.domain.Treino
import br.com.myfitt.treinos.domain.repository.TreinoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MenuPrincipalViewModel(private val treinoRepository: TreinoRepository) : ViewModel() {
    private val _state = MutableStateFlow(MenuPrincipalState())
    val state = _state.asStateFlow()
    fun novoTreino() {
        viewModelScope.launch(Dispatchers.IO) {
            _state.update { it.copy(carregando = true) }
            val result = treinoRepository.criar(Treino(0))
            _state.update {
                it.copy(
                    carregando = false, irParaTreino = result.dataOrNull, erro = result.erroOrNull
                )
            }
        }
    }

    fun resetaEventos() = _state.update { it.resetaEventos() }
}