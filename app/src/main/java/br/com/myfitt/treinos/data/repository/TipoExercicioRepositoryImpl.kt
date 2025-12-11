package br.com.myfitt.treinos.data.repository

import br.com.myfitt.common.domain.Resultado
import br.com.myfitt.common.domain.TipoExercicio
import br.com.myfitt.common.domain.wrapSuspend
import br.com.myfitt.treinos.data.dao.TipoExercicioDao
import br.com.myfitt.treinos.data.mappers.toDomain
import br.com.myfitt.treinos.domain.model.TipoExercicioTreino
import br.com.myfitt.treinos.domain.repository.TipoExercicioRepository

class TipoExercicioRepositoryImpl(val tipoExercicioDao: TipoExercicioDao) :
    TipoExercicioRepository {

    override suspend fun lista(emTreino: Boolean): Resultado<List<TipoExercicio>> {
        return wrapSuspend {
            tipoExercicioDao.lista().toDomain()
        }
    }

    override suspend fun doTreino(treinoIds: List<Int>): Resultado<List<TipoExercicioTreino>> {
        return wrapSuspend {
            val a = mutableMapOf<Int, List<TipoExercicio>>()
            tipoExercicioDao.doTreino(treinoIds.joinToString()).forEach {
                a[it.treinoId] = (a[it.treinoId] ?: emptyList()).plus(
                    TipoExercicio(
                        it.tipoExercicioId, it.descricao
                    )
                )
            }
            a.map {
                TipoExercicioTreino(
                    treinoId = it.key, tipoExercicios = it.value
                )
            }
        }
    }
}