package br.com.myfitt.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import br.com.myfitt.data.entity.FichaEntity
import br.com.myfitt.data.entity.FichaExercicioDto
import kotlinx.coroutines.flow.Flow

@Dao
interface FichaExercicioDao {
    @Transaction
    @Query("""
        SELECT  * from ficha WHERE id = :id 
    """)
    fun getFichaExercicioByIdFlow(id: Int): Flow<FichaExercicioDto>
}