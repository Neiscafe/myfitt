package br.com.myfitt.treinos.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import br.com.myfitt.treinos.data.dto.DestaquesExercicioDto
import br.com.myfitt.treinos.data.entities.SerieExercicioEntity

@Dao
interface SerieExercicioDao {
    @Query(
        """
        select
            *
        from
            series_exercicio
        where
            treinoId = :treinoId
        order by serieId
    """
    )
    suspend fun listaTreino(treinoId: Int): List<SerieExercicioEntity>

    @Query(
        """
        select
            *
        from
            series_exercicio
        where
            exercicioTreinoId = :exercicioTreinoId
        order by serieId
    """
    )
    suspend fun lista(exercicioTreinoId: Int): List<SerieExercicioEntity>

    @Insert
    suspend fun cria(toEntity: SerieExercicioEntity): Long

    @Update
    suspend fun altera(toEntity: SerieExercicioEntity)

    @Query("""
        SELECT
            *
        FROM
            series_exercicio
        WHERE
            serieId = :serieId
    """)
    suspend fun busca(serieId: Int): SerieExercicioEntity

    @Query("""
        SELECT * FROM (
            SELECT 
                'Última Série' as categoria,
                b.exercicioTreinoId, b.serieId, b.exercicioId, b.treinoId,
                b.dhInicioExecucao, b.dhFimExecucao, b.dhInicioDescanso, b.dhFimDescanso,
                b.duracaoSegundos, b.segundosDescanso, b.pesoKg, b.repeticoes, b.finalizado,
                (b.pesoKg * (1.0 + (b.repeticoes / 30.0))) AS umRmEstimado
            FROM series_exercicio b
            WHERE b.exercicioId = :exercicioId AND b.treinoId != :treinoId
            ORDER BY b.dhFimExecucao DESC
            LIMIT 1
        )
        
        UNION ALL
        
        SELECT * FROM (
            SELECT 
                'Recorde de Força' as categoria,
                b.exercicioTreinoId, b.serieId, b.exercicioId, b.treinoId,
                b.dhInicioExecucao, b.dhFimExecucao, b.dhInicioDescanso, b.dhFimDescanso,
                b.duracaoSegundos, b.segundosDescanso, b.pesoKg, b.repeticoes, b.finalizado,
                (b.pesoKg * (1.0 + (b.repeticoes / 30.0))) AS umRmEstimado
            FROM series_exercicio b
            WHERE b.exercicioId = :exercicioId AND b.treinoId != :treinoId
            ORDER BY umRmEstimado DESC
            LIMIT 1
        );
    """)
    suspend fun seriesDestaqueExercicio(exercicioId: Int, treinoId: Int): List<DestaquesExercicioDto>
}