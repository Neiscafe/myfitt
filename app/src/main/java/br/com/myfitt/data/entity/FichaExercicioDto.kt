package br.com.myfitt.data.entity

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class FichaExercicioDto(
    @Embedded val ficha: FichaEntity,
    @Relation(
        parentColumn = "fichaId", entityColumn = "exercicioId", associateBy = Junction(
            FichaExercicioCrossRef::class
        )
    ) val exercicios: List<ExercicioEntity>
)