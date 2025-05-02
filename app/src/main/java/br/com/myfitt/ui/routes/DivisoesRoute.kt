package br.com.myfitt.ui.routes

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import br.com.myfitt.ui.screens.ListaDivisoesScreen

fun NavGraphBuilder.listaDivisoesRoute(navController: NavController){
    composable(Routes.LISTA_DIVISOES.name){
        ListaDivisoesScreen(navigate = {
            navController.navigate(Routes.LISTA_PLANILHAS.name+"/$it")
        })
    }
}