package br.com.myfitt.treinos.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "exercicios", foreignKeys = [ForeignKey(
        entity = TipoExercicioEntity::class,
        parentColumns = ["tipoExercicioId"],
        childColumns = ["tipoExercicioId"],
        onDelete = ForeignKey.RESTRICT,
    )], indices = [Index("tipoExercicioId")]
)
data class ExercicioEntity(
    @PrimaryKey(autoGenerate = true) val exercicioId: Int = 0,
    val nome: String,
    val observacao: String? = null,
    val tipoExercicioId: Int?,
)