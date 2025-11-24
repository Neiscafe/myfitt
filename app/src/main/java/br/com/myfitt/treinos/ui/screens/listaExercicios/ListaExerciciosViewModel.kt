package br.com.myfitt.treinos.ui.screens.listaExercicios

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.myfitt.common.domain.Exercicio
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ListaExerciciosViewModel : ViewModel() {
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
            delay(500L)
        }
    }
}