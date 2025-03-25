package br.com.myfitt.domain.mapper

import br.com.myfitt.data.entity.ExercicioEntity
import br.com.myfitt.domain.models.Exercicio

fun Exercicio.toEntity(): ExercicioEntity {
    return ExercicioEntity(
        id = this.id,
        nome = this.nome,
        habilitado = this.habilitado,
        dataDesabilitado = this.dataDesabilitado
    )
}

fun ExercicioEntity.toDomain(): Exercicio {
    return Exercicio(
        id = this.id,
        nome = this.nome,
        habilitado = this.habilitado,
        dataDesabilitado = this.dataDesabilitado
    )
}