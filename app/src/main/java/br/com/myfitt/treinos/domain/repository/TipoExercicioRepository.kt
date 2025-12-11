package br.com.myfitt.treinos.domain.repository

import br.com.myfitt.common.domain.Resultado
import br.com.myfitt.common.domain.TipoExercicio
import br.com.myfitt.treinos.domain.model.TipoExercicioTreino

interface TipoExercicioRepository {
    suspend fun lista(emTreino: Boolean): Resultado<List<TipoExercicio>>
    suspend fun doTreino(treinoIds: List<Int>): Resultado<List<TipoExercicioTreino>>
}