package br.com.myfitt.data.repository

import br.com.myfitt.data.dao.PlanilhaDao
import br.com.myfitt.data.entity.PlanilhaEntity
import br.com.myfitt.domain.mapper.toDomain
import br.com.myfitt.domain.mapper.toEntity
import br.com.myfitt.domain.models.Planilha
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class PlanilhaRepository(private val dao: PlanilhaDao) {
    suspend fun insertPlanilha(planilha: Planilha) = withContext(Dispatchers.IO) {
        dao.insert(
            planilha.toEntity()
        )
    }
    fun getAllPlanilhas(): Flow<List<Planilha>> = dao.getAllPlanilhas().map { it.map { it.toDomain() } }
    suspend fun deletePlanilha(planilha: Planilha) = withContext(Dispatchers.IO) {
        dao.delete(
            planilha.toEntity()
        )
    }
}