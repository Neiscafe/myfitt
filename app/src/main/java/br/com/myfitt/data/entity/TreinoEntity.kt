package br.com.myfitt.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(
    tableName = "treinos",
)
data class TreinoEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val data: String,  // Formato: "yyyy-MM-dd"
    val nome: String
)