package br.com.myfitt.data.dto

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import br.com.myfitt.data.entity.ExercicioEntity
import br.com.myfitt.data.entity.ExercicioTipoEntity
import br.com.myfitt.domain.models.Exercicio

data class ExercicioComTipoDto2(
    @Embedded val exercicio: ExercicioEntity,
    @Relation(
        entity = ExercicioEntity::class, parentColumn = "exercicioTipoId", entityColumn = "id",
    ) val tipo: ExercicioTipoEntity? = null,
    val position: Int,
    val fichaId: Int,
)