package br.com.myfitt.data.dto

data class ExercicioComTipoDto(
    val id: Int = 0,
    val nome: String,
    val habilitado: Boolean = true,
    val dataDesabilitado: String? = null,
    val exercicioTipoId: Int? = null,
    val exercicioTipoNome: String? = null,
)