package br.com.myfitt.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.Index

@Entity(
    tableName = "treino_exercicio",
    primaryKeys = ["treinoId", "exercicioId"],
    foreignKeys = [
        ForeignKey(
            entity = TreinoEntity::class,
            parentColumns = ["id"],
            childColumns = ["treinoId"],
            onUpdate = CASCADE,
            onDelete = CASCADE
        ),
        ForeignKey(
            entity = ExercicioEntity::class,
            parentColumns = ["id"],
            childColumns = ["exercicioId"],
            onUpdate = CASCADE,
            onDelete = CASCADE
        )
    ],
    indices = [Index("treinoId"), Index("exercicioId")]
)
data class TreinoExercicioEntity(
    val treinoId: Int,
    val exercicioId: Int,
    val series: Int = 0,
    val posicao: Int,
    val pesoKg: Float = 0f,
    val repeticoes: Int = 0,
    val observacao: String? = null
)