package br.com.myfitt

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
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
import br.com.myfitt.treinos.ui.screens.detalhesExercicio.DetalhesExercicioNavigation
import br.com.myfitt.treinos.ui.screens.exerciciosTreino.ExerciciosTreinoNavigation
import br.com.myfitt.treinos.ui.screens.listaExercicios.ListaExerciciosNavigation
import br.com.myfitt.treinos.ui.screens.listaTreinos.ListaTreinoNavigation
import br.com.myfitt.treinos.ui.screens.menuPrincipal.MenuPrincipalNavigation
import br.com.myfitt.treinos.ui.screens.seriesExercicio.SeriesExercicioNavigation
import br.com.myfitt.treinos.ui.theme.MyFittTheme

class MainActivity : ComponentActivity() {
    private val lifecycleCallbacks: MutableList<(Lifecycle.Event) -> Unit> = mutableListOf()
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { _ -> }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        ProcessLifecycleOwner.get().lifecycle.addObserver(object : LifecycleEventObserver {
            override fun onStateChanged(
                source: LifecycleOwner, event: Lifecycle.Event
            ) {
                lifecycleCallbacks.forEach {
                    it.invoke(event)
                }
            }
        })

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
                        ListaTreinoNavigation.composeNavigation(this, navController)
                        DetalhesExercicioNavigation.composeNavigation(this, navController)
                    }
                }
            }
        }
    }


    fun addLifecycleCallback(callback: (Lifecycle.Event) -> Unit) {
        lifecycleCallbacks.add(callback)
    }

    fun removeLifecycleCallback(callback: (Lifecycle.Event) -> Unit) {
        lifecycleCallbacks.remove(callback)
    }

    private fun checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this, Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
}