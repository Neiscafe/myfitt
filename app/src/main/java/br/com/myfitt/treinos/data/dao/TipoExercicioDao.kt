package br.com.myfitt.treinos.data.dao

import androidx.room.Dao
import androidx.room.Query
import br.com.myfitt.treinos.data.dto.TipoExercicioTreinoDto
import br.com.myfitt.treinos.data.entities.TipoExercicioEntity

@Dao
interface TipoExercicioDao {
    @Query("""
        SELECT
            *
        FROM
            tipos_exercicio
    """)
    suspend fun lista(): List<TipoExercicioEntity>

    @Query(
        """
        SELECT DISTINCT
            a.treinoId,
            c.* 
        FROM 
            exercicios_treino a
        INNER JOIN
            exercicios b
        ON b.exercicioId=a.exercicioId
        INNER JOIN
            tipos_exercicio c
        ON c.tipoExercicioId=b.tipoExercicioId
        where 
            a.treinoId in (:treinoIds)        
    """
    )
    suspend fun doTreino(treinoIds: String): List<TipoExercicioTreinoDto>
}