package br.com.myfitt.data.dto

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import br.com.myfitt.data.entity.ExercicioEntity
import br.com.myfitt.data.entity.FichaEntity
import br.com.myfitt.data.entity.FichaExercicioEntity

data class FichaExercicioDto(
    @Embedded val ficha: FichaEntity, @Relation(
        entity = ExercicioEntity::class,
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            FichaExercicioEntity::class, parentColumn = "fichaId", entityColumn = "exercicioId"
        )
    ) val exercicios: List<ExercicioComTipoDto2>
)