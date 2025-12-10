package br.com.myfitt.treinos.ui.screens.detalhesExercicio

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.myfitt.common.domain.Exercicio
import br.com.myfitt.treinos.domain.repository.ExercicioRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetalhesExercicioViewModel(
    val exercicioId: Int, val exercicioRepository: ExercicioRepository
) : ViewModel() {

    companion object {
        private var salvarCallback: (Exercicio) -> Unit = {}
        fun onSalvar(block: (Exercicio) -> Unit) {
            salvarCallback = block
        }
    }

    private val _state = MutableStateFlow(DetalhesExercicioState())
    val state = _state.asStateFlow()
    private val _observacao = MutableStateFlow("")
    val observacao = _observacao.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            _state.update { it.copy(carregando = true) }
            val result = exercicioRepository.busca(exercicioId)
            _state.update {
                it.copy(
                    erro = result.erroOrNull, exercicio = result.dataOrNull, carregando = false
                )
            }
        }
    }

    fun salvar() {
        viewModelScope.launch(Dispatchers.IO) {
            _state.update { it.copy(carregando = true) }
            val result =
                exercicioRepository.altera(state.value.exercicio!!.copy(observacao = observacao.value))
            _state.update { it.copy(erro = result.erroOrNull, carregando = false) }
            result.dataOrNull?.let {
                withContext(Dispatchers.Main) {
                    salvarCallback(it)
                    salvarCallback = {}
                }
                _state.update { it.copy(retornar = true) }
            }
        }
    }

    fun resetaEventos() {
        _state.update { it.copy(erro = null) }
    }

    fun observacaoMudou(observacao: String) {
        _observacao.update { observacao.trim() }
    }
}