package br.com.myfitt.ui.viewModels

import androidx.lifecycle.ViewModel
import br.com.myfitt.data.repository.FichaRepository

class DetalhesFichaViewModel(private val fichaRepository: FichaRepository): ViewModel() {
    val ficha = fichaRepository.getFichaByIdFlow()
}