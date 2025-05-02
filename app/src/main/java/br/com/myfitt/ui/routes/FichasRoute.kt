package br.com.myfitt.ui.routes

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import br.com.myfitt.MainActivity.Companion.DIVISAO_ID
import br.com.myfitt.ui.routes.Routes.LISTA_FICHAS_DIVISAO
import br.com.myfitt.ui.screens.ListaFichasScreen

fun NavGraphBuilder.listaFichasDivisaoRoute(navController: NavController) {
    composable(
        route = LISTA_FICHAS_DIVISAO.name + "/{$DIVISAO_ID}", arguments = listOf(navArgument(DIVISAO_ID) {
            type = NavType.IntType
        })
    ) {
        ListaFichasScreen(it.arguments!!.getInt(DIVISAO_ID), navigate = {
            navController.navigate(Routes.LISTA_EXERCICIOS_FICHA.name + "/$it")
        })
    }
}