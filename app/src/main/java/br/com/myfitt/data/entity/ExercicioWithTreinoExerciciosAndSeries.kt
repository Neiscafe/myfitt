package br.com.myfitt.data.entity

import androidx.room.Embedded
import androidx.room.Relation

data class ExercicioWithTreinoExerciciosAndSeries(
    @Embedded val treinoExercicioAndSeries: TreinoExercicioAndSeries,
    @Relation(
        parentColumn = "exercicioId",
        entityColumn = "id",
    ) val exercicio: ExercicioEntity,
)