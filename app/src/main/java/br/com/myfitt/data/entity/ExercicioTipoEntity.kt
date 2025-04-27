package br.com.myfitt.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exercicio_tipo")
data class ExercicioTipoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nome: String
)
