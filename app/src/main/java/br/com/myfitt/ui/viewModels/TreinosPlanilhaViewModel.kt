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

class TreinosPlanilhaViewModel(
    private val treinoRepository: TreinoRepository,
    private val divisaoRepository: DivisaoRepository,
    private val fichaRepository: FichaRepository,
) : ViewModel() {
    // Função para obter treinos da planilha
    fun getTreinosByPlanilha(planilhaId: Int) = treinoRepository.getTreinosByPlanilha(planilhaId)

    // Função para remover treino
    fun deleteTreino(treino: Treino) {
        viewModelScope.launch {
            treinoRepository.deleteTreino(treino)
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