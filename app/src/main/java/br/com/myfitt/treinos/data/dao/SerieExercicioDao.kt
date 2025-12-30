package br.com.myfitt.treinos.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import br.com.myfitt.common.domain.SerieExercicio
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
        SELECT 
            b.*
        FROM
            exercicios_treino a
        INNER JOIN
            series_exercicio b
        ON b.exercicioTreinoId = a.exercicioTreinoId
        WHERE
            a.exercicioId = :exercicioId
        LIMIT 1
    """)
    suspend fun topSerie(exercicioId: Int) {

    }
}