package br.com.myfitt.data.repository

import br.com.myfitt.data.dao.FichaDao
import br.com.myfitt.data.dao.FichaExercicioDao
import br.com.myfitt.data.entity.ExercicioComTipo
import br.com.myfitt.data.mapper.toDomain
import br.com.myfitt.data.mapper.toEntity
import br.com.myfitt.domain.models.Exercicio
import br.com.myfitt.domain.models.Ficha
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class FichaRepository(
    private val fichaDao: FichaDao,
    private val fichaExercicioDao: FichaExercicioDao
) {
    fun getTodasByDivisaoFlow(divisaoId: Int) =
        fichaDao.getTodasByDivisaoFlow(divisaoId).map { it.map { it.toDomain() } }

    suspend fun getTodasByDivisao(divisaoId: Int) = withContext(Dispatchers.IO) {
        fichaDao.getTodasByDivisao(divisaoId).map { it.toDomain() }
    }

    suspend fun insert(ficha: Ficha): Int = withContext(Dispatchers.IO) {
        fichaDao.insert(ficha.toEntity()).toInt()
    }

    fun getFichaByIdFlow(fichaId: Int): Flow<Ficha> {
        return fichaExercicioDao.getFichaExercicioByIdFlow(fichaId).map { it.toDomain() }
    }

    suspend fun removeExercise(ficha: Ficha, exercise: Exercicio) {

    }
    suspend fun moveUp(ficha: Ficha, exercise: Exercicio) {
        fichaExercicioDao.moveUp(ficha.id, exercise.id)
    }
    suspend fun moveDown(ficha: Ficha, exercise: Exercicio) {}
    suspend fun addExercise(ficha: Ficha, exercise: Exercicio) {}
}