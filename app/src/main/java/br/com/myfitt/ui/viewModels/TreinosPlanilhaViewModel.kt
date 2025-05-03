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
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
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

    fun fichas(divisaoId: Int) = fichaRepository.getTodasByDivisaoFlow(divisaoId)
        .map { it.toMutableList<Ficha?>().also { it.add(0, null) }.toList() }.stateIn(
            viewModelScope, SharingStarted.Eagerly, listOf(null)
        )

    val divisoes = divisaoRepository.getTodasFlow()
        .map { it.toMutableList<Divisao?>().also { it.add(0, null) }.toList() }.stateIn(
            viewModelScope, SharingStarted.Eagerly, listOf(null)
        )

    // Função para adicionar treino
    suspend fun insertTreino(treino: Treino): Int {
        return treinoRepository.insertTreino(treino)
    }
}