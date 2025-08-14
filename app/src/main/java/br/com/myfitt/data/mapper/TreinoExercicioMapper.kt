package br.com.myfitt.data.mapper

import br.com.myfitt.data.dto.TreinoExercicioDto
import br.com.myfitt.data.entity.TreinoExercicioEntity
import br.com.myfitt.data.entity.TreinoExercicioSerieEntity
import br.com.myfitt.domain.models.ExercicioTreino

fun ExercicioTreino.toEntity() = TreinoExercicioEntity(
    id = this.id,
    treinoId = this.treinoId,
    exercicioId = this.id,
    posicao = this.posicao,
    observacao = this.observacao
)