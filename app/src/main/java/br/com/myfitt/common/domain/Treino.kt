package br.com.myfitt.common.domain

import java.time.Duration
import java.time.LocalDateTime
import kotlin.math.abs

data class Treino(
    val treinoId: Int,
    val dhCriado: LocalDateTime = LocalDateTime.now(),
    val tipoTreinoId: Int? = null,
    val tipoTreinoDescr: String? = null,
    val dhInicio: LocalDateTime? = null,
    val dhFim: LocalDateTime? = null
) {
    val segundosDuracao
        get() = if (dhInicio == null || dhFim == null) 0 else abs(
            Duration.between(
                dhInicio,
                dhFim
            ).seconds
        ).toInt()
}