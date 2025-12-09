package br.com.myfitt.treinos.data.mappers

import br.com.myfitt.common.domain.Treino
import br.com.myfitt.treinos.data.entities.TreinoEntity

fun TreinoEntity.toDomain(): Treino {
    return Treino(
        treinoId = this.treinoId,
        dhCriado = this.dhCriado,
        finalizado = this.finalizado
    )
}

fun Treino.toEntity(): TreinoEntity {
    return TreinoEntity(
        treinoId = this.treinoId,
        dhCriado = this.dhCriado,
        finalizado = this.finalizado
    )
}

// Extensões para Listas
fun List<TreinoEntity>.toDomain(): List<Treino> = map { it.toDomain() }
fun List<Treino>.toEntity(): List<TreinoEntity> = map { it.toEntity() }