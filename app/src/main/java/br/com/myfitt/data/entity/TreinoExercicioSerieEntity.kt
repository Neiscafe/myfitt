package br.com.myfitt.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    "treino_exercicio_serie", [Index("treinoExercicioId")], foreignKeys = [ForeignKey(
        TreinoExercicioEntity::class, ["id"], ["treinoExercicioId"]
    )]
)
data class TreinoExercicioSerieEntity(
    @PrimaryKey(true)
    val id: Int = 0,
    val treinoExercicioId: Int,
    val pesoKg: Float? = null,
    val reps: Int? = null,
    val segundosDescanso: Int? = null // em segundos
)