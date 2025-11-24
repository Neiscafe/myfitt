package br.com.myfitt.treinos.data.repository

import br.com.myfitt.common.domain.Exercicio
import br.com.myfitt.common.domain.Resultado
import br.com.myfitt.treinos.domain.repository.ExercicioRepository
import kotlinx.coroutines.delay

class ExercicioRepositoryImpl : ExercicioRepository {
    private val exercicios: List<Exercicio> = listOf(
        Exercicio(1, "Supino reto", null),
        Exercicio(2, "Passada", null),
        Exercicio(3, "Rosquinha", null),
    )

    override suspend fun lista(pesquisa: String): Resultado<List<Exercicio>> {
        delay(500)
        return Resultado.Sucesso(exercicios.filter { it.nome.contains(pesquisa, true) })
    }
}