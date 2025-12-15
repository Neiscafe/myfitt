package br.com.myfitt.common.domain.builder

import br.com.myfitt.common.domain.SerieExercicio
import java.time.Duration
import java.time.LocalDateTime
import kotlin.math.abs

class GerenciaSerieTreino private constructor() {
    companion object {
        fun criar(
            exercicioId: Int,
            exercicioTreinoId: Int,
            treinoId: Int,
            dhInicioDescanso: LocalDateTime?
        ): GerenciaSerieTreino {
            val builder = GerenciaSerieTreino()
            return builder.iniciar(exercicioId, exercicioTreinoId, treinoId, 0, dhInicioDescanso)
        }

        fun continuar(
            serieExercicio: SerieExercicio
        ): GerenciaSerieTreino {
            val builder = GerenciaSerieTreino()
            builder.iniciar(
                serieExercicio.exercicioId,
                serieExercicio.exercicioTreinoId,
                serieExercicio.treinoId,
                serieExercicio.serieId,
                serieExercicio.dhInicioDescanso
            )
            serieExercicio.dhInicioDescanso?.let {
                builder.descanso(it)
            }
            serieExercicio.dhInicioExecucao?.let {
                builder.execucao(serieExercicio.pesoKg, it)
            }
            serieExercicio.dhFimExecucao?.let {
                builder.fimExecucao(it)
            }
            serieExercicio.repeticoes.takeIf { it > 0 }?.let {
                builder.repeticoes(it)
            }
            return builder
        }
    }

    fun get() = instancia.copy()
    fun descanso(dhInicioDescanso: LocalDateTime = LocalDateTime.now()): GerenciaSerieTreino {
        _instancia = _instancia?.copy(
            dhInicioDescanso = dhInicioDescanso
        )
        return this
    }

    fun fimExecucao(dhFimExecucao: LocalDateTime = LocalDateTime.now()): GerenciaSerieTreino {
        val now = dhFimExecucao
        _instancia = _instancia?.copy(
            dhFimExecucao = now,
            duracaoSegundos = abs(Duration.between(now, instancia.dhInicioExecucao).seconds.toInt())
        )
        return this
    }

    fun serieId(serieId: Int): GerenciaSerieTreino {
        _instancia = _instancia?.copy(
            serieId = serieId
        )
        return this
    }

    fun execucao(
        pesoKg: Float, dhInicioExecucao: LocalDateTime = LocalDateTime.now()
    ): GerenciaSerieTreino {
        _instancia = _instancia?.copy(
            pesoKg = pesoKg,
            dhFimDescanso = dhInicioExecucao,
            segundosDescanso = instancia.dhInicioDescanso?.let {
                abs(
                    (Duration.between(
                        dhInicioExecucao, it
                    ).seconds).toInt()
                )
            } ?: 0,
            dhInicioExecucao = dhInicioExecucao,
        )
        return this
    }


    private val instancia get() = _instancia!!
    private var _instancia: SerieExercicio? = null
    private fun iniciar(
        exercicioId: Int,
        exercicioTreinoId: Int,
        treinoId: Int,
        serieId: Int,
        dhInicioDescanso: LocalDateTime?
    ): GerenciaSerieTreino {
        _instancia = SerieExercicio(
            serieId = serieId,
            exercicioTreinoId = exercicioTreinoId,
            exercicioId = exercicioId,
            treinoId = treinoId,
            dhInicioExecucao = null,
            dhFimExecucao = null,
            dhInicioDescanso = dhInicioDescanso,
            dhFimDescanso = null,
            duracaoSegundos = 0,
            segundosDescanso = 0,
            pesoKg = 0f,
            repeticoes = 0,
            finalizado = false
        )
        return this
    }

    fun repeticoes(repeticoes: Int): GerenciaSerieTreino {
        _instancia = _instancia?.copy(
            repeticoes = repeticoes, finalizado = true
        )
        return this
    }
}