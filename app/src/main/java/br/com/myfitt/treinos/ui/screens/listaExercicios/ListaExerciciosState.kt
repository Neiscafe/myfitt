package br.com.myfitt.treinos.ui.screens.listaExercicios

import br.com.myfitt.common.domain.Exercicio

data class ListaExerciciosState(
    val carregando: Boolean = false,
    val erro: String? = null,
    val exerciciosExibidos: List<Exercicio> = emptyList(),
) {
    fun resetaEventos(): ListaExerciciosState = this.copy(erro = null)
}