package br.com.myfitt.treinos.data.entities
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "treinos")
data class TreinoEntity(
    @PrimaryKey(autoGenerate = true)
    val treinoId: Int = 0,
    val dhCriado: LocalDateTime = LocalDateTime.now(),
    val finalizado: Boolean = false,
)