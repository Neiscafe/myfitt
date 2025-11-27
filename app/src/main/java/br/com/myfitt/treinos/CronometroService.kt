package br.com.myfitt.treinos

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import br.com.myfitt.MainActivity
import br.com.myfitt.R
import br.com.myfitt.treinos.ui.screens.seriesExercicio.CronometroState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CronometroService : Service() {
    companion object {
        const val NOTIFICATION_ID = 1
        const val CHANNEL_ID = "cronometro_channel"
        const val ACTION_STOP = "ACTION_STOP"
    }

    inner class CronometroBinder : Binder() {
        fun getService(): CronometroService = this@CronometroService
    }

    private var cronometrosJob: Job? = null
    private val binder = CronometroBinder()
    private var tempoAtual: Int = 0
    override fun onBind(intent: Intent?): IBinder? {
        return binder
    }

    override fun onCreate() {
        super.onCreate()
        criaNotificationChannel()
    }


    override fun onStartCommand(
        intent: Intent?, flags: Int, startId: Int
    ): Int {
        return START_STICKY
    }

    fun paraCronometro(): CronometroState {
        return CronometroState(segundos = tempoAtual)
    }

    fun iniciaCronometro(state: CronometroState) {
        startForeground(NOTIFICATION_ID, criaNotificacao(tempoAtual))
        cronometrosJob = CoroutineScope(Dispatchers.Default).launch {
            while (true) {
                delay(1000L)
                tempoAtual++
                Log.d("Teste", "${tempoAtual} de service")
                atualizaNotificacao(tempoAtual)
            }
        }
    }

    private fun atualizaNotificacao(cronometro: Int) {
//        val notificationManager =
//            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//        notificationManager.notify(NOTIFICATION_ID, criaNotificacao(cronometro))
    }

    private fun criaNotificacao(tempo: Int): Notification {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setUsesChronometer(true).setWhen(System.currentTimeMillis()-tempo*1000).setShowWhen(true)
            .setTicker("Teste")
//            .setContentTitle("${tempo / 60}m${tempo % 60}s")
//            .setContentText("Contando o tempo...")
            .setSmallIcon(R.drawable.timer_24dp_000000_fill0_wght400_grad0_opsz24)
            .setContentIntent(pendingIntent)
            .setOnlyAlertOnce(true)
            .setOngoing(true)
            .setForegroundServiceBehavior(NotificationCompat.FOREGROUND_SERVICE_IMMEDIATE)
            .build()
    }

    override fun onDestroy() {
        cronometrosJob?.cancel()
        tempoAtual = 0
        super.onDestroy()
    }

    private fun criaNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID, "Canal de cronômetros", NotificationManager.IMPORTANCE_LOW
            ).apply { description = "Notificações de crônometros ativos" }
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }
}