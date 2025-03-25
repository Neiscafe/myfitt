package br.com.myfitt.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "planilhas")
data class PlanilhaEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nome: String
)
