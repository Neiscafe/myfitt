package br.com.myfitt.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
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
    indices = [Index("exercicioId"), Index("fichaId")]
)
data class FichaExercicioEntity(
    val fichaId: Int,
    val exercicioId: Int,
    @ColumnInfo(defaultValue = "0") val position: Int = 0,
)