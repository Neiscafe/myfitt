package br.com.myfitt.treinos.data.repository

import br.com.myfitt.common.domain.Resultado
import br.com.myfitt.common.domain.TipoExercicio
import br.com.myfitt.common.domain.Treino
import br.com.myfitt.treinos.domain.repository.TreinoRepository
import kotlinx.coroutines.delay
import java.time.LocalDate

class TreinoRepositoryImpl : TreinoRepository {
    val treinos = mutableListOf<Treino>()
    override suspend fun listar(
        tamPagina: Int,
        pagina: Int,
        filtroTipos: List<TipoExercicio>?,
        filtroData: LocalDate?,
    ): Resultado<List<Treino>> {
        delay(500)
        if (tamPagina == 0 || pagina == 0) {
            return Resultado.Sucesso(treinos)
        }
        if (treinos.size < (tamPagina * pagina)) {
            return Resultado.Sucesso(emptyList())
        }
        val offset = (pagina - 1) * tamPagina
        return runCatching {
            Resultado.Sucesso(treinos.slice(offset..offset + tamPagina))
        }.getOrNull() ?: Resultado.Erro("Ocorreu um erro: estamos trabalhando para resolvê-lo")
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

    override suspend fun altera(novo: Treino): Resultado<Treino> {
        delay(500L)
        treinos.updateEntry(novo) { it.treinoId == novo.treinoId }
        return Resultado.Sucesso(novo)
    }

    fun <T> MutableList<T>.updateEntry(new: T, compare: (T) -> Boolean) {
        val index = indexOfFirst { compare(new) }
        if (index != -1) {
            this[index] = new
        }
    }

    companion object {
        private var contadorTreinoId = 0
        fun sequenciaTreinoId(): Int {
            contadorTreinoId++
            return contadorTreinoId
        }
    }
}