package br.com.myfitt.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import br.com.myfitt.data.entity.FichaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FichaDao {
    @Query("""SELECT * FROM ficha""")
    fun getTodasFichasFlow(): Flow<List<FichaEntity>>

    @Query("""SELECT * FROM ficha""")
    suspend fun getFichasByDivisao(): List<FichaEntity>
    @Query("""SELECT * FROM ficha """)
    fun getFichasByDivisaoFlow(): Flow<List<FichaEntity>>

    @Insert
    suspend fun insert(entity: FichaEntity): Long

    @Transaction
    suspend fun delete(exercicioId: kotlin.Int, fichaId: kotlin.Int, position: Int){
        executeDelete(exercicioId, fichaId)
        updatePositions(position, fichaId)
    }

    @Query("""DELETE FROM ficha_exercicio WHERE fichaId = :fichaId AND exercicioId = :exercicioId""")
    suspend fun executeDelete(exercicioId: kotlin.Int, fichaId: kotlin.Int)

    @Query("""UPDATE ficha_exercicio SET position = position-1 WHERE fichaId = :fichaId AND position >:position""")
    suspend fun updatePositions(position: Int, fichaId: kotlin.Int)
}