package br.com.myfitt.data.repository

import br.com.myfitt.data.dao.FichaDao
import br.com.myfitt.data.dao.FichaExercicioDao
import br.com.myfitt.data.entity.FichaExercicioEntity
import br.com.myfitt.data.mapper.toDomain
import br.com.myfitt.data.mapper.toEntity
import br.com.myfitt.domain.ExerciseValidator
import br.com.myfitt.domain.models.Exercicio
import br.com.myfitt.domain.models.Ficha
import br.com.myfitt.domain.validate.FichaValidator
import br.com.myfitt.domain.validate.PosicaoValidator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlin.collections.first

class FichaRepository(
    private val fichaDao: FichaDao,
    private val fichaExercicioDao: FichaExercicioDao,
) {
    private val fichasCache: MutableList<Ficha> = mutableListOf()
    fun getTodasFichasFlow() = fichaDao.getTodasFichasFlow().map {
        it.map {
            it.toDomain()
        }
    }

    suspend fun getFichasByDivisaoId(divisaoId: Int) = withContext(Dispatchers.IO) {
        fichaDao.getFichasByDivisao(divisaoId).map {
            it.toDomain()
        }.addAllToCache()
    }

    fun getFichasByDivisaoIdFlow(divisaoId: Int) =
        fichaDao.getFichasByDivisaoFlow(divisaoId).map { it.map { it.toDomain() }.addAllToCache() }


    suspend fun insert(ficha: Ficha): Int = withContext(Dispatchers.IO) {
        FichaValidator.canBeInserted(ficha)
        fichaDao.insert(ficha.toEntity()).toInt()
    }

    fun getFichaExerciciosFlow(fichaId: Int): Flow<List<Exercicio>> {
        return fichaExercicioDao.getFichaExerciciosByIdFlow(fichaId).map { flow ->
            flow.map { it.toDomain() }.also { exercicios ->
                updateCache(getCachedFicha(fichaId).copy(exercicios = exercicios))
            }
        }
    }

    suspend fun getFichaExercicios(fichaId: Int): List<Exercicio> {
        return fichaExercicioDao.getFichaExerciciosById(fichaId).map {
            it.toDomain()
        }
    }

    suspend fun removeExercise(exercise: Exercicio) {
        fichaDao.delete(exercise.id, exercise.fichaId, exercise.posicao)
    }

    suspend fun increasePosition(exercise: Exercicio) {
        if (!PosicaoValidator.podeAumentar(exercise, getCachedFicha(exercise.fichaId).exercicios)) return
        val useFicha = this.getCachedFicha(exercise.fichaId)
        fichaExercicioDao.switchPositions(
            useFicha.id,
            exercise.id,
            useFicha.exercicios.first { it.posicao == exercise.posicao + 1 }.id,
        )
    }

    suspend fun decreasePosition(exercise: Exercicio) {
        if (!PosicaoValidator.podeDiminuir(exercise)) return
        val useFicha = getCachedFicha(exercise.fichaId)
        fichaExercicioDao.switchPositions(
            useFicha.id,
            useFicha.exercicios.first { it.posicao == exercise.posicao - 1 }.id,
            exercise.id,
        )
    }

    suspend fun addExerciseToFicha(exercise: Exercicio, fichaId: Int) {
        ExerciseValidator(exercise).canBeVinculatedToFicha()
        fichaExercicioDao.insert(
            FichaExercicioEntity(
                fichaId, exercise.id, getCachedFicha(fichaId).exercicios.size -1
            )
        )
    }

    private fun List<Ficha>.addAllToCache(): List<Ficha> {
        fichasCache.clear()
        fichasCache.addAll(this)
        return this
    }

    private fun updateCache(new: Ficha): List<Ficha> {
        val index = fichasCache.indexOfFirst { it.id == new.id }
        fichasCache[index] = new
        return fichasCache
    }

    private fun getCachedFicha(fichaId: Int) = fichasCache.first { it.id == fichaId }
}