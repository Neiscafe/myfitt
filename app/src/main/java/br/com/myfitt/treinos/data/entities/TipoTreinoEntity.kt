package br.com.myfitt.treinos.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tipos_treino")
data class TipoTreinoEntity(
    @PrimaryKey(autoGenerate = true)
    val tipoTreinoId: Int = 0,
    val descricao: String,
)