package br.com.myfitt.treinos.data.mappers

import br.com.myfitt.common.domain.ExercicioTreino
import br.com.myfitt.treinos.data.entities.ExercicioTreinoEntity

fun ExercicioTreinoEntity.toDomain(): ExercicioTreino {
    return ExercicioTreino(
        exercicioTreinoId = this.exercicioTreinoId,
        exercicioId = this.exercicioId,
        treinoId = this.treinoId,
        ordem = this.ordem,
        compostoId = this.compostoId,
        nomeExercicio = this.nomeExercicio
    )
}

fun ExercicioTreino.toEntity(): ExercicioTreinoEntity {
    return ExercicioTreinoEntity(
        exercicioTreinoId = this.exercicioTreinoId,
        exercicioId = this.exercicioId,
        treinoId = this.treinoId,
        ordem = this.ordem,
        compostoId = this.compostoId,
        nomeExercicio = this.nomeExercicio
    )
}

fun List<ExercicioTreinoEntity>.toDomain(): List<ExercicioTreino> = map { it.toDomain() }
fun List<ExercicioTreino>.toEntity(): List<ExercicioTreinoEntity> = map { it.toEntity() }