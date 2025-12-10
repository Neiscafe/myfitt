package br.com.myfitt.treinos.data.mappers

import br.com.myfitt.common.domain.Exercicio
import br.com.myfitt.treinos.data.entities.ExercicioEntity
import br.com.myfitt.treinos.data.relations.ExercicioComTipoRelation

fun ExercicioComTipoRelation.toDomain(): Exercicio {
    return Exercicio(
        exercicioId = this.exercicio.exercicioId,
        nome = this.exercicio.nome,
        observacao = this.exercicio.observacao,
        tipoExercicioId = this.exercicio.tipoExercicioId,
        tipoExercicioDescr = this.tipo.descricao
    )
}

fun Exercicio.toEntity(): ExercicioEntity {
    return ExercicioEntity(
        exercicioId = this.exercicioId,
        tipoExercicioId = this.tipoExercicioId, // Apenas o ID é salvo na tabela de exercícios
        nome = this.nome,
        observacao = this.observacao
    )
}

fun List<ExercicioComTipoRelation>.toDomain(): List<Exercicio> = map { it.toDomain() }