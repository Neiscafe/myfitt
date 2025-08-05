package br.com.myfitt.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.com.myfitt.data.entity.TreinoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TreinoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(treino: TreinoEntity): Long

    @Query("SELECT * FROM treinos ORDER BY data DESC")
    fun getTreinos(): Flow<List<TreinoEntity>>

    @Delete
    suspend fun delete(treino: TreinoEntity)
}