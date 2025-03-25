package br.com.myfitt.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import br.com.myfitt.data.entity.ExercicioEntity
import br.com.myfitt.domain.models.Exercicio
import kotlinx.coroutines.flow.Flow

@Dao
interface ExercicioDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(exercicio: ExercicioEntity): Long

    @Query("SELECT DISTINCT * FROM exercicios WHERE nome LIKE '%' || :query || '%' AND habilitado = true ORDER BY nome ASC LIMIT 6")
    fun getSugeridosExercicios(query: String): Flow<List<ExercicioEntity>>

    @Update
    suspend fun update(exercicio: ExercicioEntity)

    @Query("""
        UPDATE 
            exercicios 
        SET 
            habilitado = false AND dataDesabilitado = DATE('now') 
        WHERE id = :exercicioId
    """)
    suspend fun delete(exercicioId: Int)

    @Query("SELECT * FROM exercicios WHERE lower(trim(nome)) = lower(trim(:nome)) AND habilitado = true LIMIT 1")
    suspend fun getExercicioPorNome(nome: String): Exercicio?
}