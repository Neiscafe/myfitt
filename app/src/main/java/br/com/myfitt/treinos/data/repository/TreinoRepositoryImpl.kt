package br.com.myfitt.treinos.data.repository

import br.com.myfitt.common.domain.Resultado
import br.com.myfitt.common.domain.TipoExercicio
import br.com.myfitt.common.domain.Treino
import br.com.myfitt.common.domain.map
import br.com.myfitt.common.domain.wrapSuspend
import br.com.myfitt.treinos.data.dao.TreinoDao
import br.com.myfitt.treinos.data.mappers.toDomain
import br.com.myfitt.treinos.data.mappers.toEntity
import br.com.myfitt.treinos.domain.repository.TreinoRepository
import java.time.LocalDate

class TreinoRepositoryImpl(private val treinoDao: TreinoDao) : TreinoRepository {
    val treinos = mutableListOf<Treino>()
    override suspend fun listar(
        tamPagina: Int,
        pagina: Int,
        filtroTipos: List<TipoExercicio>?,
        filtroData: LocalDate?,
    ): Resultado<List<Treino>> {
        return wrapSuspend { treinoDao.lista(tamPagina, pagina).toDomain() }
    }


    override suspend fun criar(treino: Treino): Resultado<Treino> {
        return wrapSuspend {
            treinoDao.insere(treino.toEntity())
        }.map { treino.copy(treinoId = it.toInt()) }
    }

    override suspend fun busca(treinoId: Int): Resultado<Treino> {
        return wrapSuspend { treinoDao.busca(treinoId).toDomain() }
    }

    override suspend fun altera(novo: Treino): Resultado<Treino> {
        return wrapSuspend { treinoDao.altera(novo.toEntity()) }.map { novo }
    }
}