package br.com.myfitt.treinos.data.repository

import br.com.myfitt.common.domain.ExercicioTemplate
import br.com.myfitt.common.domain.ExercicioTreino
import br.com.myfitt.common.domain.Resultado
import br.com.myfitt.common.domain.wrapSuspend
import br.com.myfitt.treinos.data.dao.ExercicioTreinoDao
import br.com.myfitt.treinos.data.mappers.toDomain
import br.com.myfitt.treinos.data.mappers.toEntity
import br.com.myfitt.treinos.domain.repository.ExercicioTreinoRepository

class ExercicioTreinoRepositoryImpl(val exercicioTreinoDao: ExercicioTreinoDao) :
    ExercicioTreinoRepository {

    override suspend fun adiciona(exercicioTreino: ExercicioTreino): Resultado<List<ExercicioTreino>> {
        return wrapSuspend {
            exercicioTreinoDao.adiciona(exercicioTreino.toEntity())
            exercicioTreinoDao.lista(exercicioTreino.treinoId).toDomain()
        }
    }

    override suspend fun lista(treinoId: Int): Resultado<List<ExercicioTreino>> {
        return wrapSuspend { exercicioTreinoDao.lista(treinoId).toDomain() }
    }

    override suspend fun remove(exercicioTreino: ExercicioTreino): Resultado<List<ExercicioTreino>> {
        return wrapSuspend {
            exercicioTreinoDao.remove(exercicioTreino.toEntity())
            exercicioTreinoDao.lista(exercicioTreino.treinoId).toDomain()
        }
    }

    override suspend fun substitui(
        novo: ExercicioTreino
    ): Resultado<List<ExercicioTreino>> {
        return wrapSuspend {
            exercicioTreinoDao.atualiza(novo.toEntity())
            exercicioTreinoDao.lista(novo.treinoId).toDomain()
        }
    }

    override suspend fun busca(exercicioTreinoId: Int): Resultado<ExercicioTreino> {
        return wrapSuspend { exercicioTreinoDao.busca(exercicioTreinoId).toDomain() }
    }

    override suspend fun reordena(
        exercicioTreino: ExercicioTreino, posicaoNova: Int
    ): Resultado<List<ExercicioTreino>> {
        return wrapSuspend {
            val listaAtual = exercicioTreinoDao.lista(exercicioTreino.treinoId)
            val seraTrocado = listaAtual.firstOrNull { it.ordem == posicaoNova }
            seraTrocado?.let {
                exercicioTreinoDao.atualiza(exercicioTreino.copy(ordem = posicaoNova).toEntity())
                exercicioTreinoDao.atualiza(it.copy(ordem = exercicioTreino.ordem))
                exercicioTreinoDao.lista(exercicioTreino.treinoId).toDomain()
            } ?: run { return@wrapSuspend listaAtual.toDomain() }
        }
    }

    override suspend fun adicionaPorTemplate(
        treinoId: Int, template: ExercicioTemplate
    ): Resultado<List<ExercicioTreino>> {
        TODO("Not yet implemented")
    }
}