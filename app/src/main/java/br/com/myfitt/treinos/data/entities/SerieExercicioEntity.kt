package br.com.myfitt.treinos.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(
    tableName = "series_exercicio",
    foreignKeys = [
        ForeignKey(
            entity = ExercicioTreinoEntity::class, // Atualizado
            parentColumns = ["exercicioTreinoId"],
            childColumns = ["exercicioTreinoId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("exercicioTreinoId")]
)
data class SerieExercicioEntity(
    @PrimaryKey(autoGenerate = true)
    val serieId: Int = 0,
    val exercicioTreinoId: Int,
    val exercicioId: Int,
    val treinoId: Int,
    val dhInicioExecucao: LocalDateTime?,
    val dhFimExecucao: LocalDateTime?,
    val dhInicioDescanso: LocalDateTime?,
    val dhFimDescanso: LocalDateTime?,
    val duracaoSegundos: Int,
    val segundosDescanso: Int,
    val pesoKg: Float,
    val repeticoes: Int,
    val finalizado: Boolean
)