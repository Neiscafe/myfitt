package br.com.myfitt.treinos.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import br.com.myfitt.common.domain.ExercicioTreino
import br.com.myfitt.treinos.data.entities.ExercicioTreinoEntity

@Dao
interface ExercicioTreinoDao {
    @Insert
    suspend fun adiciona(exercicioTreinoEntity: ExercicioTreinoEntity): Long
    @Update
    suspend fun atualiza(exercicioTreinoEntity: ExercicioTreinoEntity)
    @Delete
    suspend fun remove(exercicioTreinoEntity: ExercicioTreinoEntity)

    @Query("""
        select
            *
        from
            exercicios_treino
        where
            treinoId = :treinoId
        order by ordem
    """)
    suspend fun lista(treinoId: Int): List<ExercicioTreinoEntity>
    @Query("""
        select
            *
        from
            exercicios_treino
        where
            exercicioTreinoId = :exercicioTreinoId
    """)
    suspend fun busca(exercicioTreinoId: Int): ExercicioTreinoEntity
}