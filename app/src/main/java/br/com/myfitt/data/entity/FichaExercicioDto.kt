package br.com.myfitt.data.entity

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class FichaExercicioDto(
    val fichaId: Int,
    val divisaoId: Int,
    val exercicioId: Int,
    val position: Int,
    val fichaNome: String,
    val exercicioNome: String,
    val exercicioTipoId: Int,
    val exercicioTipoNome: String,
)