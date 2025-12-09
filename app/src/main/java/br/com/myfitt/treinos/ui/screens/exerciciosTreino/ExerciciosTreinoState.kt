package br.com.myfitt.treinos.ui.screens.exerciciosTreino

import androidx.compose.runtime.Immutable
import br.com.myfitt.common.domain.Exercicio
import br.com.myfitt.common.domain.ExercicioTreino

@Immutable
data class ExerciciosTreinoState(
    val mensagemDuracao: String = "Não iniciado",
    val exercicioEmAndamento: ExercicioTreino? = null,
    val erro: String? = null,
    val carregando: Boolean = true,
    val mostrarTreinoFinalizado: Boolean = false,
    val exercicios: List<ExercicioTreino> = emptyList(),
)