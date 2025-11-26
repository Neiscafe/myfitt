package br.com.myfitt.treinos.ui.screens.menuPrincipal

sealed interface MenuPrincipalEvents {
    data class NavegaTreino(val treinoId: Int) : MenuPrincipalEvents
    data object NavegaListaTreinos : MenuPrincipalEvents
}