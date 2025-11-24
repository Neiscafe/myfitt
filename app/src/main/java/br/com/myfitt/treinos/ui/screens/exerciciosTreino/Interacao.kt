package br.com.myfitt.treinos.ui.screens.exerciciosTreino

import br.com.myfitt.common.domain.ExercicioTreino

sealed class Interacao {
    data class Adicionar(val novo: ExercicioTreino) : Interacao()
    data class Substituir(val novo: ExercicioTreino) : Interacao()
    data class Remover(val exercicioTreino: ExercicioTreino) : Interacao()
    data class Reposicionar(val reposicionar: ExercicioTreino, val posicao: Int) : Interacao()
}
