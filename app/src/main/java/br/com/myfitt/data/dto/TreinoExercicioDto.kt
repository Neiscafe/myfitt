package br.com.myfitt.data.dto

data class TreinoExercicioDto(
    val id: Int,
    val treinoId: Int,
    val exercicioId: Int,
    val exercicioNome: String,
    val data: String,
    val series: Int,
    val posicao: Int,
    val serieId: Int,
    val segundosDescanso: Int?,
    val pesoKg: Float?,
    val repeticoes: Int?,
    val observacao: String?,
    val seriesUltimoTreino: Int = 0,
    val repeticoesUltimoTreino: Int = 0,
    val pesoKgUltimoTreino: Float = 0f
)