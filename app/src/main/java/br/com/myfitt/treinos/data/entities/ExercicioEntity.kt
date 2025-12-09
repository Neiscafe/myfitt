package br.com.myfitt.treinos.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exercicios")
data class ExercicioEntity(
    @PrimaryKey(autoGenerate = true)
    val exercicioId: Int = 0,
    val nome: String,
    val observacao: String? = null,
)