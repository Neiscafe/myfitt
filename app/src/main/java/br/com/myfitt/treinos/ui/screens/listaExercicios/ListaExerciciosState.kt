package br.com.myfitt.treinos.ui.screens.listaExercicios

import br.com.myfitt.common.domain.Exercicio

data class ListaExerciciosState(
    val carregando: Boolean = false,
    val erro: String? = null,
    val atualizarItens: List<Exercicio>? = null,
) {
    fun resetaEventos(): ListaExerciciosState = this.copy(atualizarItens = null, erro = null)
}