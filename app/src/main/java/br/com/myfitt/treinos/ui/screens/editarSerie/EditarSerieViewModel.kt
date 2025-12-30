package br.com.myfitt.treinos.ui.screens.editarSerie

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.myfitt.common.domain.SerieExercicio
import br.com.myfitt.treinos.domain.repository.SeriesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EditarSerieViewModel(val serieId: Int, val seriesRepository: SeriesRepository) : ViewModel() {

    private val _peso = MutableStateFlow<String>("")
    val peso = _peso.asStateFlow()
    val pesoFloat get() = peso.value.replace(",", ".").toFloatOrNull() ?: 0f
    private val _repeticoes = MutableStateFlow<String>("")
    val repeticoes = _repeticoes.asStateFlow()
    val repeticoesInt get() = repeticoes.value.toIntOrNull() ?: 0

    private val _state = MutableStateFlow(EditarSerieState())
    val state = _state.asStateFlow()

    private var _serie: SerieExercicio? = null
    val serie get() = _serie!!
    fun salvar() {
        viewModelScope.launch(Dispatchers.IO) {
            _state.update { it.copy(carregando = true) }
            val result = seriesRepository.altera(
                serie.copy(
                    pesoKg = pesoFloat,
                    repeticoes = repeticoesInt,
                )
            )
            _state.update { it.copy(erro = result.erroOrNull) }
            if (result.sucesso) {
                val result2 = seriesRepository.lista(serie.exercicioTreinoId)
                _state.update { it.copy(erro = result2.erroOrNull) }
                if (!result2.sucesso) {
                    return@launch
                }
                withContext(Dispatchers.Main) { callback(result2.dataOrNull!!) }
                callback = {}
            }
        }
    }

    fun pesoChanged(peso: String) {
        _peso.update { peso }
        validaInput()
    }

    private fun validaInput() {
        _state.update {
            it.copy(salvarHabilitado = pesoFloat > 0f && repeticoesInt > 0)
        }
    }

    fun repeticoesChanged(repeticoes: String) {
        _repeticoes.update { repeticoes }
        validaInput()
    }

    fun limpaEventos() {
        _state.update { it.copy(erro = null) }
    }

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val result = seriesRepository.busca(serieId)
            _state.update {
                it.copy(
                    erro = result.erroOrNull, carregando = false, salvarHabilitado = result.sucesso
                )
            }
            if (result.sucesso) {
                _serie = result.dataOrNull
            }
            _peso.update { String.format("%.1f", result.dataOrNull!!.pesoKg).replace(".", ",") }
            _repeticoes.update { result.dataOrNull!!.repeticoes.toString() }
        }
    }

    companion object {
        private var callback: (List<SerieExercicio>) -> Unit = {}
        fun setCallback(block: (List<SerieExercicio>) -> Unit) {
            callback = block
        }
    }

}