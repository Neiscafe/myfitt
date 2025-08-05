package br.com.myfitt.data.mapper

import br.com.myfitt.data.dao.FichaDao
import br.com.myfitt.data.dto.FichaExercicioDto
import br.com.myfitt.data.entity.FichaEntity
import br.com.myfitt.domain.models.Exercicio
import br.com.myfitt.domain.models.Ficha
import br.com.myfitt.domain.models.TipoExercicio

fun FichaEntity.toDomain(): Ficha {
    return Ficha(
        id = this.id, nome = this.nome
    )
}

//fun FichaExercicioDto.toDomain(): Ficha {
//    return Ficha(id = this.ficha.id,
//        divisaoId = this.ficha.divisaoId,
//        nome = this.ficha.nome,
//        exercicios = this.exercicios.map {
//            Exercicio(
//                nome = it.exercicio.nome,
//                id = it.exercicio.id,
//                posicao = it.position,
//                tipo = it.tipo?.let { TipoExercicio(it.id, it.nome) },
//                habilitado = it.exercicio.habilitado,
//                dataDesabilitado = it.exercicio.dataDesabilitado
//            )
//        })
//}

fun Ficha.toEntity(): FichaEntity {
    return FichaEntity(
        id = this.id, nome = this.nome
    )
}
//fun FichaExercicioDto.toDomain(): Ficha{
//    return Ficha(
//        id = this.ficha.id,
//        divisaoId = this.ficha.divisaoId,
//        nome = this.ficha.nome,
//        exercicios = this.exercicios
//    )
//}