package br.com.myfitt.treinos.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import br.com.myfitt.common.domain.Exercicio
import br.com.myfitt.treinos.data.entities.ExercicioEntity
import br.com.myfitt.treinos.data.relations.ExercicioComTipoRelation

@Dao
interface ExercicioDao {
    @Query("""
        SELECT 
            *
        FROM 
            exercicios
        WHERE
            lower(nome) like '%' || lower(:pesquisa) || '%'
    """)
    suspend fun lista(pesquisa: String): List<ExercicioComTipoRelation>

    @Query("""
        SELECT
            *
        FROM
            exercicios
        WHERE
            exercicioId = :exercicioId
    """)
    suspend fun busca(exercicioId: Int): ExercicioComTipoRelation?

    @Update
    suspend fun altera(novo: ExercicioEntity)
}