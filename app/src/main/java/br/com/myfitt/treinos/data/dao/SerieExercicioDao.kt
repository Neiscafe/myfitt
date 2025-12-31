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
       WITH C AS (
            SELECT 
                B.*,
                -- Cálculo do 1RM usando Epley: Peso * (1 + Reps/30)
                ( B.pesoKg * (1 + (B.repeticoes / 30.0))) AS um_rm_estimado
            FROM exercicios A
            INNER JOIN series_exercicio B
            ON B.exercicioId = A.exercicioId
            WHERE A.exercicioId = :exercicioId-- Filtre pelo exercício desejado
            AND B.treinoId != :treinoId
        ),
        D AS (
            SELECT C.*,
                -- Rank por data (mais recente)
                ROW_NUMBER() OVER (ORDER BY C.dhFimExecucao DESC) as rank_recente,
                -- Rank por carga (maior 1RM)
                ROW_NUMBER() OVER (ORDER BY C.um_rm_estimado DESC) as rank_forca
            FROM C
        )
      SELECT 
        CASE 
            WHEN D.rank_recente = 1 AND rank_forca = 1 THEN 'Última e Melhor Marca'
            WHEN D.rank_recente = 1 THEN 'Última Série'
            WHEN D.rank_forca = 1 THEN 'Recorde de Força (Mais próximo do 1RM)'
        END as categoria,
        D.*
, ROUND(D.um_rm_estimado, 2) as um_rm_estimado
FROM D
WHERE D.rank_recente = 1 OR D.rank_forca = 1;
    """)
    suspend fun seriesDestaqueExercicio(exercicioId: Int, treinoId: Int): List<DestaquesExercicioDto>
}