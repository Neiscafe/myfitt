package br.com.myfitt.treinos.domain.facade

import br.com.myfitt.common.domain.Exercicio
import br.com.myfitt.common.domain.ExercicioTreino
import br.com.myfitt.common.domain.Resultado
import br.com.myfitt.common.domain.SerieExercicio
import br.com.myfitt.common.domain.Treino
import br.com.myfitt.common.domain.map
import br.com.myfitt.treinos.domain.repository.ExercicioRepository
import br.com.myfitt.treinos.domain.repository.ExercicioTreinoRepository
import br.com.myfitt.treinos.domain.repository.SeriesRepository
import br.com.myfitt.treinos.domain.repository.TreinoRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

data class TreinoFacadeState(
    val treino: Treino? = null,
    val exerciciosTreino: List<ExercicioTreino> = emptyList(),
    val exercicios: List<Exercicio> = emptyList(),
    val series: List<SerieExercicio> = emptyList(),
) {
    fun seriesParaExercicio(exercicioTreinoId: Int) =
        series.filter { it.exercicioTreinoId == exercicioTreinoId }
}

class TreinoFacade(
    private val treinoRepository: TreinoRepository,
    private val exercicioTreinoRepository: ExercicioTreinoRepository,
    private val exercicioRepository: ExercicioRepository,
    private val seriesRepository: SeriesRepository,
) {
    suspend fun buscarAtivo(): Resultado<TreinoFacadeState> {
        return coroutineScope {
            buscaTreinoAtualizado()
        }
    }

    suspend fun buscar(treinoId: Int): Resultado<TreinoFacadeState> {
        return coroutineScope {
            buscaTreinoAtualizado(treinoId)
        }
    }

    private suspend fun CoroutineScope.buscaTreinoAtualizado(treinoId: Int? = null): Resultado<TreinoFacadeState> {
        val treino = if (treinoId == null) {
            treinoRepository.ativo()
        } else treinoRepository.busca(treinoId)
        if (!treino.sucesso) {
            return treino.map()
        }
        if (treino.dataOrNull == null) {
            return Resultado.Sucesso(TreinoFacadeState())
        }
        val treinoId = treino.dataOrNull!!.treinoId
        val exerciciosTreino = async {
            exercicioTreinoRepository.lista(treinoId)
        }
        val exercicios = async {
            exercicioRepository.doTreino(treinoId)
        }
        val seriesTreino = async {
            seriesRepository.todasDoTreino(treinoId)
        }
        awaitAll(exerciciosTreino, seriesTreino, exercicios)
        val exercicioTreinoResult = exerciciosTreino.await()
        val seriesTreinoResult = seriesTreino.await()
        val exerciciosResult = exercicios.await()
        return if (!exercicioTreinoResult.sucesso) {
            exercicioTreinoResult.map()
        } else if (!seriesTreinoResult.sucesso) {
            seriesTreinoResult.map()
        } else if (!exerciciosResult.sucesso) {
            exerciciosResult.map()
        } else Resultado.Sucesso(
            TreinoFacadeState(
                treino = treino.dataOrNull,
                exerciciosTreino = exercicioTreinoResult.dataOrNull!!,
                series = seriesTreinoResult.dataOrNull!!,
                exercicios = exerciciosResult.dataOrNull!!,
            )
        )
    }
}