package br.com.myfitt.data.repository

import br.com.myfitt.data.dao.TreinoExercicioDao
import br.com.myfitt.data.entity.TreinoExercicioEntity
import br.com.myfitt.data.mapper.toDomain
import br.com.myfitt.data.mapper.toEntity
import br.com.myfitt.data.mapper.toSeriesEntity
import br.com.myfitt.domain.ExerciseValidator
import br.com.myfitt.domain.models.Exercicio
import br.com.myfitt.domain.models.ExercicioMudou
import br.com.myfitt.domain.models.ExercicioMudou.ADICIONAR
import br.com.myfitt.domain.models.ExercicioMudou.DESCANSO
import br.com.myfitt.domain.models.ExercicioMudou.PESO
import br.com.myfitt.domain.models.ExercicioMudou.REPS
import br.com.myfitt.domain.models.ExercicioMudou.SERIES
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
        var _exercicio = exercicio
        if (_exercicio.id == 0) {
            _exercicio = exercicioRepository.insertExercicio(exercicio)
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
        val cached = exercicios.find { treinoExercicio.id == it.id }
        if (cached == null) return@withContext
        if (cached != treinoExercicio || exercicioMudou == ExercicioMudou.ADICIONAR || exercicioMudou == ExercicioMudou.REMOVER) {
            val entity = when (exercicioMudou) {
                SERIES -> {
                    dao.update(
                        treinoExercicio.copy(
                            pesoKg = cached.pesoKg,
                            repeticoes = cached.repeticoes,
                            segundosDescanso = cached.segundosDescanso
                        ).toEntity()
                    )
                    return@withContext
                }

                PESO -> {
                    treinoExercicio.copy(
                        series = cached.series,
                        repeticoes = cached.repeticoes,
                        segundosDescanso = cached.segundosDescanso
                    )
                }

                REPS -> treinoExercicio.copy(
                    pesoKg = cached.pesoKg,
                    series = cached.series,
                    segundosDescanso = cached.segundosDescanso
                )

                DESCANSO -> treinoExercicio.copy(
                    pesoKg = cached.pesoKg, series = cached.series, repeticoes = cached.repeticoes
                )

                ADICIONAR -> treinoExercicio
                REMOVER -> {
                    dao.delete(treinoExercicio.toSeriesEntity())
                    return@withContext
                }
            }.toSeriesEntity()
            dao.insert(entity)
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
//                val performance =
//                    dao.getUltimaPerformance(it.exercicioId, it.data) ?: PerformanceDto()
//                it.copy(
//                    seriesUltimoTreino = performance.series,
//                    repeticoesUltimoTreino = performance.repeticoes,
//                    pesoKgUltimoTreino = performance.pesoKg
//                )
                it.toDomain()
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
        val idAumentar = exercicios[exercicio.posicao - 1].id
        dao.switchPositions(exercicio.treinoId, idAumentar, exercicio.id)
    }

    suspend fun aumentarPosicao(exercicio: TreinoExercicioComNome) = withContext(
        Dispatchers.IO
    ) {
        if (exercicio.posicao >= exercicios.size - 1) return@withContext
        val idDiminuir = exercicios[exercicio.posicao + 1].id
        dao.switchPositions(exercicio.treinoId, exercicio.id, idDiminuir)
    }

    suspend fun addFromFicha(exercicios: List<Exercicio>, treinoId: Int) {
        exercicios.forEach {
            ExerciseValidator(it).canBeVinculatedToTreino()
            addExercicioAoTreino(treinoId, it)
        }
    }

    fun getHistorico(exercicioId: Int): Flow<List<HistoricoExercicioTreinos>?> {
        return dao.getHistorico(exercicioId).catch {
            LogTool.log("getHistorico", it.message!!, "exercicioId" to exercicioId)
            emit(null)
        }.map {
            it?.map {
                LogTool.log("getHistorico", it, "exercicioId" to exercicioId)
                HistoricoExercicioTreinos(
                    exercicioTreinoId = it.exercicioTreinoId,
                    dataTreino = it.dataTreino,
                    serieId = it.serieId,
                    segundosDescanso = it.segundosDescanso,
                    pesoKg = it.pesoKg,
                    repeticoes = it.repeticoes
                )
            }
        }
    }
}