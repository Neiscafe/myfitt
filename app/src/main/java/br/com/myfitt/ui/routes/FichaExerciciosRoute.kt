package br.com.myfitt.ui.routes

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import br.com.myfitt.MainActivity.Companion.FICHA_ID
import br.com.myfitt.ui.screens.ListaExerciciosFichaScreen

fun NavGraphBuilder.listaExerciciosFichaRoute(navController: NavController){
    composable(
        route = Routes.LISTA_EXERCICIOS_FICHA.name + "/{$FICHA_ID}",
        arguments = listOf(navArgument(FICHA_ID) {
            type = NavType.IntType
        })
    ) {
        ListaExerciciosFichaScreen(fichaId = it.arguments!!.getInt(FICHA_ID))
    }
}