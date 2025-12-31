package br.com.myfitt.treinos.domain.model

import br.com.myfitt.common.domain.SerieExercicio

data class SeriesDestaqueExercicio(
    val categoriaDestaque: String,
    val serie: SerieExercicio,
    val umRepMaxAbsoluto: Float,
){
    val umRepMaxPorcentagem get() = umRepMaxAbsoluto*100f
}
