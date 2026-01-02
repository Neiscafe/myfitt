package br.com.myfitt.common.domain

import br.com.myfitt.common.utils.differenceSeconds
import java.time.LocalDateTime

data class SerieExercicio(
    val serieId: Int,
    val exercicioTreinoId: Int,
    val exercicioId: Int,
    val treinoId: Int,
    val dhInicioExecucao: LocalDateTime? = LocalDateTime.now(),
    val dhFimExecucao: LocalDateTime? = null,
    val dhInicioDescanso: LocalDateTime? = null,
    val dhFimDescanso: LocalDateTime? = dhInicioExecucao,
    val duracaoSegundos: Int = 0,
    val segundosDescanso: Int = if (dhInicioDescanso == null || dhFimDescanso == null) 0 else differenceSeconds(
        dhInicioDescanso, dhFimDescanso
    ),
    val pesoKg: Float = 0f,
    val repeticoes: Int = 0,
    val finalizado: Boolean = dhFimExecucao != null
) {
    fun iniciaExecucao(
        pesoKg: Float,
        dhInicioDescanso: LocalDateTime?,
        dhFimDescanso: LocalDateTime = LocalDateTime.now()
    ): SerieExercicio {
        return this.copy(
            pesoKg = pesoKg,
            dhInicioExecucao = dhFimDescanso,
            dhInicioDescanso = dhInicioDescanso,
            dhFimDescanso = dhFimDescanso,
            segundosDescanso = dhInicioDescanso?.let { differenceSeconds(it, dhFimDescanso) } ?: 0)
    }

    fun finaliza(dhFimExecucao: LocalDateTime): SerieExercicio {
        return this.copy(
            dhFimExecucao = dhFimExecucao,
            duracaoSegundos = if (dhInicioExecucao == null) 0 else differenceSeconds(
                this.dhInicioExecucao, dhFimExecucao
            ),
            finalizado = true
        )
    }

    val serieEmAndamento get() = this.dhInicioExecucao != null && this.dhFimExecucao == null
    val descansando get() = this.dhInicioExecucao == null
}