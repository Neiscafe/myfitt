package br.com.myfitt.treinos.data.repository

import br.com.myfitt.common.domain.Resultado
import br.com.myfitt.common.domain.Treino
import br.com.myfitt.treinos.domain.repository.TreinoRepository
import kotlinx.coroutines.delay
import java.time.LocalDateTime

class TreinoRepositoryImpl : TreinoRepository {
    val treinos = mutableListOf(Treino(sequenciaTreinoId(), LocalDateTime.now().minusDays(1)))
    override suspend fun listar(): Resultado<List<Treino>> {
        delay(500)
        return Resultado.Sucesso(treinos)
    }

    override suspend fun criar(treino: Treino): Resultado<Unit> {
        delay(500)
        treinos.add(treino.copy(treinoId = sequenciaTreinoId()))
        return Resultado.Sucesso(Unit)
    }

    companion object {
        private var contadorTreinoId = 0
        fun sequenciaTreinoId(): Int {
            contadorTreinoId++
            return contadorTreinoId
        }
    }
}