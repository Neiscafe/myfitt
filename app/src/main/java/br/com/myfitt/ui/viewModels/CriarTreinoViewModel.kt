package br.com.myfitt.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.myfitt.data.repository.DivisaoRepository
import br.com.myfitt.data.repository.FichaRepository
import br.com.myfitt.data.repository.TreinoRepository
import br.com.myfitt.domain.models.Divisao
import br.com.myfitt.domain.models.Ficha
import br.com.myfitt.domain.models.Treino
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CriarTreinoViewModel(
    private val fichaRepository: FichaRepository,
    private val divisaoRepository: DivisaoRepository,
    private val treinoRepository: TreinoRepository
) : ViewModel() {
    init {
        viewModelScope.launch(Dispatchers.IO) {
            getDivisoes()
        }
    }

    private val _fichas = MutableStateFlow<List<Ficha?>>(listOf(null))
    val fichas = _fichas.asStateFlow()
    fun getFichas(divisaoId: Int) = viewModelScope.launch(Dispatchers.IO) {
        _fichas.value = mutableListOf<Ficha?>().apply {
            add(null)
            addAll(
                fichaRepository.getTodasByDivisao(divisaoId)
            )
        }
    }

    private val _divisoes = MutableStateFlow<List<Divisao?>>(listOf(null))
    val divisoes = _divisoes.asStateFlow()
    fun getDivisoes() = viewModelScope.launch(Dispatchers.IO) {
        _divisoes.value = mutableListOf<Divisao?>().apply {
            add(null)
            addAll(divisaoRepository.getTodas())
        }
    }

    // Função para adicionar treino
    suspend fun insertTreino(treino: Treino): Int {
        return treinoRepository.insertTreino(treino)
    }
}