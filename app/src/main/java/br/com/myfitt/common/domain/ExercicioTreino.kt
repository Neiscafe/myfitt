package br.com.myfitt.common.domain

data class ExercicioTreino(
    val exercicioTreinoId: Int,
    val exercicioId: Int,
    val treinoId: Int,
    val ordem: Int,
    val compostoId: Int? = null
)