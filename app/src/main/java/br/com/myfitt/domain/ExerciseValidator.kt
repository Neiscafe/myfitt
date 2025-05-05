package br.com.myfitt.domain

import br.com.myfitt.domain.models.Exercicio

class ExerciseValidator(val exercise: Exercicio) {
    fun canBeCreated(): Exercicio {
        return exercise.apply {
            require(id == 0) { "Id de $exercise deveria ser 0 para inserção." }
            require(nome.isNotEmpty()) { "Nome de $exercise não pode ser vazio para inserção" }
            require(habilitado) { "habilitado de $exercise deve ser true para inserção" }
            require(dataDesabilitado == null) { "dataDesabilitado de $exercise deve ser null para inserção" }
        }
    }

    fun canBeVinculatedToFicha(): Exercicio {
        return exercise.apply {
            require(id > 0) { "Id de $exercise deve ser maior que zero para vinculação." }
            require(habilitado) { "habilitado de $exercise deve ser true para vinculação" }
            require(dataDesabilitado == null) { "dataDesabilitado de $exercise deve ser null para vinculação" }
            require(fichaId==0){"fichaId de $exercise deve ser 0 para vinculação!"}
        }
    }

    fun canBeVinculatedToTreino(): Exercicio {
        return exercise.apply {
            require(id > 0) { "Id de $exercise deve ser maior que zero para vinculação." }
            require(habilitado) { "habilitado de $exercise deve ser true para vinculação" }
            require(dataDesabilitado == null) { "dataDesabilitado de $exercise deve ser null para vinculação" }
        }
    }

    fun canBeUpdated(): Exercicio {
        return exercise.apply {
            require(id > 0) { "Id = $id, deve ser maior que zero para atualização." }
        }
    }
}