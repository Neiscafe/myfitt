package br.com.myfitt.data.repository

import br.com.myfitt.data.dao.TreinoExercicioDao
import br.com.myfitt.data.dto.PerformanceDto
import br.com.myfitt.data.entity.TreinoExercicioEntity
import br.com.myfitt.data.mapper.toDomain
import br.com.myfitt.data.mapper.toEntity
import br.com.myfitt.domain.models.Exercicio
import br.com.myfitt.domain.models.ExercicioMudou
import br.com.myfitt.domain.models.ExercicioMudou.*
import br.com.myfitt.domain.models.TreinoExercicioComNome
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

private const val TAG = "TreinoExercicioRepo"

class TreinoExercicioRepository(
    private val dao: TreinoExercicioDao, private val exercicioRepository: ExercicioRepository
) {
    private val exercicios = mutableListOf<TreinoExercicioComNome>()

    suspend fun addExercicioAoTreino(treinoId: Int, exercicio: Exercicio) = withContext(
        Dispatchers.IO
    ) {
        val _exercicio = exercicio
        if (_exercicio.id == 0) {
            _exercicio.copy(id = exercicioRepository.insertExercicio(exercicio))
        }
        dao.insert(
            TreinoExercicioEntity(
                treinoId = treinoId,
                exercicioId = _exercicio.id,
                posicao = exercicio.posicao,
            )
        )
    }

    suspend fun updateExercicioDoTreino(
        treinoExercicio: TreinoExercicioComNome, exercicioMudou: ExercicioMudou
    ) = withContext(
        Dispatchers.IO
    ) {
        val cached = exercicios.find { treinoExercicio.exercicioId == it.exercicioId }
        if (cached == null) return@withContext
        if (cached != treinoExercicio) {
            val entity = when (exercicioMudou) {
                SERIES -> treinoExercicio.copy(
                    pesoKg = cached.pesoKg, repeticoes = cached.repeticoes
                )

                PESO -> treinoExercicio.copy(series = cached.series, repeticoes = cached.repeticoes)
                REPS -> treinoExercicio.copy(pesoKg = cached.pesoKg, series = cached.series)
            }.toEntity()
            dao.update(entity)
        }
    }

    suspend fun removeExercicioDoTreino(treinoExercicio: TreinoExercicioComNome) = withContext(
        Dispatchers.IO
    ) {
        dao.deleteAndAdjustPosition(treinoExercicio.toEntity())
    }

    fun getExerciciosDeUmTreino(treinoId: Int): Flow<List<TreinoExercicioComNome>> {
        return dao.getExerciciosByTreino(treinoId).map { listDto ->
            listDto.map {
                val performance =
                    dao.getUltimaPerformance(it.exercicioId, it.data) ?: PerformanceDto()
                it.copy(
                    seriesUltimoTreino = performance.series,
                    repeticoesUltimoTreino = performance.repeticoes,
                    pesoKgUltimoTreino = performance.pesoKg
                ).toDomain()
            }.also {
                exercicios.clear()
                exercicios.addAll(it)
            }
        }
    }

    suspend fun diminuirPosicao(exercicio: TreinoExercicioComNome) = withContext(
        Dispatchers.IO
    ) {
        if (exercicio.posicao <= 0) return@withContext
        val idAumentar = exercicios.first { it.posicao == exercicio.posicao - 1 }.exercicioId
        dao.switchPositions(exercicio.treinoId, idAumentar, exercicio.exercicioId)
    }

    suspend fun aumentarPosicao(exercicio: TreinoExercicioComNome) = withContext(
        Dispatchers.IO
    ) {
        if (exercicio.posicao >= exercicios.size - 1) return@withContext
        val idDiminuir = exercicios.first { it.posicao == exercicio.posicao + 1 }.exercicioId
        dao.switchPositions(exercicio.treinoId, exercicio.exercicioId, idDiminuir)
    }
}