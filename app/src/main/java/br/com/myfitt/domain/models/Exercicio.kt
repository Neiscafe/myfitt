package br.com.myfitt.domain.models

data class Exercicio(
    val nome: String = "",
    val id: Int = 0,
    val posicao: Int = 0,
    val tipo: TipoExercicio? = null,
    val habilitado: Boolean = true,
    val dataDesabilitado: String? = null,
    val fichaId: Int = 0,
)