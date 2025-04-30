package br.com.myfitt.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.Index

@Entity(
    tableName = "ficha_exercicio",
    primaryKeys = ["fichaId", "exercicioId"],
    foreignKeys = [ForeignKey(
        entity = FichaEntity::class,
        parentColumns = ["id"],
        childColumns = ["fichaId"],
    ), ForeignKey(
        entity = ExercicioEntity::class,
        parentColumns = ["id"],
        childColumns = ["exercicioId"],
    )],
    indices = [Index("fichaId"), Index("exercicioId")]
)
data class FichaExercicioCrossRef(
    val fichaId: Int,
    val exercicioId: Int,
)