package br.com.myfitt.ui.screens.cronometro

import br.com.myfitt.MyFittTimerService

sealed class CronometroActions {
    class StartAndListenCronometro(
        val type: Int,
        val callback: MyFittTimerService.MyFittTimerCallback
    ) :
        CronometroActions()

    class StartListeningCronometro(val callback: MyFittTimerService.MyFittTimerCallback): CronometroActions()
    class StopListeningCronometro(): CronometroActions()
    class StopCronometro : CronometroActions()
}