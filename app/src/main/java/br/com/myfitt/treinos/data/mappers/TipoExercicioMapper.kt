package br.com.myfitt.treinos.data.mappers

import br.com.myfitt.common.domain.TipoExercicio
import br.com.myfitt.treinos.data.entities.TipoExercicioEntity

fun TipoExercicioEntity.toDomain(): TipoExercicio {
    return TipoExercicio(
        tipoExercicioId = this.tipoExercicioId, descricao = this.descricao
    )
}

fun TipoExercicio.toEntity(): TipoExercicioEntity {
    return TipoExercicioEntity(
        tipoExercicioId = this.tipoExercicioId, descricao = this.descricao
    )
}

fun List<TipoExercicioEntity>.toDomain(): List<TipoExercicio> = map { it.toDomain() }