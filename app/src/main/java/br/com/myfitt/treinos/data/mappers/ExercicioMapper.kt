package br.com.myfitt.treinos.data.mappers

import br.com.myfitt.common.domain.Exercicio
import br.com.myfitt.treinos.data.entities.ExercicioEntity

fun ExercicioEntity.toDomain(): Exercicio {
    return Exercicio(
        exercicioId = this.exercicioId,
        nome = this.nome,
        observacao = this.observacao
    )
}

fun Exercicio.toEntity(): ExercicioEntity {
    return ExercicioEntity(
        exercicioId = this.exercicioId,
        nome = this.nome,
        observacao = this.observacao
    )
}

fun List<ExercicioEntity>.toDomain(): List<Exercicio> = map { it.toDomain() }
fun List<Exercicio>.toEntity(): List<ExercicioEntity> = map { it.toEntity() }