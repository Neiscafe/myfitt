package br.com.myfitt.treinos.domain.repository

import br.com.myfitt.common.domain.Resultado
import br.com.myfitt.common.domain.SerieExercicio
import br.com.myfitt.treinos.domain.model.SeriesDestaqueExercicio

interface SeriesRepository {
    suspend fun seriesDestaqueExercicio(exercicioId: Int, treinoId: Int): Resultado<List<SeriesDestaqueExercicio>>
    suspend fun todasDoTreino(treinoId: Int): Resultado<List<SerieExercicio>>
    suspend fun lista(exercicioTreinoId: Int): Resultado<List<SerieExercicio>>
    suspend fun cria(serie: SerieExercicio): Resultado<List<SerieExercicio>>
    suspend fun altera(alterada: SerieExercicio): Resultado<List<SerieExercicio>>
    suspend fun busca(serieId: Int): Resultado<SerieExercicio>
}