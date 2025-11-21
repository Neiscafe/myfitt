package br.com.myfitt.treinos.domain.repository

import br.com.myfitt.common.domain.ExercicioTemplate
import br.com.myfitt.common.domain.ExercicioTreino
import br.com.myfitt.common.domain.Resultado

interface ExercicioTreinoRepository {
    suspend fun adiciona(exercicioTreino: ExercicioTreino): Resultado<ExercicioTreino>
    suspend fun lista(treinoId: Int): Resultado<List<ExercicioTreino>>
    suspend fun remove(exercicioTreino: ExercicioTreino): Resultado<ExercicioTreino>
    suspend fun substitui(velho: ExercicioTreino, novo: ExercicioTreino): Resultado<ExercicioTreino>
    suspend fun reordena(
        exercicioTreino: ExercicioTreino,
        posicaoNova: Int
    ): Resultado<ExercicioTreino>

    suspend fun adicionaPorTemplate(
        treinoId: Int,
        template: ExercicioTemplate
    ): Resultado<List<ExercicioTreino>>
}