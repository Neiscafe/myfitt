package br.com.myfitt.common.domain

import java.time.LocalDateTime

data class SerieExercicio(
    val serieId: Int,
    val exercicioTreinoId: Int,
    val exercicioId: Int,
    val dhInicioSerie: LocalDateTime,
    val dhFimSerie: LocalDateTime,
    val duracaoSegundos: Int,
    val segundosDescanso: Int,
    val pesoKg: Int,
    val repeticoes: Int
)