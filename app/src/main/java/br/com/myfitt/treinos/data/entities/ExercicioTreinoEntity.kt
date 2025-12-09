package br.com.myfitt.treinos.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "exercicios_treino",
    foreignKeys = [
        ForeignKey(
            entity = TreinoEntity::class, // Atualizado
            parentColumns = ["treinoId"],
            childColumns = ["treinoId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ExercicioEntity::class, // Atualizado
            parentColumns = ["exercicioId"],
            childColumns = ["exercicioId"],
            onDelete = ForeignKey.RESTRICT
        )
    ],
    indices = [Index("treinoId"), Index("exercicioId")]
)
data class ExercicioTreinoEntity(
    @PrimaryKey(autoGenerate = true)
    val exercicioTreinoId: Int = 0,
    val exercicioId: Int,
    val treinoId: Int,
    val ordem: Int = 0,
    val compostoId: Int? = null,
    val nomeExercicio: String,
)