package br.com.myfitt.treinos.domain.repository

import br.com.myfitt.common.domain.ExercicioTreino
import br.com.myfitt.common.domain.SerieExercicio
import br.com.myfitt.common.domain.Treino
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class TreinoFlowState(
    val treino: Treino,
    val exerciciosTreino: List<ExercicioTreino>,
    val series: List<SerieExercicio>
) {
    fun seriesParaExercicio(exercicioTreinoId: Int) =
        series.filter { it.exercicioTreinoId == exercicioTreinoId }
}

class TreinoFlowProxy(
    val treinoId: Int,
    private val treinoRepository: TreinoRepository,
    private val exercicioTreinoRepository: ExercicioTreinoRepository,
    private val seriesRepository: SeriesRepository,
    private val scope: CoroutineScope,
) {
    private val _treino = MutableStateFlow<TreinoFlowState?>(null)
    val treino = _treino.asStateFlow()

    init {
        scope.launch(Dispatchers.IO) {
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
        }
    }

}