package br.com.myfitt.domain.models

data class ExercicioTreino(
    val id: Int,
    val treinoId: Int,
    val exercicio: Exercicio,
    val posicao: Int = 0,
    val observacao: String? = null,
    val seriesLista: List<Serie>
){
}