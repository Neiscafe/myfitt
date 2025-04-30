package br.com.myfitt.data.mapper

import br.com.myfitt.data.entity.FichaEntity
import br.com.myfitt.domain.models.Ficha

fun FichaEntity.toDomain(): Ficha {
    return Ficha(
        id = this.id,
        divisaoId = this.divisaoId,
        nome = this.nome
    )
}