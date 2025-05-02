package br.com.myfitt.data.dto

data class TreinoExercicioDto(
    val treinoId: Int,
    val exercicioId: Int,
    val exercicioNome: String,
    val data: String,
    val series: Int,
    val posicao: Int,
    val pesoKg: Float,
    val repeticoes: Int,
    val observacao: String?,
    val seriesUltimoTreino: Int = 0,
    val repeticoesUltimoTreino: Int = 0,
    val pesoKgUltimoTreino: Float = 0f
)