package br.com.myfitt.treinos.ui.screens.seriesExercicio

import br.com.myfitt.common.domain.SerieExercicio
import br.com.myfitt.treinos.domain.model.SeriesDestaqueExercicio

data class SeriesExercicioState(
    val series: List<SerieExercicio> = emptyList(),
    val nomeExercicio: String = "",
    val serieDestaques: List<SeriesDestaqueExercicio>? = null,
    val observacaoExercicio: String = "",
    val carregando: Boolean = false,
    val erro: String? = null
) {
    fun resetaEventos() = this.copy(erro = null)
}