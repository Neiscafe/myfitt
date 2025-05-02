package br.com.myfitt.ui.routes

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import br.com.myfitt.MainActivity.Companion.PLANILHA_ID
import br.com.myfitt.ui.routes.Routes.CRIAR_TREINO
import br.com.myfitt.ui.routes.Routes.LISTA_TREINOS_PLANILHA
import br.com.myfitt.ui.screens.ListaTreinosPlanilhaScreen

fun NavGraphBuilder.listaTreinosPlanilhaRoute(navController: NavController) {
    composable(
        route = LISTA_TREINOS_PLANILHA.name + "/{$PLANILHA_ID}",
        arguments = listOf(navArgument(PLANILHA_ID) {
            type = NavType.IntType
        })
    ) {
        ListaTreinosPlanilhaScreen(
            it.arguments?.getInt(PLANILHA_ID)!!,
         {
            navController.navigate(Routes.LISTA_EXERCICIOS_TREINO.name + "/$it")
        }, {navController.navigate(Routes.LISTA_DIVISOES.name)})
    }
}