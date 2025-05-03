package br.com.myfitt.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import br.com.myfitt.data.entity.DivisaoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DivisaoDao {
    @Query(
        """
            SELECT * FROM divisao 
    """
    )
    suspend fun getTodas(): List<DivisaoEntity>

    @Query(
        """
            SELECT * FROM divisao 
    """
    )
    fun getTodasFlow(): Flow<List<DivisaoEntity>>

    @Insert
    suspend fun insert(divisao: DivisaoEntity): Long

    @Delete
    suspend fun delete(divisao: DivisaoEntity)


}