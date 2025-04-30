package br.com.myfitt.domain.models

data class Ficha(
    val id: Int = 0, val divisaoId: Int = 0, val nome: String = "", val exercicios: List<Exercicio> = emptyList()
)