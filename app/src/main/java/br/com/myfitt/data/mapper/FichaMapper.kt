package br.com.myfitt.data.mapper

import br.com.myfitt.data.entity.FichaEntity
import br.com.myfitt.data.entity.FichaExercicioDto
import br.com.myfitt.domain.models.Ficha

fun FichaEntity.toDomain(): Ficha {
    return Ficha(
        id = this.id,
        divisaoId = this.divisaoId,
        nome = this.nome
    )
}
fun Ficha.toEntity(): FichaEntity {
    return FichaEntity(
        id = this.id,
        divisaoId = this.divisaoId,
        nome = this.nome
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