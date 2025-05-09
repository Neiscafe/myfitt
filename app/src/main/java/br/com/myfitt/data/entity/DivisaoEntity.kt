package br.com.myfitt.data.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity("divisao", indices = [Index("id")])
data class DivisaoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nome: String
)