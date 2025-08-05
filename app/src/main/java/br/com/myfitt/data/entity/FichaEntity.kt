package br.com.myfitt.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.util.TableInfo

@Entity(
    tableName = "ficha",
)
data class FichaEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0, val nome: String
)