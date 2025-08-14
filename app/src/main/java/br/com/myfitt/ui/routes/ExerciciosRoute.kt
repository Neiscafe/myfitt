package br.com.myfitt.ui.routes

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import br.com.myfitt.MainActivity.Companion.TREINO_ID
import br.com.myfitt.ui.routes.Routes.LISTA_EXERCICIOS_TREINO
import br.com.myfitt.ui.screens.exerciciosTreino.ListaExerciciosTreinoScreen

fun NavGraphBuilder.listaExerciciosTreinoRoute(navController: NavController) {
    composable(
        route = LISTA_EXERCICIOS_TREINO.name + "/{$TREINO_ID}", arguments = listOf(navArgument(TREINO_ID) {
            type = NavType.IntType
        })
    ) {
        ListaExerciciosTreinoScreen(
            it.arguments?.getInt(TREINO_ID)!!
        )
    }
}