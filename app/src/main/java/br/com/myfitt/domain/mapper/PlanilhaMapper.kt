package br.com.myfitt.domain.mapper

import br.com.myfitt.data.entity.PlanilhaEntity
import br.com.myfitt.domain.models.Planilha

fun Planilha.toEntity(): PlanilhaEntity{
    return PlanilhaEntity(
        id = this.id,
        nome = this.nome
    )
}
fun PlanilhaEntity.toDomain(): Planilha{
    return Planilha(
        id = this.id,
        nome = this.nome
    )
}