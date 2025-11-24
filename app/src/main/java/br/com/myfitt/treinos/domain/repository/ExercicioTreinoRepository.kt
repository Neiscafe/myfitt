package br.com.myfitt.treinos.domain.repository

import br.com.myfitt.common.domain.ExercicioTemplate
import br.com.myfitt.common.domain.ExercicioTreino
import br.com.myfitt.common.domain.Resultado

interface ExercicioTreinoRepository {
    suspend fun adiciona(exercicioTreino: ExercicioTreino): Resultado<List<ExercicioTreino>>
    suspend fun lista(treinoId: Int): Resultado<List<ExercicioTreino>>
    suspend fun remove(exercicioTreino: ExercicioTreino): Resultado<List<ExercicioTreino>>
    suspend fun substitui(
        novo: ExercicioTreino
    ): Resultado<List<ExercicioTreino>>

    suspend fun reordena(
        exercicioTreino: ExercicioTreino,
        posicaoNova: Int
    ): Resultado<List<ExercicioTreino>>

    suspend fun adicionaPorTemplate(
        treinoId: Int,
        template: ExercicioTemplate
    ): Resultado<List<ExercicioTreino>>
}