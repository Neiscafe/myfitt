package br.com.myfitt.treinos.domain.usecase

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant

data class TickCronometro(
    val numero: Int = 0,
    val inicio: Instant? = null,
    val ativo: Boolean = false,
    val pausado: Boolean = false
)

class CronometroUseCase(val externalScope: CoroutineScope) {
    private var incrementaCronometroJob: Job? = null
    val contadorTicks = MutableStateFlow(TickCronometro())

    fun iniciaDescanso(fimUltimaSerie: Instant) {
        contadorTicks.update {
            it.copy(
                numero = 0, inicio = fimUltimaSerie, ativo = true, pausado = false
            )
        }
        contagemIncremental()
    }

    private fun contagemIncremental() {
        incrementaCronometroJob?.cancel()
        incrementaCronometroJob = externalScope.launch {
            while (true) {
                delay(1000L)
                contadorTicks.update {
                    it.copy(
                        numero = it.numero + 1
                    )
                }
            }
        }
    }

    fun pausa() {
        incrementaCronometroJob?.cancel()
        contadorTicks.update { it.copy(pausado = true) }
    }

    fun pausado() = contadorTicks.value.pausado
    fun ativo() = contadorTicks.value.ativo
    fun retoma() {
        contadorTicks.update { it.copy(pausado = false) }
        contagemIncremental()
    }

    fun iniciaExercicio() {
        contadorTicks.update {
            it.copy(
                numero = 0, inicio = Instant.now(), ativo = true, pausado = false
            )
        }
        contagemIncremental()
    }

    fun finaliza() {
        incrementaCronometroJob?.cancel()
        incrementaCronometroJob = null
        contadorTicks.update {
            it.copy(
                numero = 0, inicio = Instant.now(), ativo = false, pausado = false
            )
        }
    }
}