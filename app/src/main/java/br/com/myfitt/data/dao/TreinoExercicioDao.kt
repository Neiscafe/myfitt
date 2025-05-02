package br.com.myfitt.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import br.com.myfitt.data.dto.PerformanceDto
import br.com.myfitt.data.dto.TreinoExercicioDto
import br.com.myfitt.data.entity.TreinoExercicioEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TreinoExercicioDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(treinoExercicio: TreinoExercicioEntity)

    @Update
    suspend fun update(treinoExercicio: TreinoExercicioEntity)

    @Transaction
    suspend fun deleteAndAdjustPosition(treinoExercicio: TreinoExercicioEntity) {
        reducePositionsBiggerThan(treinoExercicio.treinoId, treinoExercicio.posicao)
        delete(treinoExercicio)
    }

    @Query(
        """
        UPDATE 
            treino_exercicio
        SET 
            posicao = posicao -1
        WHERE
            treinoId = :treinoId
            AND posicao > :position
    """
    )
    suspend fun reducePositionsBiggerThan(treinoId: Int, position: Int)

    @Delete
    suspend fun delete(treinoExercicio: TreinoExercicioEntity)

    /**
     * Traz dados do cross + nome do exercício.
     * Necessário um JOIN entre treino_exercicio e exercicios.
     */
    @Query(
        """
        SELECT 
            te.treinoId AS treinoId,
            te.exercicioId AS exercicioId,
            ex.nome AS exercicioNome,
            tr.data AS data,
            te.series AS series,
            te.posicao AS posicao,
            te.pesoKg AS pesoKg,
            te.repeticoes AS repeticoes,
            te.observacao AS observacao,
            0 AS pesoKgUltimoTreino,
            0 AS repeticoesUltimoTreino,
            0 AS seriesUltimoTreino
        FROM treino_exercicio te
        INNER JOIN exercicios ex ON ex.id = te.exercicioId
        INNER JOIN treinos tr ON tr.id = te.treinoId
        WHERE te.treinoId = :treinoId AND COALESCE(ex.dataDesabilitado, DATE('now'))>=tr.data
        ORDER BY te.posicao ASC
    """
    )
    fun getExerciciosByTreino(treinoId: Int): Flow<List<TreinoExercicioDto>>
    @Query(
        """
            UPDATE  
                treino_exercicio
            SET 
                posicao = CASE 
                    WHEN exercicioId = :increaseExercise THEN posicao+1
                    ELSE posicao-1
                END
            WHERE 
                treinoId = :treinoId
                AND exercicioId = :increaseExercise 
                OR exercicioId = :decreaseExercise
        
        """
    )
    suspend fun switchPositions(treinoId: Int, increaseExercise: Int, decreaseExercise: Int)
    @Query(
        """
        SELECT 
            COALESCE(te.pesoKg,0) as pesoKg,
            COALESCE(te.repeticoes,0) as repeticoes,
            COALESCE(te.series,0) as series
        FROM 
            treino_exercicio te 
        INNER JOIN 
            treinos tr 
        ON 
            tr.id = te.treinoId 
        WHERE 
            te.exercicioId = :exercicioId 
        AND tr.data < DATE(:dataComparacao)
        ORDER BY 
            tr.data DESC
        LIMIT 1
    """
    )
    suspend fun getUltimaPerformance(exercicioId: Int, dataComparacao: String): PerformanceDto?
}