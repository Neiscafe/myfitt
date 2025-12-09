package br.com.myfitt.treinos.ui.screens.listaTreinos

import br.com.myfitt.common.domain.Treino

data class ListaTreinoState(
    val treinos: List<Treino> = emptyList(),
    val paginas: Int = 0,
    val erro: String? = null,
    val carregando: Boolean = false,
)
