package br.com.myfitt.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import br.com.myfitt.data.entity.ExercicioComTipoDto
import br.com.myfitt.data.entity.ExercicioEntity
import br.com.myfitt.domain.models.Exercicio
import kotlinx.coroutines.flow.Flow

@Dao
interface ExercicioDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(exercicio: ExercicioEntity): Long

    @Query("""SELECT DISTINCT ex.id, ex.nome, ex.habilitado, ex.dataDesabilitado, et.id as exercicioTipoId, et.nome as  exercicioTipoNome FROM exercicios ex LEFT JOIN exercicio_tipo et ON et.id = ex.exercicioTipoId WHERE ex.nome LIKE '%' || :query || '%' AND ex.habilitado = true ORDER BY ex.nome ASC LIMIT 6""")
    fun getSugeridosExercicios(query: String): Flow<List<ExercicioComTipoDto>>

    @Update
    suspend fun update(exercicio: ExercicioEntity)

    @Query(
        """
        UPDATE 
            exercicios 
        SET 
            habilitado = false AND dataDesabilitado = DATE('now') 
        WHERE id = :exercicioId
    """
    )
    suspend fun delete(exercicioId: Int)

    @Query("""SELECT ex.id, ex.nome, ex.habilitado, ex.dataDesabilitado, et.id as exercicioTipoId, et.nome as exercicioTipoNome FROM exercicios  ex LEFT JOIN exercicio_tipo et ON et.id = ex.exercicioTipoId WHERE lower(trim(ex.nome)) = lower(trim(:nome)) AND ex.habilitado = true LIMIT 1""")
    suspend fun getExercicioPorNome(nome: String): ExercicioComTipoDto?
}