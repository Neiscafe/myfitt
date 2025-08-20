package br.com.myfitt.data.entity

import androidx.room.Embedded
import androidx.room.Relation
import br.com.myfitt.domain.models.Exercicio

data class ExercicioWithTipo (
    @Embedded val exercicio: ExercicioEntity,
    @Relation(parentColumn = "exercicioTipoId", entityColumn = "id") val tipo: ExercicioTipoEntity?
)