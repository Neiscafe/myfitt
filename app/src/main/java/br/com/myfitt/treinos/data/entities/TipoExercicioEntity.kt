package br.com.myfitt.treinos.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tipos_exercicio")
data class TipoExercicioEntity(
    @PrimaryKey(autoGenerate = true)
    val tipoExercicioId: Int = 0,
    val descricao: String
)