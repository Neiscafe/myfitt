package br.com.myfitt.treinos.ui

import br.com.myfitt.common.utils.differenceSeconds
import br.com.myfitt.common.utils.instant
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

data class TickCronometro(
    val numero: Int = 0,
    val inicio: Instant? = null,
    val descansoAtivo: Boolean = false,
    val serieAtiva: Boolean = false,
)

class CronometroFacade(val externalScope: CoroutineScope) {
    private var incrementaCronometroJob: Job? = null
    private val _ticksCronometro = MutableStateFlow(TickCronometro())
    val ticksCronometro = _ticksCronometro.asStateFlow()

    fun iniciaDescanso(fimUltimaSerie: LocalDateTime) {
        val instant = fimUltimaSerie.instant()
        _ticksCronometro.update {
            it.copy(
                numero = differenceSeconds(LocalDateTime.now(), fimUltimaSerie),
                inicio = instant,
                serieAtiva = false,
                descansoAtivo = true
            )
        }
        contagemIncremental()
    }

    private fun contagemIncremental() {
        incrementaCronometroJob?.cancel()
        incrementaCronometroJob = externalScope.launch {
            while (true) {
                delay(100L)
                _ticksCronometro.update {
                    it.copy(
                        numero = it.numero + 1
                    )
                }
            }
        }
    }

    fun pausa() {
        incrementaCronometroJob?.cancel()
        _ticksCronometro.update { it.copy(serieAtiva = false, descansoAtivo = false) }
    }


    fun iniciaExercicio(inicio: LocalDateTime = LocalDateTime.now()) {
        val instant = inicio.toInstant(ZoneOffset.ofHours(-3))
        _ticksCronometro.update {
            it.copy(
                numero = differenceSeconds(LocalDateTime.now(), inicio),
                inicio = instant,
                descansoAtivo = false,
                serieAtiva = true
            )
        }
        contagemIncremental()
    }
}