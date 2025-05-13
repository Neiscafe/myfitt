package br.com.myfitt.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import br.com.myfitt.data.dto.ExercicioComTipoDto
import br.com.myfitt.data.entity.ExercicioEntity
import br.com.myfitt.data.entity.ExercicioTipoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ExercicioDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(exercicio: ExercicioEntity): Long

    @Query("""SELECT DISTINCT ex.id, ex.nome, ex.habilitado, ex.dataDesabilitado, et.id as exercicioTipoId, et.nome as  exercicioTipoNome FROM exercicios ex LEFT JOIN exercicio_tipo et ON et.id = ex.exercicioTipoId WHERE ex.nome LIKE '%' || :query || '%' AND ex.habilitado = 1 ORDER BY ex.nome ASC LIMIT 6""")
    suspend fun getSugeridosExercicios(query: String): List<ExercicioComTipoDto>

    @Update
    suspend fun update(exercicio: ExercicioEntity)

    @Query(
        """
        UPDATE 
            exercicios 
        SET 
            habilitado = 0 AND dataDesabilitado = DATE('now') 
        WHERE id = :exercicioId
    """
    )
    suspend fun delete(exercicioId: Int)

    @Query("""SELECT ex.id, ex.nome, ex.habilitado, ex.dataDesabilitado, et.id as exercicioTipoId, et.nome as exercicioTipoNome FROM exercicios  ex LEFT JOIN exercicio_tipo et ON et.id = ex.exercicioTipoId WHERE lower(trim(ex.nome)) = lower(trim(:nome)) AND ex.habilitado = 1 LIMIT 1""")
    suspend fun getExercicioPorNome(nome: String): ExercicioComTipoDto?

    @Query(
        """
                SELECT 
                    e.*, et.id as exercicioTipoId, et.nome as exercicioTipoNome    
                FROM
                    exercicios e
                JOIN
                    exercicio_tipo et
                ON et.id = e.exercicioTipoId
                WHERE 
                    e.id = :exercicioId
        """
    )
    suspend fun getExercicio(exercicioId: kotlin.Int): ExercicioComTipoDto?
    @Query(
        """
                SELECT 
                    e.*, et.id as exercicioTipoId, et.nome as exercicioTipoNome    
                FROM
                    exercicios e
                JOIN
                    exercicio_tipo et
                ON et.id = e.exercicioTipoId
                WHERE 
                    lower(e.nome) = lower(:nome)
        """
    )
    suspend fun getExercicio(nome: String): ExercicioComTipoDto?
    @Query("""
        SELECT * FROM exercicio_tipo ORDER BY nome ASC
    """)
    fun getExercicioTiposFlow(): Flow<List<ExercicioTipoEntity>>
}