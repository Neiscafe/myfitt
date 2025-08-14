package br.com.myfitt.data.entity

import androidx.room.Embedded
import androidx.room.Relation

data class TreinoExercicioAndSeries(
    @Embedded val treinoExercicio: TreinoExercicioEntity,
    @Relation(parentColumn = "id", entityColumn = "treinoExercicioId") val series: List<TreinoExercicioSerieEntity>
)