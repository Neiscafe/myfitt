package br.com.myfitt.data.repository

import br.com.myfitt.data.dao.FichaDao
import br.com.myfitt.data.dao.FichaExercicioDao
import br.com.myfitt.data.mapper.toDomain
import br.com.myfitt.data.mapper.toEntity
import br.com.myfitt.domain.models.Exercicio
import br.com.myfitt.domain.models.Ficha
import br.com.myfitt.domain.models.TipoExercicio
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class FichaRepository(
    private val fichaDao: FichaDao, private val fichaExercicioDao: FichaExercicioDao
) {
    private lateinit var selectedFichaCache: Ficha
    fun getTodasByDivisaoFlow(divisaoId: Int) =
        fichaDao.getTodasByDivisaoFlow(divisaoId).map { it.map { it.toDomain() } }

    suspend fun getTodasByDivisao(divisaoId: Int) = withContext(Dispatchers.IO) {
        fichaDao.getTodasByDivisao(divisaoId).map { it.toDomain() }
    }

    suspend fun insert(ficha: Ficha): Int = withContext(Dispatchers.IO) {
        fichaDao.insert(ficha.toEntity()).toInt()
    }

    fun getFichaByIdFlow(fichaId: Int): Flow<Ficha> {
        return fichaExercicioDao.getFichaExercicioByIdFlow(fichaId).map {
            val first = it.first()
            val ficha = Ficha(first.fichaId, divisaoId = first.divisaoId, first.fichaNome)
            val exercicios = mutableListOf<Exercicio>()
            it.forEach {
                exercicios.add(
                    Exercicio(
                        it.exercicioNome,
                        it.exercicioId,
                        tipo = TipoExercicio(it.exercicioTipoId, it.exercicioTipoNome),
                        habilitado = true,
                        dataDesabilitado = null,
                        posicao = it.position
                    )
                )
            }
            ficha.copy(exercicios = exercicios).also { selectedFichaCache = ficha }
        }
    }

    suspend fun removeExercise(exercise: Exercicio) {

    }

    suspend fun increasePosition(exercise: Exercicio) {
        if (exercise.posicao == selectedFichaCache.exercicios.size - 1) return
        fichaExercicioDao.switchPositions(
            selectedFichaCache.id,
            exercise.id,
            selectedFichaCache.exercicios.first { it.posicao == exercise.posicao + 1 }.id,
        )
    }

    suspend fun decreasePosition(exercise: Exercicio) {
        if (exercise.posicao == 0) return
        fichaExercicioDao.switchPositions(
            selectedFichaCache.id,
            selectedFichaCache.exercicios.first { it.posicao == exercise.posicao - 1 }.id,
            exercise.id,
        )
    }

    suspend fun addExercise(ficha: Ficha, exercise: Exercicio) {}
}