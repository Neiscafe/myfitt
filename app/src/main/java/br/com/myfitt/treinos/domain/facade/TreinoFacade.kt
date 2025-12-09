package br.com.myfitt.treinos.domain.facade

import br.com.myfitt.common.domain.ExercicioTreino
import br.com.myfitt.common.domain.Resultado
import br.com.myfitt.common.domain.SerieExercicio
import br.com.myfitt.common.domain.Treino
import br.com.myfitt.common.domain.map
import br.com.myfitt.treinos.domain.repository.ExercicioTreinoRepository
import br.com.myfitt.treinos.domain.repository.SeriesRepository
import br.com.myfitt.treinos.domain.repository.TreinoRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

data class TreinoFacadeState(
    val treino: Treino,
    val exerciciosTreino: List<ExercicioTreino>,
    val series: List<SerieExercicio>,
) {
    fun seriesParaExercicio(exercicioTreinoId: Int) =
        series.filter { it.exercicioTreinoId == exercicioTreinoId }
}

class TreinoFacade(
    private val treinoRepository: TreinoRepository,
    private val exercicioTreinoRepository: ExercicioTreinoRepository,
    private val seriesRepository: SeriesRepository,
) {

    suspend fun buscar(treinoId: Int): Resultado<TreinoFacadeState> {
        return coroutineScope {
            buscaTreinoAtualizado(treinoId)
        }
    }

    private suspend fun CoroutineScope.buscaTreinoAtualizado(treinoId: Int): Resultado<TreinoFacadeState> {
        val treino = async {
            treinoRepository.busca(treinoId)
        }
        val exerciciosTreino = async {
            exercicioTreinoRepository.lista(treinoId)
        }
        val seriesTreino = async {
            seriesRepository.todasDoTreino(treinoId)
        }
        awaitAll(treino, exerciciosTreino, seriesTreino)
        val treinoResult = treino.await()
        val exercicioTreinoResult = exerciciosTreino.await()
        val seriesTreinoResult = seriesTreino.await()
        return if (!treinoResult.sucesso) {
            treinoResult.map()
        } else if (!exercicioTreinoResult.sucesso) {
            exercicioTreinoResult.map()
        } else if (!seriesTreinoResult.sucesso) {
            seriesTreinoResult.map()
        } else
            Resultado.Sucesso(
                TreinoFacadeState(
                    treino = treinoResult.dataOrNull!!,
                    exerciciosTreino = exercicioTreinoResult.dataOrNull!!,
                    series = seriesTreinoResult.dataOrNull!!,
                )
            )
    }
}