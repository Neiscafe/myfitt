package br.com.myfitt.domain.models

data class Serie(
    val id: Int,
    val exercicioTreinoId: Int,
    val pesoKg: Float,
    val reps: Int,
    val segundosDescanso: Int
)