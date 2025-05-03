package br.com.myfitt.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.myfitt.data.repository.PlanilhaRepository
import br.com.myfitt.domain.models.Planilha
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


class ListaPlanilhasViewModel(private val planilhaRepository: PlanilhaRepository) : ViewModel() {

    // Fluxo de Planilhas
    val planilhas = planilhaRepository.getAllPlanilhas().stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    // Função para adicionar uma planilha
    fun insertPlanilha(planilha: Planilha) {
        viewModelScope.launch {
            planilhaRepository.insertPlanilha(planilha)
        }
    }

    // Função para remover uma planilha
    fun deletePlanilha(planilha: Planilha) {
        viewModelScope.launch {
            planilhaRepository.deletePlanilha(planilha)
        }
    }
}