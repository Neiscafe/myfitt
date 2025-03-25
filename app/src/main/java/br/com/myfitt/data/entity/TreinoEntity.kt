package br.com.myfitt.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(
    tableName = "treinos",
    foreignKeys = [ForeignKey(
        entity = PlanilhaEntity::class,
        parentColumns = ["id"],
        childColumns = ["planilhaId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["planilhaId"])]
)
data class TreinoEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val planilhaId: Int,
    val data: String,  // Formato: "yyyy-MM-dd"
    val nome: String
)