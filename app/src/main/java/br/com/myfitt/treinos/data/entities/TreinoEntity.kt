package br.com.myfitt.treinos.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(
    tableName = "treinos", foreignKeys = [ForeignKey(
        entity = TipoTreinoEntity::class,
        parentColumns = ["tipoTreinoId"],
        childColumns = ["tipoTreinoId"],
        onDelete = ForeignKey.RESTRICT // Não apaga o tipo se houver treinos usando
    )], indices = [Index("tipoTreinoId")]
)
data class TreinoEntity(
    @PrimaryKey(autoGenerate = true) val treinoId: Int = 0,
    val dhCriado: LocalDateTime = LocalDateTime.now(),
    val dhInicio: LocalDateTime? = null,
    val dhFim: LocalDateTime? = null,
    val tipoTreinoId: Int?
)