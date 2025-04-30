package br.com.myfitt.data.entity

import androidx.room.Embedded
import androidx.room.Relation

data class ExercicioComTipo(
    @Embedded val exercicio: ExercicioEntity,
    @Relation(parentColumn = "exercicioTipoId", entityColumn = "id")
    val exercicioTipo: ExercicioTipoEntity
)