package br.com.myfitt.treinos.data.mappers

import br.com.myfitt.common.domain.SerieExercicio
import br.com.myfitt.treinos.data.entities.SerieExercicioEntity

fun SerieExercicioEntity.toDomain(): SerieExercicio {
    return SerieExercicio(
        serieId = this.serieId,
        exercicioTreinoId = this.exercicioTreinoId,
        exercicioId = this.exercicioId,
        treinoId = this.treinoId,
        dhInicioExecucao = this.dhInicioExecucao,
        dhFimExecucao = this.dhFimExecucao,
        dhInicioDescanso = this.dhInicioDescanso,
        dhFimDescanso = this.dhFimDescanso,
        duracaoSegundos = this.duracaoSegundos,
        segundosDescanso = this.segundosDescanso,
        pesoKg = this.pesoKg,
        repeticoes = this.repeticoes,
        finalizado = this.finalizado
    )
}

fun SerieExercicio.toEntity(): SerieExercicioEntity {
    return SerieExercicioEntity(
        serieId = this.serieId,
        exercicioTreinoId = this.exercicioTreinoId,
        exercicioId = this.exercicioId,
        treinoId = this.treinoId,
        dhInicioExecucao = this.dhInicioExecucao,
        dhFimExecucao = this.dhFimExecucao,
        dhInicioDescanso = this.dhInicioDescanso,
        dhFimDescanso = this.dhFimDescanso,
        duracaoSegundos = this.duracaoSegundos,
        segundosDescanso = this.segundosDescanso,
        pesoKg = this.pesoKg,
        repeticoes = this.repeticoes,
        finalizado = this.finalizado
    )
}

fun List<SerieExercicioEntity>.toDomain(): List<SerieExercicio> = map { it.toDomain() }
fun List<SerieExercicio>.toEntity(): List<SerieExercicioEntity> = map { it.toEntity() }