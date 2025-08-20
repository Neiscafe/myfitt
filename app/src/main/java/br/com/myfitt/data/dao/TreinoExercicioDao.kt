package br.com.myfitt.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import br.com.myfitt.data.dto.HistoricoExercicioTreinosDto
import br.com.myfitt.data.dto.PerformanceDto
import br.com.myfitt.data.entity.ExercicioWithTreinoExerciciosAndSeries
import br.com.myfitt.data.entity.TreinoExercicioEntity
import br.com.myfitt.data.entity.TreinoExercicioSerieEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TreinoExercicioDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(treinoExercicio: TreinoExercicioEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(treinoExercicio: TreinoExercicioSerieEntity): Long

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
    suspend fun delete(serie: TreinoExercicioSerieEntity): Int

    @Delete
    suspend fun delete(treinoExercicio: TreinoExercicioEntity)

    @Query(
        """
        SELECT * FROM treino_exercicio WHERE treinoId = :treinoId
    """
    )
    fun getExerciciosByTreino(treinoId: Int): Flow<List<ExercicioWithTreinoExerciciosAndSeries>>

    @Query(
        """
        SELECT * FROM treino_exercicio WHERE id = :exercicioTreinoId 
    """
    )
    fun getTreinoExercicioSeriesById(exercicioTreinoId: Int): Flow<ExercicioWithTreinoExerciciosAndSeries?>

    @Query(
        """
            UPDATE  
                treino_exercicio
            SET 
                posicao = CASE 
                    WHEN id = :increaseExercise THEN posicao+1
                    ELSE posicao-1
                END
            WHERE 
                treinoId = :treinoId
                AND id = :increaseExercise 
                OR id = :decreaseExercise
        
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

    @Query(
        """
        SELECT
            te.id as exercicioTreinoId,
            t.data as dataTreino,
            tes.id as serieId,
            tes.segundosDescanso as segundosDescanso,
            tes.pesoKg AS pesoKg,
            tes.reps AS repeticoes
        FROM treino_exercicio_serie tes
        INNER JOIN treino_exercicio te ON tes.treinoExercicioId = te.id
        INNER JOIN treinos t ON te.treinoId = t.id
        WHERE te.exercicioId = :exercicioId
        ORDER BY 
            t.data DESC,
            tes.id DESC
        LIMIT 20
    """
    )
    fun getHistorico(exercicioId: Int): Flow<List<HistoricoExercicioTreinosDto>?>
}