package br.com.myfitt.treinos.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import br.com.myfitt.common.domain.Treino
import br.com.myfitt.treinos.data.entities.TreinoEntity

@Dao
interface TreinoDao {
    @Insert
    suspend fun insere(treino: TreinoEntity): Long
    @Query("""
        SELECT
        *
        FROM treinos
        WHERE treinoId = :treinoId
        ORDER BY dhInicio
    """)
    suspend fun busca(treinoId: Int): TreinoEntity?
    @Update
    suspend fun altera(treino: TreinoEntity)
    @Query("""
        SELECT
            *
        FROM
            treinos
        ORDER BY treinoId DESC
        LIMIT :tamPag
        OFFSET :tamPag*(:pag-1)
    """)
    suspend fun lista(tamPag: Int, pag: Int): List<TreinoEntity>
    @Query("""
        SELECT
            *
        FROM
            treinos
        WHERE
            dhFim is null and dhInicio is not null
        LIMIT 1
    """)
    suspend fun ativo(): TreinoEntity?

    @Delete
    suspend fun deleta(treinoEntity: TreinoEntity) }