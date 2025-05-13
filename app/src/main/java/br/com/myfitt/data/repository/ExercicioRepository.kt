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
        val existingExercise = dao.getExercicio(exercicio.nome)
        if (existingExercise != null) {
            return@withContext existingExercise.toDomain()
        }
        exercicio.copy(id = dao.insert(exercicio.toEntity()).toInt())
    }

    suspend fun getSugeridosExercicios(query: String): List<Exercicio> =
        dao.getSugeridosExercicios(query).map {
            it.toDomain()

        }

    suspend fun updateExercicio(exercicio: Exercicio) = withContext(Dispatchers.IO) {
        ExerciseValidator(exercicio).canBeUpdated()
        dao.update(
            exercicio.toEntity()
        )
    }

    suspend fun deleteExercicio(exercicio: Exercicio) = withContext(Dispatchers.IO) {
        dao.delete(
            exercicio.id
        )
    }

    fun getTiposFlow() = dao.getExercicioTiposFlow().map { it.map { it.toDomain() } }
}