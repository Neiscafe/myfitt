package br.com.myfitt.treinos.data.relations

import androidx.room.Embedded
import androidx.room.Relation
import br.com.myfitt.treinos.data.entities.TipoTreinoEntity
import br.com.myfitt.treinos.data.entities.TreinoEntity

data class TreinoComTipoRelation(
    @Embedded val treino: TreinoEntity,

    @Relation(
        parentColumn = "tipoTreinoId",
        entityColumn = "tipoTreinoId"
    )
    val tipo: TipoTreinoEntity
)
