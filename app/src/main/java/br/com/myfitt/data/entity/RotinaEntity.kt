package br.com.myfitt.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("rotina")
data class RotinaEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nome: String
)