package br.com.myfitt.treinos.ui.screens.listaTreinos

data class ListaTreinoState(
    val treinos: List<ListaTreinoModel> = emptyList(),
    val pagina: Int = 0,
    val erro: String? = null,
    val carregando: Boolean = false,
    val ultimaPagina: Boolean = false,
)
