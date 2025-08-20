package br.com.myfitt

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.graphics.drawable.IconCompat

class MyFittTimerService : Service() {
    companion object{
        const val EXTRA_TYPE_ACTION = "1"
        const val EXTRA_FINISH_EXERCISE = 1
        const val EXTRA_START_EXERCISE = 2
        const val EXTRA_START_REST = 3
    }
    private val onTimerTick: (Long) -> Unit = {}
    override fun onStartCommand(
        intent: Intent?, flags: Int, startId: Int
    ): Int {
        when (val it = intent?.extras?.getInt(EXTRA_TYPE_ACTION)) {
            EXTRA_FINISH_EXERCISE -> {
//                val notification = createCounterNotification()
//                ServiceCompat.startForeground(
//                    this, startId, notification,
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//                        ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE
//                    } else {
//                        0
//                    },
//                )
            }

            EXTRA_START_EXERCISE -> {}
            EXTRA_START_REST -> {}
            else -> error("Tipo inválido de extra ACTION_EXTRA: $it")
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return Binder()
    }

    private fun createCounterNotification(): Notification {
        val notificationChannel = NotificationChannel(
            "1", "Notificação de timer", NotificationManager.IMPORTANCE_HIGH
        )
        val stopCounterIcon = IconCompat.createWithResource(this, R.drawable.ic_launcher_foreground)
        val stopCounterIntent = Intent(
            this, MyFittTimerService::class.java
        )
        stopCounterIntent.putExtra(EXTRA_TYPE_ACTION, true)
        val stopCounterPendingIntent = PendingIntent.getService(
            this,
            0,
            stopCounterIntent,
            PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_MUTABLE
        )
        val stopCounterAction = NotificationCompat.Action.Builder(
            stopCounterIcon, "Stop current exercise", stopCounterPendingIntent
        ).build()
        return NotificationCompat.Builder(this, notificationChannel.id).setUsesChronometer(true)
            .setContentText("Cronometrando seu exercício...").addAction(stopCounterAction).build()
    }


}