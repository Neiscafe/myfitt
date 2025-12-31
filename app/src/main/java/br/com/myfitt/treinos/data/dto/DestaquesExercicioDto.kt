package br.com.myfitt.treinos.data.dto

import br.com.myfitt.common.domain.SerieExercicio
import java.time.LocalDateTime

data class DestaquesExercicioDto(
    val categoria: String,
    val serieId: Int = 0,
    val exercicioTreinoId: Int,
    val exercicioId: Int,
    val treinoId: Int,
    val dhInicioExecucao: LocalDateTime?,
    val dhFimExecucao: LocalDateTime?,
    val dhInicioDescanso: LocalDateTime?,
    val dhFimDescanso: LocalDateTime?,
    val duracaoSegundos: Int,
    val segundosDescanso: Int,
    val pesoKg: Float,
    val repeticoes: Int,
    val finalizado: Boolean, val umRmEstimado: Float
) {
    fun serie(): SerieExercicio {
        this.let {
            return SerieExercicio(
                serieId = it.serieId,
                exercicioTreinoId = it.exercicioTreinoId,
                exercicioId = it.exercicioId,
                treinoId = it.treinoId,
                dhInicioExecucao = it.dhInicioExecucao,
                dhFimExecucao = it.dhFimExecucao,
                dhInicioDescanso = it.dhInicioDescanso,
                dhFimDescanso = it.dhFimDescanso,
                duracaoSegundos = it.duracaoSegundos,
                segundosDescanso = it.segundosDescanso,
                pesoKg = it.pesoKg,
                repeticoes = it.repeticoes,
                finalizado = it.finalizado
            )
        }
    }
}