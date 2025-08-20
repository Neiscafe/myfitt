package br.com.myfitt.data.repository

import br.com.myfitt.data.dao.TreinoExercicioDao
import br.com.myfitt.data.entity.TreinoExercicioEntity
import br.com.myfitt.data.entity.TreinoExercicioSerieEntity
import br.com.myfitt.data.mapper.toEntity
import br.com.myfitt.domain.ExerciseValidator
import br.com.myfitt.domain.models.Exercicio
import br.com.myfitt.domain.models.ExercicioTreino
import br.com.myfitt.domain.models.HistoricoExercicioTreinos
import br.com.myfitt.domain.models.Serie
import br.com.myfitt.log.LogTool
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

private const val TAG = "TreinoExercicioRepo"

class TreinoExercicioRepository(
    private val dao: TreinoExercicioDao, private val exercicioRepository: ExercicioRepository
) {
    private val exercicios = mutableListOf<ExercicioTreino>()

    suspend fun addExercicioAoTreino(treinoId: Int, exercicio: Exercicio): Long? = withContext(
        Dispatchers.IO
    ) {
        var _exercicio = exercicio
        if (_exercicio.id == 0) {
            _exercicio = exercicioRepository.insertExercicio(exercicio)
        }
        return@withContext runCatching {
            dao.insert(
                TreinoExercicioEntity(
                    treinoId = treinoId,
                    exercicioId = _exercicio.id,
                    posicao = exercicio.posicao,
                )
            )
        }.getOrNull()
    }

    suspend fun addSeries(serie: Serie): Boolean {
        return runCatching {
            dao.insert(
                TreinoExercicioSerieEntity(
                    id = serie.id,
                    treinoExercicioId = serie.exercicioTreinoId,
                    pesoKg = serie.pesoKg,
                    reps = serie.reps,
                    segundosDescanso = serie.segundosDescanso
                )
            ) > 0L
        }.onFailure {
            LogTool.log(
                "addSeries",
                Unit,
                "serie" to serie,
                "message" to it.message,
                "stackTrace" to it.stackTraceToString()
            )
        }.onSuccess { }.getOrNull() ?: false
    }


    suspend fun removeSeries(serie: Serie): Boolean {
        return runCatching {
            dao.delete(
                TreinoExercicioSerieEntity(
                    id = serie.id,
                    treinoExercicioId = serie.exercicioTreinoId,
                    pesoKg = serie.pesoKg,
                    reps = serie.reps,
                    segundosDescanso = serie.segundosDescanso
                )
            ) > 0L
        }.onFailure {
            LogTool.log(
                "removeSeries",
                Unit,
                "serie" to serie,
                "message" to it.message,
                "stackTrace" to it.stackTraceToString()
            )
        }.getOrNull() ?: false
    }

    suspend fun updateSeries(
        serie: Serie
    ): Boolean {
        return runCatching {
            LogTool.log(
                "updateSeries",
                Unit,
                "serie" to serie,
            )
//            val exercicioTreinoCached = exercicios.find { it.id == serie.exercicioTreinoId }
//            val serieCached = exercicioTreinoCached?.seriesLista?.find { it.id == serie.id }
//            if (exercicioTreinoCached == null || serieCached == null) {
//                return false
//            }
            dao.insert(
                TreinoExercicioSerieEntity(
                    id = serie.id,
                    treinoExercicioId = serie.exercicioTreinoId,
                    pesoKg = serie.pesoKg,
                    reps = serie.reps,
                    segundosDescanso = serie.segundosDescanso
                )
            ) > 0L
        }.onFailure {
            LogTool.log(
                "updateSeries",
                Unit,
                "serie" to serie,
                "message" to it.message,
                "stackTrace" to it.stackTraceToString()
            )
        }.getOrNull() ?: false
    }

    suspend fun removeExercicioDoTreino(treinoExercicio: ExercicioTreino) = withContext(
        Dispatchers.IO
    ) {
        dao.deleteAndAdjustPosition(treinoExercicio.toEntity())
    }

    fun getExerciciosDeUmTreino(treinoId: Int): Flow<List<ExercicioTreino>> {
        return dao.getExerciciosByTreino(treinoId).map {
            it.map {
//                LogTool.log(it)
                ExercicioTreino(
                    id = it.treinoExercicioAndSeries.treinoExercicio.id,
                    treinoId = treinoId,
                    exercicio = Exercicio(it.exercicio.nome, it.exercicio.id),
                    posicao = it.treinoExercicioAndSeries.treinoExercicio.posicao,
                    observacao = "",
                    seriesLista = it.treinoExercicioAndSeries.series.map {
                        Serie(
                            id = it.id,
                            pesoKg = it.pesoKg ?: 0f,
                            reps = it.reps ?: 0,
                            segundosDescanso = it.segundosDescanso ?: 0,
                            exercicioTreinoId = it.treinoExercicioId
                        )
                    }).also {
                    synchronized(exercicios) {
                        exercicios.clear()
                        exercicios.add(it)
                    }
                }
            }
        }
    }

    fun getTreinoExercicioSeriesById(treinoExercicioId: Int): Flow<ExercicioTreino?> {
        return dao.getTreinoExercicioSeriesById(treinoExercicioId).catch { emit(null) }.map {
//            LogTool.log(it)
            it?.let {
                ExercicioTreino(
                    id = it.treinoExercicioAndSeries.treinoExercicio.id,
                    treinoId = it.treinoExercicioAndSeries.treinoExercicio.treinoId,
                    exercicio = Exercicio(it.exercicio.nome, it.exercicio.id),
                    posicao = it.treinoExercicioAndSeries.treinoExercicio.posicao,
                    observacao = "",
                    seriesLista = it.treinoExercicioAndSeries.series.map {
                        Serie(
                            id = it.id,
                            pesoKg = it.pesoKg ?: 0f,
                            reps = it.reps ?: 0,
                            segundosDescanso = it.segundosDescanso ?: 0,
                            exercicioTreinoId = it.treinoExercicioId
                        )
                    })
            }
        }
    }


    suspend fun diminuirPosicao(exercicio: ExercicioTreino) = withContext(
        Dispatchers.IO
    ) {
        if (exercicio.posicao <= 0) return@withContext
        val idAumentar = exercicios[exercicio.posicao - 1].id
        dao.switchPositions(exercicio.treinoId, idAumentar, exercicio.id)
    }

    suspend fun aumentarPosicao(exercicio: ExercicioTreino) = withContext(
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