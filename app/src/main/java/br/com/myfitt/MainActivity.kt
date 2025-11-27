package br.com.myfitt

import android.Manifest
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import br.com.myfitt.treinos.CronometroService
import br.com.myfitt.treinos.ui.screens.exerciciosTreino.ExerciciosTreinoNavigation
import br.com.myfitt.treinos.ui.screens.listaExercicios.ListaExerciciosNavigation
import br.com.myfitt.treinos.ui.screens.menuPrincipal.MenuPrincipalNavigation
import br.com.myfitt.treinos.ui.screens.seriesExercicio.CronometroState
import br.com.myfitt.treinos.ui.screens.seriesExercicio.SeriesExercicioNavigation
import br.com.myfitt.treinos.ui.theme.MyFittTheme

class MainActivity : ComponentActivity() {
    private var cronometroService: CronometroService? = null
    private var cronometroConnection: ServiceConnection? = null
    var lifecycleObserver: LifecycleEventObserver? = null
    private fun conectaCronometroService(
        iniciaCronometro: (CronometroService) -> Unit
    ) = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            val binder = service as CronometroService.CronometroBinder
            cronometroService = binder.getService()
            cronometroService?.let {
                iniciaCronometro(it)
            }
        }

        override fun onServiceDisconnected(arg0: ComponentName) {

        }
    }.also { cronometroConnection = it }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { _ -> }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        checkPermissions()
        setContent {
            MyFittTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = MenuPrincipalNavigation.route,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        MenuPrincipalNavigation.composeNavigation(this, navController)
                        ExerciciosTreinoNavigation.composeNavigation(this, navController)
                        SeriesExercicioNavigation.composeNavigation(this, navController)
                        ListaExerciciosNavigation.composeNavigation(this, navController)
                    }
                }
            }
        }
    }

    fun activityOnForegroundListener(callback: (MainActivity) -> Unit) {
        ProcessLifecycleOwner.get().lifecycle.addObserver(object : LifecycleEventObserver {
            override fun onStateChanged(
                source: LifecycleOwner,
                event: Lifecycle.Event
            ) {
                if (source == this && event == Lifecycle.Event.ON_RESUME) {
                    callback(this@MainActivity)
                }
            }
        })
    }

    private fun checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

//    private fun observeLifecycle() {
//        lifecycleObserver?.let {
//            ProcessLifecycleOwner.get().lifecycle.removeObserver(it)
//        }
//    }

    fun configuraCronometroBackground(
        vaiParaSegundoPlano: () -> CronometroState,
        voltaParaPrimeiroPlano: (CronometroState) -> Unit
    ) {
        lifecycleObserver = object : LifecycleEventObserver {
            override fun onStateChanged(
                source: LifecycleOwner,
                event: Lifecycle.Event
            ) {
                if (event == Lifecycle.Event.ON_PAUSE) {
                    val connection = conectaCronometroService { cronometroService ->
                        cronometroService.iniciaCronometro(vaiParaSegundoPlano())
                    }
                    Intent(this@MainActivity, CronometroService::class.java).also { intent ->
                        bindService(
                            intent,
                            connection,
                            BIND_AUTO_CREATE
                        )
                    }
                } else if (event == Lifecycle.Event.ON_RESUME) {
                    cronometroService?.paraCronometro()?.let {
                        voltaParaPrimeiroPlano(it)
                        cronometroConnection?.let {
                            unbindService(it)
                        }
                        lifecycleObserver?.let {
                            ProcessLifecycleOwner.get().lifecycle.removeObserver(it)
                        }
                    }
                }
            }
        }
        ProcessLifecycleOwner.get().lifecycle.addObserver(lifecycleObserver!!)
    }
}