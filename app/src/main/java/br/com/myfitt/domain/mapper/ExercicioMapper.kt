package br.com.myfitt.domain.mapper

import br.com.myfitt.data.entity.ExercicioComTipoDto
import br.com.myfitt.data.entity.ExercicioEntity
import br.com.myfitt.data.entity.ExercicioTipoEntity
import br.com.myfitt.domain.models.Exercicio
import br.com.myfitt.domain.models.TipoExercicio

fun Exercicio.toEntity(): ExercicioEntity {
    return ExercicioEntity(
        id = this.id,
        nome = this.nome,
        habilitado = this.habilitado,
        dataDesabilitado = this.dataDesabilitado,
        exercicioTipoId = this.tipo?.id
    )
}

fun ExercicioComTipoDto.toDomain(): Exercicio {
    return Exercicio(
        id = this.id,
        nome = this.nome,
        habilitado = this.habilitado,
        dataDesabilitado = this.dataDesabilitado,
        tipo = if (exercicioTipoNome != null && exercicioTipoId != null)
            TipoExercicio(exercicioTipoId, exercicioTipoNome) else null
    )
}