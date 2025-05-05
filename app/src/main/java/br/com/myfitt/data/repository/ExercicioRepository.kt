package br.com.myfitt.data.repository

import br.com.myfitt.data.dao.ExercicioDao
import br.com.myfitt.data.mapper.toDomain
import br.com.myfitt.data.mapper.toEntity
import br.com.myfitt.domain.ExerciseValidator
import br.com.myfitt.domain.models.Exercicio
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

private const val TAG = "ExerciciosRepo"

class ExercicioRepository(private val dao: ExercicioDao) {
    suspend fun getExercicio(id: Int): Exercicio? = withContext(Dispatchers.IO) {
        dao.getExercicio(id)?.toDomain()
    }

    suspend fun insertExercicio(exercicio: Exercicio): Exercicio = withContext(
        Dispatchers.IO
    ) {
        ExerciseValidator(exercicio).canBeCreated()
        val insertedId = dao.insert(exercicio.toEntity())
        if (insertedId == -1L) {
            return@withContext dao.getExercicio(exercicio.nome)!!.toDomain()
        }
        exercicio.copy(id = insertedId.toInt())
    }

    fun getSugeridosExercicios(query: String): Flow<List<Exercicio>> =
        dao.getSugeridosExercicios(query).map {
            it.map {
                it.toDomain()
            }
        }

    suspend fun updateExercicio(exercicio: Exercicio) = withContext(Dispatchers.IO) {
        dao.update(
            exercicio.toEntity()
        )
    }

    suspend fun deleteExercicio(exercicio: Exercicio) = withContext(Dispatchers.IO) {
        dao.delete(
            exercicio.id
        )
    }
}