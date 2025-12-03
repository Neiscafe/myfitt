package br.com.myfitt.treinos.data.repository

import br.com.myfitt.common.domain.ExercicioTemplate
import br.com.myfitt.common.domain.ExercicioTreino
import br.com.myfitt.common.domain.Resultado
import br.com.myfitt.treinos.domain.repository.ExercicioTreinoRepository
import kotlinx.coroutines.delay

class ExercicioTreinoRepositoryImpl : ExercicioTreinoRepository {
    private val exerciciosTreino = mutableListOf<ExercicioTreino>()

    override suspend fun adiciona(exercicioTreino: ExercicioTreino): Resultado<List<ExercicioTreino>> {
        delay(500L)
        val exercicioTreino = exercicioTreino.copy(exercicioTreinoId = sequenciaExercicioTreino())
        exerciciosTreino.add(exercicioTreino)
        return Resultado.Sucesso(exerciciosTreino)
    }

    override suspend fun lista(treinoId: Int): Resultado<List<ExercicioTreino>> {
        delay(1500L)
        return Resultado.Sucesso(exerciciosTreino)
    }

    override suspend fun remove(exercicioTreino: ExercicioTreino): Resultado<List<ExercicioTreino>> {
        delay(500L)
        exerciciosTreino.removeAt(exercicioTreino.ordem - 1)
        return Resultado.Sucesso(exerciciosTreino)
    }

    override suspend fun substitui(
        novo: ExercicioTreino
    ): Resultado<List<ExercicioTreino>> {
        delay(500L)
        val atual = exerciciosTreino[novo.ordem - 1]
        val removeResult = remove(atual)
        if (!removeResult.sucesso) {
            return removeResult
        }
        return adiciona(novo)
    }

    override suspend fun busca(exercicioTreinoId: Int): Resultado<ExercicioTreino> {
        delay(200L)
        return exerciciosTreino.firstOrNull { it.exercicioTreinoId == exercicioTreinoId }
            ?.let { Resultado.Sucesso(it) } ?: Resultado.Erro("Exercício não encontrado!")
    }

    override suspend fun reordena(
        exercicioTreino: ExercicioTreino, posicaoNova: Int
    ): Resultado<List<ExercicioTreino>> {
        delay(500L)
        val modificado = exerciciosTreino[posicaoNova - 1]
        exerciciosTreino[posicaoNova - 1] = exercicioTreino.copy(ordem = posicaoNova)
        exerciciosTreino[exercicioTreino.ordem - 1] = modificado
        return Resultado.Sucesso(exerciciosTreino)
    }

    override suspend fun adicionaPorTemplate(
        treinoId: Int, template: ExercicioTemplate
    ): Resultado<List<ExercicioTreino>> {
        TODO("Not yet implemented")
    }

    companion object {
        private var contadorExercicioTreino = 0
        private fun sequenciaExercicioTreino(): Int {
            contadorExercicioTreino++
            return contadorExercicioTreino
        }
    }
}