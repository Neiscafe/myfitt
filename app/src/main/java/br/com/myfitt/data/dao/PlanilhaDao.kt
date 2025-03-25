package br.com.myfitt.data.dao

import androidx.room.*
import br.com.myfitt.data.entity.PlanilhaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlanilhaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(planilha: PlanilhaEntity)

    @Query("SELECT * FROM planilhas ORDER BY nome ASC")
    fun getAllPlanilhas(): Flow<List<PlanilhaEntity>>

    @Delete
    suspend fun delete(planilha: PlanilhaEntity)
}