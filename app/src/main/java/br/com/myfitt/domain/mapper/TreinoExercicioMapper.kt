package br.com.myfitt.domain.mapper

import br.com.myfitt.data.dao.TreinoExercicioDao
import br.com.myfitt.data.entity.TreinoExercicioCrossRef
import br.com.myfitt.domain.models.TreinoExercicioComNome


fun TreinoExercicioComNome.toEntity() = TreinoExercicioCrossRef(
    treinoId = this.treinoId,
    exercicioId = this.exercicioId,
    series = this.series,
    posicao = this.posicao,
    pesoKg = this.pesoKg,
    repeticoes = this.repeticoes,
    observacao = this.observacao
)

fun TreinoExercicioDao.TreinoExercicioDto.toDomain() = TreinoExercicioComNome(
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