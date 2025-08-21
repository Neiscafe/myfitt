package br.com.myfitt.domain.models

import androidx.compose.runtime.Immutable

@Immutable
data class ExercicioTreino(
    val id: Int,
    val treinoId: Int,
    val exercicio: Exercicio,
    val posicao: Int = 0,
    val observacao: String? = null,
    val seriesLista: List<Serie>
){
}