package br.com.myfitt.treinos.domain.repository

import br.com.myfitt.common.domain.Resultado
import br.com.myfitt.common.domain.SerieExercicio

interface SeriesRepository {
    suspend fun lista(): Resultado<List<SerieExercicio>>
    suspend fun cria(serie: SerieExercicio): Resultado<List<SerieExercicio>>
    suspend fun altera(alterada: SerieExercicio): Resultado<List<SerieExercicio>>
}