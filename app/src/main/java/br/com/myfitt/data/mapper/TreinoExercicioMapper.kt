package br.com.myfitt.data.mapper

import br.com.myfitt.data.dto.TreinoExercicioDto
import br.com.myfitt.data.entity.TreinoExercicioEntity
import br.com.myfitt.data.entity.TreinoExercicioSerieEntity
import br.com.myfitt.domain.models.TreinoExercicioComNome

fun TreinoExercicioComNome.toSeriesEntity() = TreinoExercicioSerieEntity(
    treinoExercicioId = this.id,
    pesoKg = this.pesoKg,
    reps = this.repeticoes,
    segundosDescanso = this.segundosDescanso,
    id = this.serieId
)

fun TreinoExercicioComNome.toEntity() = TreinoExercicioEntity(
    id = this.id,
    treinoId = this.treinoId,
    exercicioId = this.exercicioId,
    posicao = this.posicao,
    observacao = this.observacao
)

fun TreinoExercicioDto.toDomain() = TreinoExercicioComNome(
    id = this.id,
    treinoId = this.treinoId,
    exercicioId = this.exercicioId,
    exercicioNome = this.exercicioNome,
    serieId = this.serieId,
    segundosDescanso = this.segundosDescanso ?: 0,
    series = this.series,
    posicao = this.posicao,
    pesoKg = this.pesoKg ?: 0f,
    repeticoes = this.repeticoes ?: 0,
    observacao = this.observacao,
    pesoKgUltimoTreino = this.pesoKgUltimoTreino,
    seriesUltimoTreino = this.seriesUltimoTreino,
    repeticoesUltimoTreino = this.repeticoesUltimoTreino

)