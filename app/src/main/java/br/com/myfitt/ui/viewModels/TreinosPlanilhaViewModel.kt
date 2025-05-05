package br.com.myfitt.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import br.com.myfitt.data.repository.DivisaoRepository
import br.com.myfitt.data.repository.FichaRepository
import br.com.myfitt.data.repository.TreinoRepository
import br.com.myfitt.domain.models.Divisao
import br.com.myfitt.domain.models.Ficha
import br.com.myfitt.domain.models.Treino
import br.com.myfitt.domain.utils.DateUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate

class TreinosPlanilhaViewModel(
    private val planilhaId: Int,
    private val treinoRepository: TreinoRepository,
    private val divisaoRepository: DivisaoRepository,
    private val fichaRepository: FichaRepository,
) : ViewModel() {
    // Função para obter treinos da planilha
    fun getTreinosByPlanilha() = treinoRepository.getTreinosByPlanilha(planilhaId)

    // Função para remover treino
    fun deleteTreino(treino: Treino) {
        viewModelScope.launch {
            treinoRepository.deleteTreino(treino)
        }
    }

    private val _fichas = MutableStateFlow<List<Ficha>>(emptyList())
    val fichas = _fichas.asStateFlow()
    fun setDivisaoSelected(divisaoId: Int?) = viewModelScope.launch(Dispatchers.IO) {
        _fichas.value =
            if (divisaoId == null) emptyList() else fichaRepository.getFichasByDivisaoId(divisaoId)
    }

    val divisoes = divisaoRepository.getTodasFlow().map { it.toList<Divisao>() }.stateIn(
        viewModelScope, SharingStarted.Eagerly, emptyList()
    )

    // Função para adicionar treino
    suspend fun insertTreino(nomeTreino: String, dataTreino: LocalDate): Int {
        val novoTreino = Treino(
            planilhaId = planilhaId, nome = nomeTreino, data = DateUtil.toDbNotation(dataTreino)
        )
        return treinoRepository.insertTreino(novoTreino)
    }

    fun setFichaSelected(fichaId: kotlin.Int?) = viewModelScope.launch(Dispatchers.IO) {

    }
}