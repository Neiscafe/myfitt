package br.com.myfitt.common.domain

data class Exercicio(
    val exercicioId: Int,
    val nome: String,
    val compostoId: Int? = null,
    val observacao: String? = null,
)