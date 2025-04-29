package br.com.myfitt.data.repository

import android.util.Log
import br.com.myfitt.data.dao.TreinoExercicioDao
import br.com.myfitt.data.entity.TreinoExercicioCrossRef
import br.com.myfitt.domain.mapper.toDomain
import br.com.myfitt.domain.mapper.toEntity
import br.com.myfitt.domain.models.Exercicio
import br.com.myfitt.domain.models.ExercicioMudou
import br.com.myfitt.domain.models.ExercicioMudou.*
import br.com.myfitt.domain.models.Treino
import br.com.myfitt.domain.models.TreinoExercicioComNome
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.coroutineContext

private const val TAG = "TreinoExercicioRepo"

class TreinoExercicioRepository(
    private val dao: TreinoExercicioDao, private val exercicioRepository: ExercicioRepository
) {
    private val exercicios = mutableListOf<TreinoExercicioComNome>()

    suspend fun addExercicioAoTreino(treinoExercicio: TreinoExercicioComNome) = withContext(
        Dispatchers.IO
    ) {
        var exercicio = exercicioRepository.getExercicioPorNome(treinoExercicio.exercicioNome)
        if (exercicio == null) {
            exercicio =
                exercicioRepository.insertExercicio(Exercicio(nome = treinoExercicio.exercicioNome))
        }
        dao.insertCrossRef(treinoExercicio.copy(exercicioId = exercicio.id).toEntity())
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
            dao.updateCrossRef(entity)
            Log.d("TESTE", "foi realmente atualizado! $cached \n$entity")
        }
    }

    suspend fun removeExercicioDoTreino(treinoExercicio: TreinoExercicioComNome) = withContext(
        Dispatchers.IO
    ) {
        dao.deleteCrossRef(treinoExercicio.toEntity())
        var lastUpdatedPosition = 0
        val updatedExercicios = mutableListOf<TreinoExercicioCrossRef>()
        exercicios.forEach {
            val shouldDescrease = (it.exercicioId == treinoExercicio.exercicioId)
            if (it.posicao != lastUpdatedPosition && it.exercicioId != treinoExercicio.exercicioId) {
                updatedExercicios.add(it.copy(posicao = lastUpdatedPosition).toEntity())
            }
            if (!shouldDescrease) {
                lastUpdatedPosition++
            }
        }
        dao.updateMany(updatedExercicios.toTypedArray())
    }

    fun getExerciciosDeUmTreino(treinoId: Int): Flow<List<TreinoExercicioComNome>> {
        return dao.getExerciciosByTreino(treinoId).map { listDto ->
            listDto.map {
                val performance = dao.getUltimaPerformance(it.exercicioId, it.data)
                    ?: TreinoExercicioDao.PerformanceDto()
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

    suspend fun subirUmaPosicao(exercicioAbaixo: TreinoExercicioComNome) = withContext(
        Dispatchers.IO
    ) {
        if (exercicioAbaixo.posicao <= 0) return@withContext

        val exercicioAcima = exercicios.first { it.posicao == exercicioAbaixo.posicao - 1 }

        val exercicioDeslocadoParaCima = exercicioAbaixo.copy(posicao = exercicioAbaixo.posicao - 1)
        val exercicioDeslocadoParaBaixo = exercicioAcima.copy(posicao = exercicioAcima.posicao + 1)
        dao.updateMany(
            arrayOf(
                exercicioDeslocadoParaCima.toEntity(), exercicioDeslocadoParaBaixo.toEntity()
            )
        )
    }

    suspend fun descerUmaPosicao(exercicioAcima: TreinoExercicioComNome) = withContext(
        Dispatchers.IO
    ) {
        if (exercicioAcima.posicao >= exercicios.size - 1) return@withContext
        val exercicioAbaixo = exercicios.first { it.posicao == exercicioAcima.posicao + 1 }
        val exercicioDeslocadoParaBaixo = exercicioAcima.copy(posicao = exercicioAcima.posicao + 1)
        val exercicioDeslocadoParaCima = exercicioAbaixo.copy(posicao = exercicioAbaixo.posicao - 1)
        dao.updateMany(
            arrayOf(
                exercicioDeslocadoParaCima.toEntity(), exercicioDeslocadoParaBaixo.toEntity()
            )
        )
    }
}