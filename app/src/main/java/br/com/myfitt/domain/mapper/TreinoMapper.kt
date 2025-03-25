package br.com.myfitt.domain.mapper

import br.com.myfitt.data.entity.TreinoEntity
import br.com.myfitt.domain.models.Treino

fun Treino.toEntity(): TreinoEntity {
    return TreinoEntity(
        id = this.id,
        planilhaId = this.planilhaId,
        nome = this.nome,
        data = this.data,
    )
}

fun TreinoEntity.toDomain(): Treino {
    return Treino(
        id = this.id,
        planilhaId = this.planilhaId,
        nome = this.nome,
        data = this.data,
    )
}