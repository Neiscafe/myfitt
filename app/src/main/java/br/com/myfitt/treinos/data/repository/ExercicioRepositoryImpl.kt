package br.com.myfitt.treinos.data.repository

import br.com.myfitt.common.domain.Exercicio
import br.com.myfitt.common.domain.Resultado
import br.com.myfitt.common.domain.map
import br.com.myfitt.common.domain.wrapSuspend
import br.com.myfitt.treinos.data.dao.ExercicioDao
import br.com.myfitt.treinos.data.mappers.toDomain
import br.com.myfitt.treinos.data.mappers.toEntity
import br.com.myfitt.treinos.domain.repository.ExercicioRepository

class ExercicioRepositoryImpl(val exercicioDao: ExercicioDao) : ExercicioRepository {

    override suspend fun lista(pesquisa: String): Resultado<List<Exercicio>> {
        return wrapSuspend { exercicioDao.lista(pesquisa).toDomain() }
    }

    override suspend fun busca(exercicioId: Int): Resultado<Exercicio> {
        return wrapSuspend { exercicioDao.busca(exercicioId)?.toDomain()!! }
    }

    override suspend fun altera(novo: Exercicio): Resultado<Exercicio> {
        return wrapSuspend { exercicioDao.altera(novo.toEntity()) }.map { novo }
    }

    override suspend fun doTreino(treinoId: Int): Resultado<List<Exercicio>> {
        return wrapSuspend { exercicioDao.doTreino(treinoId).toDomain() }
    }
}