package br.com.myfitt.common.domain

import java.time.LocalDateTime

data class SerieExercicio(
    val serieId: Int,
    val exercicioTreinoId: Int,
    val exercicioId: Int,
    val treinoId: Int,
    val dhInicioExecucao: LocalDateTime?,
    val dhFimExecucao: LocalDateTime?,
    val dhInicioDescanso: LocalDateTime?,
    val dhFimDescanso: LocalDateTime?,
    val duracaoSegundos: Int,
    val segundosDescanso: Int,
    val pesoKg: Int,
    val repeticoes: Int,
    val finalizado: Boolean
){
    val serieEmAndamento get()= this.dhFimExecucao==null
    val descansando get() = this.dhInicioExecucao==null
}