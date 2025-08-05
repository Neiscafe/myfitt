package br.com.myfitt.ui.routes

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import br.com.myfitt.ui.routes.Routes.LISTA_TREINOS_PLANILHA
import br.com.myfitt.ui.screens.ListaTreinosPlanilhaScreen

fun NavGraphBuilder.listaTreinosPlanilhaRoute(navController: NavController) {
    composable(
        route = LISTA_TREINOS_PLANILHA.name
    ) {
        ListaTreinosPlanilhaScreen({
            navController.navigate(Routes.LISTA_EXERCICIOS_TREINO.name + "/$it")
        })
    }
}