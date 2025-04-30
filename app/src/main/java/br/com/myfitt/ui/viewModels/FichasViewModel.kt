package br.com.myfitt.ui.viewModels

import androidx.lifecycle.ViewModel
import br.com.myfitt.data.repository.FichaRepository
import br.com.myfitt.domain.models.Ficha

class FichasViewModel(private val divisaoId: Int, private val fichaRepository: FichaRepository): ViewModel() {
    val fichas = fichaRepository.getTodasByDivisaoFlow(divisaoId)
    suspend fun insertFicha(ficha: Ficha) = fichaRepository.insert(ficha)
}