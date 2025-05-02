package br.com.myfitt.ui.viewModels

import androidx.lifecycle.ViewModel
import br.com.myfitt.data.repository.DivisaoRepository
import br.com.myfitt.domain.models.Divisao

class ListaDivisaoViewModel(private val divisaoRepository: DivisaoRepository) : ViewModel() {
    val divisoes = divisaoRepository.getTodasFlow()
    suspend fun insert(divisao: Divisao) =
        divisaoRepository.inserir(divisao)

}