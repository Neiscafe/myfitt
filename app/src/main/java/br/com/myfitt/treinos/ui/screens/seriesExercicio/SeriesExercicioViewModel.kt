package br.com.myfitt.treinos.ui.screens.seriesExercicio

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.myfitt.treinos.domain.repository.SeriesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SeriesExercicioViewModel(exercicioTreinoId: Int, seriesRepository: SeriesRepository) :
    ViewModel() {
    private val _state = MutableStateFlow(SeriesExercicioState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            _state.update { it.copy(carregando = true) }
            val result = seriesRepository.lista(exercicioTreinoId)
            _state.update {
                it.copy(
                    series = result.dataOrNull ?: it.series,
                    erro = result.erroOrNull,
                    carregando = false
                )
            }
        }
    }
}