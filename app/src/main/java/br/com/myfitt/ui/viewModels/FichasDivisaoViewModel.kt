package br.com.myfitt.ui.viewModels

import androidx.lifecycle.ViewModel
import br.com.myfitt.data.repository.FichaRepository
import br.com.myfitt.domain.models.Ficha

class FichasDivisaoViewModel(
    private val fichaRepository: FichaRepository
) : ViewModel() {
    val fichas = fichaRepository.getFichasByDivisaoIdFlow()
    suspend fun insertFicha(nomeFicha: String) =
        fichaRepository.insert(Ficha(nome = nomeFicha))
}