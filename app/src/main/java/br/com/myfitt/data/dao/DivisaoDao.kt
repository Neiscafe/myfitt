package br.com.myfitt.data.dao

import androidx.room.Dao
import androidx.room.Query
import br.com.myfitt.data.entity.DivisaoEntity

@Dao
interface DivisaoDao {
    @Query("""
            SELECT * FROM divisao 
    """)
    suspend fun getTodas(): List<DivisaoEntity>


}