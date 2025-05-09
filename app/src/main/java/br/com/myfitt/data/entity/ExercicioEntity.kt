package br.com.myfitt.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "exercicios", foreignKeys = [ForeignKey(
        ExercicioTipoEntity::class,
        parentColumns = ["id"],
        childColumns = ["exercicioTipoId"],
    )], indices = [Index("id"), Index("exercicioTipoId")]
)
data class ExercicioEntity(
    val nome: String,
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(defaultValue = "1") val habilitado: Boolean = true,
    @ColumnInfo(defaultValue = "NULL") val dataDesabilitado: String? = null,
    @ColumnInfo(defaultValue = "NULL") val exercicioTipoId: Int? = null,
)