package br.com.myfitt.treinos.ui.screens.listaExercicios

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import org.koin.androidx.compose.koinViewModel

object ListaExerciciosNavigation {
    const val route = "listaExercicios"
    fun composeNavigation(builder: NavGraphBuilder, navController: NavController) {
        builder.composable(
            route = route
        ) {
            ListaExerciciosScreen()
        }
    }
}

@Composable
fun ListaExerciciosScreen(viewModel: ListaExerciciosViewModel = koinViewModel()) {

}