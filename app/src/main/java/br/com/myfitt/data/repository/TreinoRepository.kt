package br.com.myfitt.data.repository

import br.com.myfitt.data.dao.TreinoDao
import br.com.myfitt.domain.mapper.toDomain
import br.com.myfitt.domain.mapper.toEntity
import br.com.myfitt.domain.models.Treino
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class TreinoRepository(private val dao: TreinoDao) {
    suspend fun insertTreino(treino: Treino) = withContext(
        Dispatchers.IO
    ) { dao.insert(treino.toEntity()) }

    fun getTreinosByPlanilha(planilhaId: Int): Flow<List<Treino>> =
        dao.getTreinosByPlanilha(planilhaId).map { it.map { it.toDomain() } }

    suspend fun deleteTreino(treino: Treino) = withContext(
        Dispatchers.IO
    ) { dao.delete(treino.toEntity()) }
}
