package br.com.myfitt.treinos.ui.screens.seriesExercicio

import br.com.myfitt.common.domain.SerieExercicio

data class SeriesExercicioState(
    val series: List<SerieExercicio> = emptyList(),
    val nomeExercicio: String = "",
    val observacaoExercicio: String = "",
    val carregando: Boolean = false,
    val erro: String? = null
) {
    fun resetaEventos() = this.copy(erro = null)
}