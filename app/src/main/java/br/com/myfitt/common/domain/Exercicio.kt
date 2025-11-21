package br.com.myfitt.common.domain

data class Exercicio(
    val exercicioId: Int,
    val nome: String,
    val observacao: String?,
    val compostoId: Int?
)