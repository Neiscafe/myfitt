package br.com.myfitt

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import androidx.core.graphics.drawable.IconCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MyFittTimerService : Service() {
    /**
     * If you implement onStartCommand(), it is your responsibility to stop the service when its work is complete by calling stopSelf() or stopService().
     */
    private val binder = MyFittTimerBinder()

    interface MyFittTimerCallback {
        fun onTick(elapsedSeconds: Int, timerType: Int)
    }

    inner class MyFittTimerBinder() : Binder() {
        fun setCallback(timerCallback: MyFittTimerCallback) {
            this@MyFittTimerService.onTimerTick = timerCallback
        }
    }

    var timerType = 0
    var onTimerTick: MyFittTimerCallback? = null
    override fun onStartCommand(
        intent: Intent?, flags: Int, startId: Int
    ): Int {
        val it = intent?.extras?.getInt(EXTRA_TYPE_ACTION)
        val notification = when (it) {
            EXTRA_FINISH_NOTIFICATION -> {
//                val notification = createCounterNotification()
//                ServiceCompat.startForeground(
//                    this, startId, notification,
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//                        ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE
//                    } else {
//                        0
//                    },
//                )
                createCounterNotification()
            }

            EXTRA_START_EXERCISE -> {
                createCounterNotification()
            }

            EXTRA_START_REST -> {
                createCounterNotification()
            }

            EXTRA_CURRENT_ELAPSED -> {
                createCounterNotification()
            }

            else -> error("Tipo inválido de extra ACTION_EXTRA: $it")
        }
        ServiceCompat.startForeground(this, 23, notification, 0)
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(23, notification)
        timerType = it
        isRunning = true
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        isRunning = false
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return binder
    }

    override fun onCreate() {
        super.onCreate()
        CoroutineScope(Dispatchers.IO).launch {
            var seconds = 0
            delay(500L)
            while ((System.currentTimeMillis() % 1000L) != 0L) {

            }
            while (true) {
                delay(1000L)
                seconds++
                onTimerTick?.onTick(seconds, timerType)
            }
        }
    }

    override fun onUnbind(intent: Intent?): Boolean {
        this.onTimerTick = null
        return super.onUnbind(intent)
    }

    companion object {
        var isRunning = false
        const val EXTRA_TYPE_ACTION = "1"
        const val EXTRA_FINISH_NOTIFICATION = 1
        const val EXTRA_START_EXERCISE = 2
        const val EXTRA_START_REST = 3
        const val EXTRA_CURRENT_ELAPSED = 4
    }


    private fun createCounterNotification(): Notification {
        val notificationChannel = NotificationChannel(
            "1", "Notificação de timer", NotificationManager.IMPORTANCE_HIGH
        )
        getSystemService(NotificationManager::class.java).createNotificationChannel(
            notificationChannel
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
        return NotificationCompat.Builder(this, "1")
            .setUsesChronometer(true)
            .setOngoing(true)
            /**
             * Lembrar de desabilitar a notificação manualmente por causa do setOngoing()
             */
            .setSmallIcon(IconCompat.createWithResource(this, R.drawable.ic_launcher_foreground))
            .setContentText("Cronometrando seu exercício...")
            .setContentInfo("asdasdasda")
            .setContentTitle("blah blah blah")
//            .addAction(stopCounterAction)
            .build()
    }


}