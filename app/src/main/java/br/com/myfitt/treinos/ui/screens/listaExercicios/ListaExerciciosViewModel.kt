package br.com.myfitt.treinos.ui.screens.listaExercicios

import android.util.Log
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
    val state = _state.asStateFlow()
    private var pesquisa: String = ""
    private var pesquisaJob: Job? = null

    companion object {
        private var _callback: ((Exercicio) -> Unit)? = null
        fun setCallback(callback: (Exercicio) -> Unit) {
            this._callback = callback
        }
    }

    fun getCallback(): (Exercicio) -> Unit {
        return (_callback ?: {})
    }

    override fun onCleared() {
        _callback = null
    }

    fun resetaEventos() {
        _state.update { it.resetaEventos() }
    }

    fun pesquisaMudou(pesquisa: String) {
        this.pesquisa = pesquisa.trim()
        if (this.pesquisa.length < 3) return
        pesquisaJob?.cancel()
        pesquisaJob = viewModelScope.launch(Dispatchers.IO) {
            _state.update { it.copy(carregando = true) }
            val result = exercicioRepository.lista(this@ListaExerciciosViewModel.pesquisa)
            Log.d("TESTE", "${result.dataOrNull}")
            _state.update {
                it.copy(
                    carregando = false,
                    erro = result.erroOrNull,
                    exerciciosExibidos = result.dataOrNull ?: it.exerciciosExibidos
                )
            }
        }
    }
}