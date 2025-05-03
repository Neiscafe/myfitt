package br.com.myfitt.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.myfitt.data.repository.DivisaoRepository
import br.com.myfitt.domain.models.Divisao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ListaDivisaoViewModel(private val divisaoRepository: DivisaoRepository) : ViewModel() {
    val divisoes = divisaoRepository.getTodasFlow()
    suspend fun insert(divisao: Divisao) =
        divisaoRepository.inserir(divisao)

    fun delete(divisao: Divisao) = viewModelScope.launch(Dispatchers.IO){
        divisaoRepository.deletar(divisao)
    }

}