package br.com.myfitt.treinos.ui.screens.exerciciosTreino

import androidx.compose.runtime.Immutable
import br.com.myfitt.common.domain.ExercicioTreino

@Immutable
data class ExerciciosTreinoState(
    val mensagemDuracao: String = "Não iniciado",
    val erro: String? = null,
    val carregando: Boolean = true,
    val exercicios: List<ExercicioTreino> = emptyList(),
    val irParaSubstituicao: ExercicioTreino? = null,
    val irParaSeries: ExercicioTreino? = null,
)