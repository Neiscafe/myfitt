package br.com.myfitt.treinos.domain.repository

import br.com.myfitt.common.domain.Exercicio
import br.com.myfitt.common.domain.Resultado
import br.com.myfitt.common.domain.Treino

interface ExercicioRepository {
    suspend fun lista(pesquisa: String): Resultado<List<Exercicio>>
    suspend fun busca(exercicioId: Int): Resultado<Exercicio>
    suspend fun altera(novo: Exercicio): Resultado<Exercicio>
}