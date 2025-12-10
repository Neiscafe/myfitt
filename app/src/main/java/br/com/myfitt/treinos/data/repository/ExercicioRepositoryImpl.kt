package br.com.myfitt.treinos.data.repository

import br.com.myfitt.common.domain.Exercicio
import br.com.myfitt.common.domain.Resultado
import br.com.myfitt.common.domain.Treino
import br.com.myfitt.treinos.domain.repository.ExercicioRepository
import kotlinx.coroutines.delay

class ExercicioRepositoryImpl : ExercicioRepository {
    private val exercicios: List<Exercicio> = listOf(
        Exercicio(1, "Supino reto", null, tipoExercicioId = 1, tipoExercicioDescr = "Peito"),
        Exercicio(
            2, "Passada", "Observação 1", tipoExercicioId = 2, tipoExercicioDescr = "Quadriceps"
        ),
        Exercicio(3, "Rosquinha", "Testes", tipoExercicioId = 3, tipoExercicioDescr = "Bíceps"),
    )

    override suspend fun lista(pesquisa: String): Resultado<List<Exercicio>> {
        delay(500)
        return Resultado.Sucesso(exercicios.filter { it.nome.contains(pesquisa, true) })
    }

    override suspend fun busca(exercicioId: Int): Resultado<Exercicio> {
        delay(500L)
        return exercicios.find { it.exercicioId == exercicioId }?.let { Resultado.Sucesso(it) }
            ?: Resultado.Erro("Exercício não encontrado!")
    }
}