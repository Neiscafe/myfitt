package br.com.myfitt.domain

import br.com.myfitt.domain.models.Exercicio

class ExerciseValidator(val exercise: Exercicio) {
    fun canBeCreated(): Exercicio {
        return exercise.apply {
            require(id == 0) { "Id = $id, deveria ser 0 para inserção." }
            require(nome.isNotEmpty()) { "Nome não pode ser vazio para inserção" }
            require(habilitado) { "habilitado deve ser true para inserção" }
            require(dataDesabilitado != null) { "dataDesabilitado deve ser null para inserção" }
        }
    }

    fun canBeVinculated(): Exercicio {
        return exercise.apply {
            require(id > 0) { "Id = $id, deve ser maior que zero para vinculação." }
            require(habilitado) { "habilitado deve ser true para vinculação" }
            require(dataDesabilitado != null) { "dataDesabilitado deve ser null para vinculação" }
        }
    }

    fun canBeUpdated(): Exercicio {
        return exercise.apply {
            require(id > 0) { "Id = $id, deve ser maior que zero para vinculação." }
        }
    }
}