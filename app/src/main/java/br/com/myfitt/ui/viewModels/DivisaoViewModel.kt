package br.com.myfitt.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.myfitt.data.repository.DivisaoRepository
import br.com.myfitt.domain.models.Divisao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DivisaoViewModel(private val divisaoRepository: DivisaoRepository) : ViewModel() {
    val divisoes = divisaoRepository.getTodasFlow()
    suspend fun insert(divisao: Divisao) =
        divisaoRepository.inserir(divisao)

}