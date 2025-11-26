package br.com.myfitt.treinos.ui.screens.menuPrincipal

import br.com.myfitt.common.domain.Treino

data class MenuPrincipalState(val carregando: Boolean = false, val erro: String? = null) {
    fun resetaEventos() = this.copy(erro = null)
}