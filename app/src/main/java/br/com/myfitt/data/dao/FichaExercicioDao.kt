package br.com.myfitt.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import br.com.myfitt.data.entity.FichaExercicioDto
import br.com.myfitt.data.entity.FichaExercicioEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FichaExercicioDao {
    @Query("""
        SELECT  
            fe.*, f.nome as fichaNome, f.divisaoId, e.nome as exercicioNome, et.id as exercicioTipoId, et.nome as exercicioTipoNome
        FROM 
            ficha_exercicio fe 
        JOIN 
            ficha f ON f.id = fe.fichaId 
        JOIN 
            exercicios e ON e.id = fe.exercicioId 
        JOIN 
            exercicio_tipo et ON et.id = e.exercicioTipoId 
        WHERE 
            fe.fichaId = :id 
            AND e.habilitado = TRUE 
    """)
    fun getFichaExercicioByIdFlow(id: Int): Flow<List<FichaExercicioDto>>

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
}