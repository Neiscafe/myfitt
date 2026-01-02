package br.com.myfitt.treinos.ui.screens.menuPrincipal

import androidx.compose.runtime.DisposableEffect
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.myfitt.common.domain.Treino
import br.com.myfitt.common.domain.onErro
import br.com.myfitt.common.domain.onSucesso
import br.com.myfitt.treinos.domain.facade.TreinoFacade
import br.com.myfitt.treinos.domain.facade.TreinoFacadeState
import br.com.myfitt.treinos.domain.repository.TreinoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MenuPrincipalViewModel(
    private val treinoRepository: TreinoRepository,
    val treinoFacade: TreinoFacade,
) : ViewModel() {
    private val _state = MutableStateFlow(MenuPrincipalState())
    val state = _state.asStateFlow()
    private val _eventos = Channel<MenuPrincipalEvents>()
    val eventos = _eventos.receiveAsFlow()
    private val _treinoFacadeState = MutableStateFlow(TreinoFacadeState())
    val treinoAtualState = _treinoFacadeState.asStateFlow()
    var primeiroFetch = true

    init {
        buscaTreinoAtual()
        viewModelScope.launch(Dispatchers.IO) {
            while(true) {
                delay(60000)
                buscaTreinoAtual()
            }
        }
    }

    fun atualizarTreinoAtual(){
        if(!primeiroFetch) {
            buscaTreinoAtual()
        }else{
            primeiroFetch = false
        }
    }

    private fun buscaTreinoAtual() {
        viewModelScope.launch(Dispatchers.IO) {
            treinoFacade.buscarAtivo().onSucesso {
                _treinoFacadeState.value = it
            }.onErro { erro ->
                _state.update { it.copy(erro = erro) }
            }
        }
    }

    fun novoTreino() {
        viewModelScope.launch(Dispatchers.IO) {
            _state.update { it.copy(carregando = true) }
            val result = treinoRepository.criar(Treino(0))
            result.dataOrNull?.treinoId?.let {
                _eventos.send(MenuPrincipalEvents.NavegaTreino(it))
            }
            _state.update {
                it.copy(
                    carregando = false, erro = result.erroOrNull
                )
            }
        }
    }

    fun resetaEventos() = _state.update { it.resetaEventos() }
}