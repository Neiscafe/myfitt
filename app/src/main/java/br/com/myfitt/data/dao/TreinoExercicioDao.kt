package br.com.myfitt.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import br.com.myfitt.data.entity.TreinoExercicioCrossRef
import kotlinx.coroutines.flow.Flow

@Dao
interface TreinoExercicioDao {
    @Transaction
    suspend fun updateMany(toUpdate: Array<TreinoExercicioCrossRef>) {
        toUpdate.forEach {
            updateCrossRef(it)
        }
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCrossRef(treinoExercicio: TreinoExercicioCrossRef)

    @Update
    suspend fun updateCrossRef(treinoExercicio: TreinoExercicioCrossRef)

    @Delete
    suspend fun deleteCrossRef(treinoExercicio: TreinoExercicioCrossRef)

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
    @Query("""
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
    """)
    suspend fun getUltimaPerformance(exercicioId: Int, dataComparacao: String): PerformanceDto?

    data class TreinoExercicioDto(
        val treinoId: Int,
        val exercicioId: Int,
        val exercicioNome: String,
        val data: String,
        val series: Int,
        val posicao: Int,
        val pesoKg: Float,
        val repeticoes: Int,
        val observacao: String?,
        val seriesUltimoTreino: Int = 0,
        val repeticoesUltimoTreino: Int = 0,
        val pesoKgUltimoTreino: Float = 0f
    )
    data class PerformanceDto(
        val series: Int = 0,
        val pesoKg: Float = 0f,
        val repeticoes: Int = 0,
    )
}