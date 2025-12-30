package br.com.myfitt.treinos.data.repository

import br.com.myfitt.common.domain.Resultado
import br.com.myfitt.common.domain.SerieExercicio
import br.com.myfitt.common.domain.wrapSuspend
import br.com.myfitt.treinos.data.dao.SerieExercicioDao
import br.com.myfitt.treinos.data.mappers.toDomain
import br.com.myfitt.treinos.data.mappers.toEntity
import br.com.myfitt.treinos.domain.repository.SeriesRepository

class SeriesRepositoryImpl(val seriesDao: SerieExercicioDao) : SeriesRepository {
    override suspend fun topSerie(exercicioId: Int): Resultado<SerieExercicio> {
        seriesDao.topSerie()
    }

    override suspend fun todasDoTreino(treinoId: Int): Resultado<List<SerieExercicio>> {
        return wrapSuspend { seriesDao.listaTreino(treinoId).toDomain() }
    }

    override suspend fun lista(exercicioTreinoId: Int): Resultado<List<SerieExercicio>> {
        return wrapSuspend { seriesDao.lista(exercicioTreinoId).toDomain() }
    }


    override suspend fun cria(serie: SerieExercicio): Resultado<List<SerieExercicio>> {
        return wrapSuspend {
            seriesDao.cria(serie.toEntity())
            seriesDao.lista(serie.exercicioTreinoId).toDomain()
        }
    }

    override suspend fun altera(alterada: SerieExercicio): Resultado<List<SerieExercicio>> {
        return wrapSuspend {
            seriesDao.altera(alterada.toEntity())
            seriesDao.lista(alterada.exercicioTreinoId).toDomain()
        }
    }

    override suspend fun busca(serieId: Int): Resultado<SerieExercicio> {
        return wrapSuspend("Série") {
            seriesDao.busca(serieId).toDomain()
        }
    }
}