package br.com.myfitt.data.dao

import androidx.compose.ui.input.pointer.PointerId
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import br.com.myfitt.data.entity.FichaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FichaDao {
    @Query("""SELECT * FROM ficha WHERE divisaoId = :divisaoId""")
    suspend fun getTodasByDivisao(divisaoId: Int): List<FichaEntity>

    @Query("""SELECT * FROM ficha WHERE divisaoId = :divisaoId""")
    fun getTodasByDivisaoFlow(divisaoId: Int): Flow<List<FichaEntity>>

    @Insert
    suspend fun insert(entity: FichaEntity): Long
}