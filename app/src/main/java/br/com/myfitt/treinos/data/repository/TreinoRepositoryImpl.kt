package br.com.myfitt.treinos.data.repository

import br.com.myfitt.common.domain.Resultado
import br.com.myfitt.common.domain.Treino
import br.com.myfitt.treinos.domain.repository.TreinoRepository
import kotlinx.coroutines.delay
import java.time.LocalDateTime

class TreinoRepositoryImpl : TreinoRepository {
    val treinos = mutableListOf<Treino>()
    override suspend fun listar(): Resultado<List<Treino>> {
        delay(500)
        return Resultado.Sucesso(treinos)
    }

    override suspend fun criar(treino: Treino): Resultado<Treino> {
        delay(500)
        val treino = treino.copy(treinoId = sequenciaTreinoId())
        treinos.add(treino)
        return Resultado.Sucesso(treino)
    }

    override suspend fun busca(treinoId: Int): Resultado<Treino> {
        return treinos.firstOrNull { it.treinoId == treinoId }?.let { Resultado.Sucesso(it) }
            ?: Resultado.Erro("Treino não encontrado!")
    }

    companion object {
        private var contadorTreinoId = 0
        fun sequenciaTreinoId(): Int {
            contadorTreinoId++
            return contadorTreinoId
        }
    }
}