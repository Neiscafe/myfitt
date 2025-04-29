package br.com.myfitt.data.repository

import br.com.myfitt.data.dao.ExercicioDao
import br.com.myfitt.data.entity.ExercicioComTipoDto
import br.com.myfitt.data.entity.ExercicioEntity
import br.com.myfitt.domain.mapper.toDomain
import br.com.myfitt.domain.mapper.toEntity
import br.com.myfitt.domain.models.Exercicio
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

private const val TAG = "ExerciciosRepo"

class ExercicioRepository(private val dao: ExercicioDao) {
    suspend fun getExercicioPorNome(nome: String): Exercicio? = withContext(Dispatchers.IO) {
        return@withContext dao.getExercicioPorNome(nome)?.toDomain()
    }

    suspend fun insertExercicio(exercicio: Exercicio): Exercicio = withContext(
        Dispatchers.IO
    ) {
        val entity = exercicio.toEntity()
        return@withContext exercicio.copy(id = dao.insert(entity).toInt())
    }

    fun getSugeridosExercicios(query: String): Flow<List<Exercicio>> =
        dao.getSugeridosExercicios(query).map { it.map { it.toDomain() } }

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