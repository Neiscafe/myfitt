package br.com.myfitt.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.myfitt.data.repository.TreinoRepository
import br.com.myfitt.domain.models.Treino
import kotlinx.coroutines.launch

class TreinoViewModel(private val treinoRepository: TreinoRepository) : ViewModel() {

    // Função para obter treinos da planilha
    fun getTreinosByPlanilha(planilhaId: Int) = treinoRepository.getTreinosByPlanilha(planilhaId)

    // Função para adicionar treino
    fun insertTreino(treino: Treino) {
        viewModelScope.launch {
            treinoRepository.insertTreino(treino)
        }
    }

    // Função para remover treino
    fun deleteTreino(treino: Treino) {
        viewModelScope.launch {
            treinoRepository.deleteTreino(treino)
        }
    }
}