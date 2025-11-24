package br.com.myfitt.treinos.domain.repository

import br.com.myfitt.common.domain.Resultado
import br.com.myfitt.common.domain.Treino

interface TreinoRepository {
    suspend fun listar(): Resultado<List<Treino>>
    suspend fun criar(treino: Treino): Resultado<Treino>
}