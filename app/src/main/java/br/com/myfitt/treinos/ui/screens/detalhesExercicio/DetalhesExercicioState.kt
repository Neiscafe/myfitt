package br.com.myfitt.treinos.ui.screens.detalhesExercicio

import br.com.myfitt.common.domain.Exercicio

data class DetalhesExercicioState(
    val exercicio: Exercicio? = null,
    val carregando: Boolean = true,
    val erro: String? = null,
    val retornar: Boolean = false,
)