package br.com.myfitt.treinos.ui.screens.listaTreinos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.myfitt.common.domain.TipoExercicio
import br.com.myfitt.treinos.domain.repository.TipoExercicioRepository
import br.com.myfitt.treinos.domain.repository.TreinoRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate

private const val TAM_PAGINA = 40

class ListaTreinoViewModel(
    val treinoRepository: TreinoRepository, val tipoExercicioRepository: TipoExercicioRepository
) : ViewModel() {

    data class FiltroState(
        val tiposDisponiveis: List<TipoExercicio> = emptyList(),
        val dataSelecionada: LocalDate? = null,
        val carregandoTipos: Boolean = true,
        val tiposSelecionados: List<TipoExercicio> = emptyList()
    )

    private val _filtros = MutableStateFlow(FiltroState())
    val filtros = _filtros.asStateFlow()
    private val _state = MutableStateFlow(ListaTreinoState())
    val state = _state.asStateFlow()

    init {
        buscaTreinos()
    }

    fun limpaEventos() {
        _state.update { it.copy(erro = null) }
    }

    private fun carregaFiltrosTipos() {
        launchCoroutine {
            _filtros.update { it.copy(carregandoTipos = true) }
            val result = tipoExercicioRepository.lista(emTreino = true)
            result.erroOrNull?.let { erro ->
                _state.update { it.copy(erro = erro) }
                return@launchCoroutine
            }
            _filtros.update {
                it.copy(
                    tiposDisponiveis = result.dataOrNull ?: emptyList(), carregandoTipos = false
                )
            }
        }
    }


    private fun buscaTreinos(data: LocalDate? = null, tipos: TipoExercicio? = null) {
        startIO {
            if (state.value.ultimaPagina) {
                return@startIO
            }
            val paginaNova = state.value.pagina + 1
            val result1 = treinoRepository.listar(TAM_PAGINA, paginaNova)
            result1.erroOrNull?.let { it2 ->
                _state.update { it.copy(erro = it2) }
                return@startIO
            }
            val treinos = result1.dataOrNull!!
            val result2 = tipoExercicioRepository.doTreino(treinos.map { it.treinoId })
            result2.erroOrNull?.let { it2 ->
                _state.update { it.copy(erro = it2) }
                return@startIO
            }
            val tipoExercicios = result2.dataOrNull!!
            val itensTransformados = tipoExercicios.map {
                ListaTreinoModel(
                    treino = result1.dataOrNull?.firstOrNull { it2 -> it.treinoId == it2.treinoId }!!,
                    tipoExercicios = it.tipoExercicios
                )
            }
            _state.update {
                it.copy(
                    treinos = it.treinos + itensTransformados,
                    pagina = paginaNova,
                    ultimaPagina = itensTransformados.isEmpty()
                )
            }
        }
    }

    fun startIO(block: suspend CoroutineScope.() -> Unit) {
        _state.update { it.copy(carregando = true) }
        launchCoroutine {
            block(this)
            _state.update { it.copy(carregando = false) }
        }
    }

    fun launchCoroutine(
        dispatcher: CoroutineDispatcher = Dispatchers.IO, block: suspend CoroutineScope.() -> Unit
    ) {
        viewModelScope.launch(dispatcher) {
            block(this)
        }
    }
}