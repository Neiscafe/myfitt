package br.com.myfitt.treinos.ui.screens.menuPrincipal

sealed class Acoes {
    data object NovoTreino: Acoes()
    data object ResetaEventos: Acoes()
    data object ListaTreinos: Acoes()
}