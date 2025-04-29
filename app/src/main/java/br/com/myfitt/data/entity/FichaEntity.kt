package br.com.myfitt.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.util.TableInfo

@Entity(
    tableName = "ficha", foreignKeys = [ForeignKey(
        entity = DivisaoEntity::class, parentColumns = ["id"], childColumns = ["divisaoId"]
    )], indices = [Index("id"), Index("divisaoId")]
)
data class FichaEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0, val divisaoId: Int, val nome: String
)