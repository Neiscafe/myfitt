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
data class TreinoExercicioCrossRef(
    val treinoId: Int,
    val exercicioId: Int,
    val series: Int,
    val posicao: Int,
    val pesoKg: Float,
    val repeticoes: Int,
    val observacao: String?
)