package br.com.myfitt.data.mapper

import br.com.myfitt.data.dto.ExercicioComTipoDto
import br.com.myfitt.data.dto.ExercicioComTipoDto2
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
fun ExercicioTipoEntity.toDomain(): TipoExercicio{
    return TipoExercicio(
        id = this.id,
        nome = this.nome
    )
}
fun ExercicioComTipoDto2.toDomain(): Exercicio {
    return Exercicio(
        id = this.exercicio.id,
        nome = this.exercicio.nome,
        habilitado = this.exercicio.habilitado,
        dataDesabilitado = this.exercicio.dataDesabilitado,
        tipo =null,
//        this.tipo?.let {
//            TipoExercicio(it.id, it.nome)
//        },
        posicao = this.position,
        fichaId = this.fichaId
    )

}

fun ExercicioComTipoDto.toDomain(): Exercicio {
    return Exercicio(
        id = this.id,
        nome = this.nome,
        habilitado = this.habilitado,
        dataDesabilitado = this.dataDesabilitado,
        tipo = if (exercicioTipoNome != null && exercicioTipoId != null) TipoExercicio(
            exercicioTipoId, exercicioTipoNome
        ) else null
    )
}