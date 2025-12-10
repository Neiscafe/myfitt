package br.com.myfitt.treinos.data.repository

import br.com.myfitt.common.domain.Resultado
import br.com.myfitt.common.domain.TipoExercicio
import br.com.myfitt.treinos.domain.repository.TipoExercicioRepository
import kotlinx.coroutines.delay

class TipoExercicioRepositoryImpl : TipoExercicioRepository {
    val tipoExercicios = listOf(
        TipoExercicio(1, "Peito"), TipoExercicio(2, "Quadríceps"), TipoExercicio(3, "Bíceps")
    )

    override suspend fun lista(emTreino: Boolean): Resultado<List<TipoExercicio>> {
        delay(500L)
        return Resultado.Sucesso(tipoExercicios)
    }

    override suspend fun doTreino(treinoIds: List<Int>): Resultado<List<Pair<Int, TipoExercicio>>> {
        delay(500L)
        return Resultado.Sucesso(buildList {
            treinoIds.forEach { treinoId ->
                repeat(2) {
                    add(treinoId to tipoExercicios.random())
                }
            }
        })
    }
}