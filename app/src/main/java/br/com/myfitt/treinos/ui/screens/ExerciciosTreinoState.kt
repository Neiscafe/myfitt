package br.com.myfitt.treinos.ui.screens

import androidx.compose.runtime.Immutable
import br.com.myfitt.common.domain.ExercicioTreino

@Immutable
data class ExerciciosTreinoState (
    val mensagemDuracao: String = "Não iniciado",
    val exercicios: List<ExercicioTreino> = listOf(
        ExercicioTreino(1, 1, 1),
        ExercicioTreino(2, 1, 1),
        ExercicioTreino(3, 1, 1),
    )
)