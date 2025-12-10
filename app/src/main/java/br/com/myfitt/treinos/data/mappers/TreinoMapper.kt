package br.com.myfitt.treinos.data.mappers

import br.com.myfitt.common.domain.Treino
import br.com.myfitt.treinos.data.entities.TreinoEntity
import br.com.myfitt.treinos.data.relations.TreinoComTipoRelation

fun TreinoComTipoRelation.toDomain(): Treino {
    return Treino(
        treinoId = this.treino.treinoId,
        tipoTreinoId = this.treino.tipoTreinoId, // Vem da entidade principal
        tipoTreinoDescr = this.tipo.descricao,   // Vem da entidade relacionada (JOIN)
        dhCriado = this.treino.dhCriado,
        dhFim = this.treino.dhFim,
        dhInicio = this.treino.dhInicio,
    )
}

fun Treino.toEntity(): TreinoEntity {
    return TreinoEntity(
        treinoId = this.treinoId,
        tipoTreinoId = this.tipoTreinoId, // Só salvamos o ID
        dhCriado = this.dhCriado,
        dhInicio = this.dhInicio,
        dhFim = this.dhFim,
    )
}

fun List<TreinoComTipoRelation>.toDomain(): List<Treino> = map { it.toDomain() }