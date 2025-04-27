package br.com.myfitt.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.Index

@Entity(
    tableName = "rotina_exercicio",
    primaryKeys = ["rotinaId", "exercicioId"],
    foreignKeys = [ForeignKey(
        entity = RotinaEntity::class,
        parentColumns = ["id"],
        childColumns = ["rotinaId"],
        onUpdate = CASCADE,
        onDelete = CASCADE
    ), ForeignKey(
        entity = ExercicioEntity::class,
        parentColumns = ["id"],
        childColumns = ["exercicioId"],
        onUpdate = CASCADE,
        onDelete = CASCADE
    )],
    indices = [Index("rotinaId"), Index("exercicioId")]
)
data class RotinaExercicioCrossRef(
    val rotinaId: Int,
    val exercicioId: Int,

)