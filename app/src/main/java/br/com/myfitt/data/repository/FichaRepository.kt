package br.com.myfitt.data.repository

import br.com.myfitt.data.dao.FichaDao
import br.com.myfitt.data.mapper.toDomain
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FichaRepository(private val dao: FichaDao) {
    suspend fun getTodasByDivisao(divisaoId: Int) = withContext(Dispatchers.IO){
        dao.getTodasByDivisao(divisaoId).map { it.toDomain() }
    }
}