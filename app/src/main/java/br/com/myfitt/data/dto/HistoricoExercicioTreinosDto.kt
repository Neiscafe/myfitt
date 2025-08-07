package br.com.myfitt.data.dto

data class HistoricoExercicioTreinosDto(
    val exercicioTreinoId: Int,
    val dataTreino: String,
    val serieId: Int,
    val segundosDescanso: Int,
    val pesoKg: Float,
    val repeticoes: Int
)