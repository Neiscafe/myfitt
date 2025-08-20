package br.com.myfitt.data.repository

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi
import br.com.myfitt.MyFittTimerService

class CronometroRepository(val context: Context) {
    private var serviceConnection: ServiceConnection? = null
    fun startTimerAndListen(timerType: Int, callback: MyFittTimerService.MyFittTimerCallback) {
        bindMyFittTimer(timerType, callback)
    }

    fun startListening(callback: MyFittTimerService.MyFittTimerCallback) {
        bindMyFittTimer(null, callback)
    }


    fun stopListening() {
        unbindMyFittTimer()
    }

    fun stopCurrentTimer() {
        stopMyFittTimer()
    }

    fun isMyFittServiceRunning(): Boolean {
        return MyFittTimerService.isRunning
    }

    private fun bindMyFittTimer(
        extraType: Int? = null, callback: MyFittTimerService.MyFittTimerCallback
    ): Boolean {
        if (serviceConnection != null) {
            return false
        }
        serviceConnection = createConnection(callback)
        Intent(context, MyFittTimerService::class.java).apply {
            extraType?.let {
                putExtra(
                    MyFittTimerService.EXTRA_TYPE_ACTION, extraType
                )
            }
            context.startForegroundService(this)
            context.bindService(this, serviceConnection!!, Context.BIND_IMPORTANT)
        }
        return true
    }

    private fun createConnection(callback: MyFittTimerService.MyFittTimerCallback): ServiceConnection {
        return object : ServiceConnection {
            @RequiresApi(Build.VERSION_CODES.S)
            override fun onServiceConnected(
                name: ComponentName?, service: IBinder?
            ) {
                (service as MyFittTimerService.MyFittTimerBinder).setCallback(callback)
            }

            override fun onServiceDisconnected(name: ComponentName?) {}
        }
    }

    private fun stopMyFittTimer(
    ): Boolean {
        if (!unbindMyFittTimer()) {
            return false
        }
        Intent(context, MyFittTimerService::class.java).apply {
            context.stopService(this)
        }
        return true
    }

    private fun unbindMyFittTimer(): Boolean {
        if (serviceConnection == null) return false
        context.unbindService(serviceConnection!!)
        return true
    }

}