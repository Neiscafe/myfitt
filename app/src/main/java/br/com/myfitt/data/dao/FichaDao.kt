package br.com.myfitt.data.dao

import androidx.room.Dao
import androidx.room.Query
import br.com.myfitt.data.entity.FichaEntity

@Dao
interface FichaDao {
    @Query("""SELECT * FROM ficha WHERE divisaoId = :divisaoId""")
    suspend fun getTodasByDivisao(divisaoId: Int): List<FichaEntity>
}