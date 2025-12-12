package br.com.myfitt.treinos.ui.screens.editarSerie

data class EditarSerieState(
    val carregando: Boolean = true,
    val erro: String? = null,
    val salvarHabilitado: Boolean = false
)