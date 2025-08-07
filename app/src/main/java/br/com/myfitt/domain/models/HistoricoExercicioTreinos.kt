package br.com.myfitt.domain.models

data class HistoricoExercicioTreinos(
    val exercicioTreinoId: Int,
    val dataTreino: String,
    val serieId: Int,
    val segundosDescanso: Int,
    val pesoKg: Float,
    val repeticoes: Int
)