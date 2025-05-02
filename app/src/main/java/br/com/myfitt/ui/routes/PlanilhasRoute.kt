package br.com.myfitt.ui.routes

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import br.com.myfitt.MainActivity.Companion.PLANILHA_ID
import br.com.myfitt.ui.routes.Routes.LISTA_PLANILHAS
import br.com.myfitt.ui.routes.Routes.LISTA_TREINOS_PLANILHA
import br.com.myfitt.ui.screens.ListaPlanilhasScreen

fun NavGraphBuilder.listaPlanilhasRoute(navController: NavController) {
    composable(route = LISTA_PLANILHAS.name)
     {
        ListaPlanilhasScreen {
            navController.navigate(LISTA_TREINOS_PLANILHA.name + "/$it")
        }
    }
}