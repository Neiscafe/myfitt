package br.com.myfitt.domain.models

data class TreinoExercicio(
    val treinoId: Int,
    val exercicioId: Int,
    val series: Int,
    val pesoKg: Float,
    val posicao: Int,
    val repeticoes: Int,
    val observacao: String?
)