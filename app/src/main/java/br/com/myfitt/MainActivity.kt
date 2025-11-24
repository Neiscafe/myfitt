package br.com.myfitt

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import br.com.myfitt.treinos.ui.screens.ExerciciosTreinoScreen
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
                        startDestination = uriExerciciosTreino,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        routeExerciciosTreino(navController)
                    }
                }
            }
        }
    }

    private fun NavGraphBuilder.routeExerciciosTreino(
        navController: NavController,
    ) {
        composable(
            route = uriExerciciosTreino,
            arguments = exercicioTreinoArgs
        ) {
            ExerciciosTreinoScreen(
                treinoId = it.arguments?.getInt(exercicioTreinoArg1) ?: 0,
                voltar = navController::popBackStack,
                irParaSeries = { navController.navigate(uriSeriesExercicio, null) },
                irParaSubstituicao = {},
            )
        }
    }

    companion object {
        const val exercicioTreinoArg1 = "treinoId"
        const val uriExerciciosTreino = "exerciciosTreino/{$exercicioTreinoArg1}"
        const val uriExerciciosTreino = "exerciciosTreino?$exercicioTreinoArg1=1"
        val exercicioTreinoArgs =
            listOf(navArgument(exercicioTreinoArg1) { type = NavType.IntType })
    }
}