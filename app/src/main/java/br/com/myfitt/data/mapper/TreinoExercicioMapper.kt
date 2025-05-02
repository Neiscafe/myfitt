package br.com.myfitt.data.mapper

import br.com.myfitt.data.dao.TreinoExercicioDao
import br.com.myfitt.data.dto.TreinoExercicioDto
import br.com.myfitt.data.entity.TreinoExercicioEntity
import br.com.myfitt.domain.models.TreinoExercicioComNome


fun TreinoExercicioComNome.toEntity() = TreinoExercicioEntity(
    treinoId = this.treinoId,
    exercicioId = this.exercicioId,
    series = this.series,
    posicao = this.posicao,
    pesoKg = this.pesoKg,
    repeticoes = this.repeticoes,
    observacao = this.observacao
)

fun TreinoExercicioDto.toDomain() = TreinoExercicioComNome(
    treinoId = this.treinoId,
    exercicioId = this.exercicioId,
    exercicioNome = this.exercicioNome,
    series = this.series,
    posicao = this.posicao,
    pesoKg = this.pesoKg,
    repeticoes = this.repeticoes,
    observacao = this.observacao,
    pesoKgUltimoTreino = this.pesoKgUltimoTreino,
    seriesUltimoTreino = this.seriesUltimoTreino,
    repeticoesUltimoTreino = this.repeticoesUltimoTreino

)