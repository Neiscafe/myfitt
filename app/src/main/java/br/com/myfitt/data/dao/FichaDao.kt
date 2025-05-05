package br.com.myfitt.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import br.com.myfitt.data.entity.FichaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FichaDao {
    @Query("""SELECT * FROM ficha""")
    fun getTodasFichasFlow(): Flow<List<FichaEntity>>

    @Query("""SELECT * FROM ficha WHERE divisaoId = :divisaoId""")
    suspend fun getFichasByDivisao(divisaoId: Int): List<FichaEntity>
    @Query("""SELECT * FROM ficha WHERE divisaoId = :divisaoId""")
    fun getFichasByDivisaoFlow(divisaoId: Int): Flow<List<FichaEntity>>

    @Insert
    suspend fun insert(entity: FichaEntity): Long
}