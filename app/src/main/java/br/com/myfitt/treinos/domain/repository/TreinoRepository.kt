package br.com.myfitt.treinos.domain.repository

import br.com.myfitt.common.domain.Resultado
import br.com.myfitt.common.domain.TipoExercicio
import br.com.myfitt.common.domain.Treino
import java.time.LocalDate

interface TreinoRepository {
    suspend fun listar(
        tamPagina: Int = 0,
        pagina: Int = 0,
        filtroTipos: List<TipoExercicio>? = null,
        filtroData: LocalDate? = null
    ): Resultado<List<Treino>>

    suspend fun criar(treino: Treino): Resultado<Treino>
    suspend fun busca(treinoId: Int): Resultado<Treino>
    suspend fun altera(novo: Treino): Resultado<Treino>
    suspend fun ativo(): Resultado<Treino>
}