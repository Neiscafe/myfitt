package br.com.myfitt.data.mapper

import br.com.myfitt.data.entity.DivisaoEntity
import br.com.myfitt.domain.models.Divisao

fun DivisaoEntity.toDomain(): Divisao{
    return Divisao(
        id = this.id,
        nome = this.nome
    )
}