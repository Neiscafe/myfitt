package br.com.myfitt.treinos.data.repository

import br.com.myfitt.common.domain.Resultado
import br.com.myfitt.common.domain.SerieExercicio
import br.com.myfitt.treinos.domain.repository.SeriesRepository
import kotlinx.coroutines.delay

class SeriesRepositoryImpl : SeriesRepository {
    private val series: MutableList<SerieExercicio> = mutableListOf()
    private val seriesIndex = mutableMapOf<Int, Int>()
    override suspend fun lista(exercicioTreinoId: Int): Resultado<List<SerieExercicio>> {
        delay(500)
        return Resultado.Sucesso(series)
    }

    override suspend fun cria(serie: SerieExercicio): Resultado<List<SerieExercicio>> {
        delay(500)
        val serieId = incrementaSequencia()
        series.add(serie.copy(serieId = serieId))
        seriesIndex[serieId] = series.size - 1
        return Resultado.Sucesso(series)
    }

    override suspend fun altera(alterada: SerieExercicio): Resultado<List<SerieExercicio>> {
        series[seriesIndex[alterada.serieId]!!] = alterada
        return Resultado.Sucesso(series)
    }

    companion object {
        private var sequenciaSeries = 0
        private fun incrementaSequencia(): Int {
            sequenciaSeries++
            return sequenciaSeries
        }
    }
}