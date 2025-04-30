package br.com.myfitt

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.core.os.bundleOf
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import br.com.myfitt.ui.screens.CriarTreinoScreen
import br.com.myfitt.ui.screens.ExercicioScreen
import br.com.myfitt.ui.screens.PlanilhaScreen
import br.com.myfitt.ui.screens.Routes.*
import br.com.myfitt.ui.screens.TreinoScreen
import br.com.myfitt.ui.theme.MyFittTheme

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
                        startDestination = PLANILHAS.name,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable(route = PLANILHAS.name) {
                            PlanilhaScreen {
                                navController.navigate(TREINOS.name + "/$it")
                            }
                        }
                        composable(
                            route = CRIAR_TREINO.name + "/{planilhaId}",
                            arguments = listOf(navArgument("planilhaId") {
                                type = NavType.IntType
                            })
                        ) {
                            CriarTreinoScreen(it.arguments!!.getInt("planilhaId"),
                                navigate = { navController.navigate(EXERCICIOS.name + "/$it"){
                                    this.popUpTo(TREINOS.name) { this.inclusive = false }
                                } })
                        }
                        composable(
                            route = TREINOS.name + "/{planilhaId}",
                            arguments = listOf(navArgument("planilhaId") {
                                type = NavType.IntType
                            })
                        ) {
                            TreinoScreen(
                                it.arguments?.getInt("planilhaId")
                                    ?: throw IllegalArgumentException()
                            ) {
                                navController.navigate(CRIAR_TREINO.name + "/$it")
                            }
                        }

                        composable(
                            route = EXERCICIOS.name + "/{treinoId}",
                            arguments = listOf(navArgument("treinoId") {
                                type = NavType.IntType
                            })
                        ) {
                            ExercicioScreen(
                                it.arguments?.getInt("treinoId") ?: throw IllegalArgumentException()
                            )
                        }
                    }
                }
            }
        }
    }
}