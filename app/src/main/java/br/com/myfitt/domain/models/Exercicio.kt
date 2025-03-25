package br.com.myfitt.domain.models

data class Exercicio(
    val nome: String,
    val id: Int = 0,
    val habilitado: Boolean = true,
    val dataDesabilitado: String? = null
)