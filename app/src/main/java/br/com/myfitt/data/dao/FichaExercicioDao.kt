package br.com.myfitt.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import br.com.myfitt.data.dto.ExercicioComTipoDto2
import br.com.myfitt.data.entity.FichaExercicioEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FichaExercicioDao {
    @Transaction
    @Query("""
        SELECT e.nome, e.id, e.habilitado, e.dataDesabilitado, et.id as exercicioTipoId, et.nome,fe.position, fe.fichaId
        FROM ficha_exercicio fe 
        INNER JOIN exercicios e ON fe.exercicioId = e.id
        LEFT JOIN exercicio_tipo et ON e.exercicioTipoId = et.id
        WHERE fe.fichaId = :id
        ORDER BY fe.position
    """)
    fun getFichaExerciciosByIdFlow(id: Int): Flow<List<ExercicioComTipoDto2>>

    @Query(
        """

            UPDATE  
                ficha_exercicio
            SET 
                position = CASE 
                    WHEN exercicioId = :increaseExercise THEN position+1
                    ELSE position-1
                END
            WHERE 
                fichaId = :fichaId
                AND exercicioId = :increaseExercise 
                OR exercicioId = :decreaseExercise
        
        """
    )
    suspend fun switchPositions(fichaId: Int, increaseExercise: Int, decreaseExercise: Int)

    @Insert
    suspend fun insert(fichaExercicio: FichaExercicioEntity)
    @Transaction
    @Query("""
        SELECT e.nome, e.id, e.habilitado, e.dataDesabilitado, e.exercicioTipoId, et.id, et.nome,fe.position, fe.fichaId
        FROM ficha_exercicio fe 
        INNER JOIN exercicios e ON fe.exercicioId = e.id
        LEFT JOIN exercicio_tipo et ON e.exercicioTipoId = et.id
        WHERE fe.fichaId = :fichaId
        ORDER BY fe.position
    """)
    suspend fun getFichaExerciciosById(fichaId: Int): List<ExercicioComTipoDto2>
}