package br.com.myfitt.domain.validate

import br.com.myfitt.domain.models.Ficha

object FichaValidator {
    fun canBeInserted(ficha: Ficha): Ficha {
        return ficha.apply {
            require(id == 0) { "O Id $id da ficha $ficha deveria ser 0 para inserção!" }
            require(divisaoId > 0) { "O divisaoId da ficha $ficha deveria ser maior que 0 para inserção!" }
            require(nome.isNotEmpty()) { "O nome da ficha $ficha não deve ser nulo para inserção!" }
            require(exercicios.isEmpty()) { "A ficha $ficha não deve ter exercícios para a inserção!" }
        }
    }
}