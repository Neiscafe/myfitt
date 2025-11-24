package br.com.myfitt.treinos.ui.screens.seriesExercicio

import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

object SeriesExercicioNavigation {
    const val arg1 = "exercicioTreinoId"
    const val route = "seriesExercicio"
    const val routeWithArg = "$route/{$arg1}"
    val argList =
        listOf(navArgument(arg1) { type = NavType.IntType })

    fun getArg(entry: NavBackStackEntry) = entry.arguments?.getInt(arg1) ?: -1
    fun composeNavigation(builder: NavGraphBuilder, navController: NavController) {
        builder.composable(
            route = routeWithArg,
            arguments = argList,
        ) {
            SeriesExercicioScreen(
                exercicioTreinoId = getArg(it),
                irParaEditarSeries = {},
                popBackstack = navController::popBackStack
            )
        }

    }
}

@Composable
fun SeriesExercicioScreen(
    exercicioTreinoId: Int,
    irParaEditarSeries: () -> Unit,
    popBackstack: () -> Boolean
) {

}