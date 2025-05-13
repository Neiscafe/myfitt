package br.com.myfitt.domain.validate

import br.com.myfitt.domain.models.Exercicio

object PosicaoValidator {
    fun podeAumentar(exercicio: Exercicio, lista: List<Any>) = exercicio.posicao < lista.size-1
    fun podeDiminuir(exercicio: Exercicio) = exercicio.posicao > 0
}