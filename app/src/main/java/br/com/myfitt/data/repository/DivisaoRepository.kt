package br.com.myfitt.data.repository

import br.com.myfitt.data.dao.DivisaoDao
import br.com.myfitt.data.mapper.toDomain
import br.com.myfitt.data.mapper.toEntity
import br.com.myfitt.domain.models.Divisao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class DivisaoRepository(private val dao: DivisaoDao) {
    suspend fun getTodas(): List<Divisao> {
        return dao.getTodas().map { it.toDomain() }
    }

    fun getTodasFlow(): Flow<List<Divisao>> {
        return dao.getTodasFlow().map { it.map { it.toDomain() } }
    }

    suspend fun inserir(divisao: Divisao): Int = withContext(Dispatchers.IO) {
        return@withContext dao.insert(divisao.toEntity()).toInt()
    }
}