package br.com.myfitt.data.repository

import br.com.myfitt.data.dao.DivisaoDao
import br.com.myfitt.data.mapper.toDomain
import br.com.myfitt.domain.models.Divisao

class DivisaoRepository(private val dao: DivisaoDao) {
    suspend fun getTodas(): List<Divisao> {
        return dao.getTodas().map { it.toDomain() }
    }
}