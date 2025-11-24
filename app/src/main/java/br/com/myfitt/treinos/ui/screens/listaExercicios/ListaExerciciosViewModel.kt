package br.com.myfitt.treinos.ui.screens.listaExercicios

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.myfitt.common.domain.Exercicio
import br.com.myfitt.treinos.domain.repository.ExercicioRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ListaExerciciosViewModel(private val exercicioRepository: ExercicioRepository) : ViewModel() {
    private val _state = MutableStateFlow(ListaExerciciosState())
    private val state = _state.asStateFlow()
    private var pesquisa: String = ""
    private var pesquisaJob: Job? = null

    companion object {
        private var callback: ((Exercicio) -> Unit)? = null
        fun setCallback(callback: (Exercicio) -> Unit) {
            this.callback = callback
        }
    }

    init {
        callback = null
    }

    override fun onCleared() {
        callback = null
    }

    fun pesquisaMudou(pesquisa: String) {
        this.pesquisa = pesquisa.trim()
        if (this.pesquisa.length < 3) return
        pesquisaJob?.cancel()
        pesquisaJob = viewModelScope.launch(Dispatchers.IO) {
            _state.update { it.copy(carregando = false) }
            val result = exercicioRepository.lista(this@ListaExerciciosViewModel.pesquisa)
            _state.update {
                it.copy(
                    carregando = false, erro = result.erroOrNull, atualizarItens = result.dataOrNull
                )
            }
        }
    }
}