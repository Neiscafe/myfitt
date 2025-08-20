package br.com.myfitt.ui.screens

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import br.com.myfitt.MyFittTimerService
import br.com.myfitt.ui.theme.MyFittTheme
import java.time.Duration

@Composable
fun CronometroTreinoScreen(modifier: Modifier = Modifier) {
    var counterText by remember { mutableStateOf("00m00s") }
    var buttonControl by remember { mutableStateOf(true) }
    val context = LocalContext.current
    Column(
        Modifier
            .fillMaxHeight()
            .fillMaxWidth()
    ) {
        OutlinedTextField(
            value = "Supino transversal", keyboardOptions = KeyboardOptions(
                autoCorrectEnabled = false, imeAction = ImeAction.Search
            ), onValueChange = {

            }, modifier = Modifier.fillMaxWidth(), trailingIcon = { }, singleLine = true
        )

        Text(counterText)
        if (buttonControl) {
            Button({
                Intent(context, MyFittTimerService::class.java).apply {
                    putExtra(
                        MyFittTimerService.EXTRA_TYPE_ACTION,
                        MyFittTimerService.EXTRA_START_EXERCISE
                    )
                    context.bindService(this, object : ServiceConnection {
                        var myFittService: MyFittTimerService? = null

                        @RequiresApi(Build.VERSION_CODES.S)
                        override fun onServiceConnected(
                            name: ComponentName?,
                            service: IBinder?
                        ) {
                            myFittService =
                                (service as MyFittTimerService.MyFittTimerBinder).getMyFittTimerService()
                            myFittService?.onTimerTick = { elapsedSeconds ->
                                counterText = "${elapsedSeconds / 60}m${elapsedSeconds}s"
                            }
                        }

                        override fun onServiceDisconnected(name: ComponentName?) {
                            myFittService = null
                        }
                    }, Context.BIND_IMPORTANT)
                }
                buttonControl = !buttonControl
            }) { Text("Iniciar") }
        } else {
            Button({
                Intent(context, MyFittTimerService::class.java).apply {
                    putExtra(
                        MyFittTimerService.EXTRA_TYPE_ACTION,
                        MyFittTimerService.EXTRA_START_REST
                    )
                    context.startService(this)
                }
                buttonControl = !buttonControl
            }) { Text("Descansar") }
        }
    }
}

@Preview
@Composable
private fun CronometroTreinoScreenPreview() {
    MyFittTheme {
        CronometroTreinoScreen()
    }
}