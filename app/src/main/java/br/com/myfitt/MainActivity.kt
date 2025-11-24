package br.com.myfitt

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import br.com.myfitt.treinos.ui.screens.exerciciosTreino.ExerciciosTreinoNavigation
import br.com.myfitt.treinos.ui.screens.listaExercicios.ListaExerciciosNavigation
import br.com.myfitt.treinos.ui.screens.menuPrincipal.MenuPrincipalNavigation
import br.com.myfitt.treinos.ui.screens.seriesExercicio.SeriesExercicioNavigation
import br.com.myfitt.treinos.ui.theme.MyFittTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

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
}