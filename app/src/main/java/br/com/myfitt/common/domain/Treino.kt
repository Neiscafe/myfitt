package br.com.myfitt.common.domain

import java.time.Duration
import java.time.LocalDateTime
import kotlin.math.abs

data class Treino(
    val treinoId: Int,
    val dhCriado: LocalDateTime = LocalDateTime.now(),
    val dhInicio: LocalDateTime? = null,
    val dhFim: LocalDateTime? = null
) {
    val segundosDuracao
        get() = if (dhInicio == null) 0 else abs(
            Duration.between(
                dhInicio,
                (dhFim ?: LocalDateTime.now())
            ).seconds
        ).toInt()
}