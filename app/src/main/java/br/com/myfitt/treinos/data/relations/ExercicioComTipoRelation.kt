package br.com.myfitt.treinos.data.relations

import androidx.room.Embedded
import androidx.room.Relation
import br.com.myfitt.treinos.data.entities.ExercicioEntity
import br.com.myfitt.treinos.data.entities.TipoExercicioEntity

data class ExercicioComTipoRelation(
    @Embedded val exercicio: ExercicioEntity,

    @Relation(
        parentColumn = "tipoExercicioId", entityColumn = "tipoExercicioId"
    ) val tipo: TipoExercicioEntity
)