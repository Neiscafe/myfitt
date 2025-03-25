package br.com.myfitt.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "exercicios",
)
data class ExercicioEntity(
    val nome: String,
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(defaultValue = "1")
    val habilitado: Boolean = true,
    @ColumnInfo(defaultValue = "NULL", name = "dataDesabilitado")
    val dataDesabilitado: String?
)