package br.com.myfitt.domain.models

import br.com.myfitt.data.entity.ExercicioComTipo

data class Ficha(
    val id: Int = 0, val divisaoId: Int = 0, val nome: String = "", val exercicios: List<ExercicioComTipo> = emptyList()
)