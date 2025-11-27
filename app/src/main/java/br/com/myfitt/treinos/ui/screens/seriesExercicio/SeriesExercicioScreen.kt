@file:OptIn(ExperimentalMaterial3Api::class)

package br.com.myfitt.treinos.ui.screens.seriesExercicio

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import br.com.myfitt.common.domain.SerieExercicio
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import java.time.LocalDateTime

object SeriesExercicioNavigation {
    const val arg1 = "exercicioTreinoId"
    const val route = "seriesExercicio"
    const val routeWithArg = "$route/{$arg1}"
    val argList = listOf(navArgument(arg1) { type = NavType.IntType })

    fun getArg(entry: NavBackStackEntry) = entry.arguments?.getInt(arg1) ?: -1
    fun composeNavigation(builder: NavGraphBuilder, navController: NavController) {
        builder.composable(
            route = routeWithArg,
            arguments = argList,
        ) {
            val exercicioSerieId = getArg(it)
            SeriesExercicioScreen(
                exercicioTreinoId = exercicioSerieId,
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
    popBackstack: () -> Boolean,
    viewModel: SeriesExercicioViewModel = koinViewModel(parameters = {
        parametersOf(exercicioTreinoId)
    })
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    Tela(irParaEditarSeries, popBackstack, viewModel::resetaEventos, state)
}

@Composable
private fun Tela(
    irParaEditarSeries: () -> Unit,
    popBackstack: () -> Boolean,
    resetaEventos: () -> Unit,
    state: SeriesExercicioState
) {
    val snackbarHostState = remember { SnackbarHostState() }
    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }, topBar = {
        TopAppBar({ Text(state.nomeExercicio) }, navigationIcon = {
            IconButton(onClick = { popBackstack() }) {
                Icon(
                    Icons.AutoMirrored.Default.ArrowBack,
                    "Voltar para exercícios",
                )
            }
        }, actions = {
            IconButton(onClick = irParaEditarSeries) {
                Icon(
                    Icons.Outlined.Edit,
                    "Editar séries",
                )
            }
        })
    }) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            LazyColumn(
                modifier = Modifier
                    .height(200.dp)
                    .fillMaxWidth()
            ) {
                itemsIndexed(items = state.series, key = { i, it -> it.serieId }) { i, it ->
                    Card(shape = RoundedCornerShape(0.dp), modifier = Modifier.fillMaxWidth()) {
                        Box(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                        ) {
                            Text("${i + 1}.", Modifier.align(Alignment.CenterStart))
                            Text(
                                "${it.pesoKg}kg x ${it.repeticoes}",
                                Modifier.align(Alignment.Center)
                            )
                            Text(
                                "Intervalo: ${it.segundosDescanso / 60}m${it.segundosDescanso % 60}s",
                                Modifier.align(Alignment.CenterEnd)
                            )
                        }
                    }
                }
            }

        }
    }
    state.erro?.let {
        LaunchedEffect(it) {
            snackbarHostState.showSnackbar(it)
            resetaEventos()
        }
    }
}

@Preview(showSystemUi = true, showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun SeriesExercicioScreenPreview() {
    Tela(
        {}, { true }, {}, SeriesExercicioState(
        series = listOf(
            SerieExercicio(
                1, 1, 1, LocalDateTime.now(), LocalDateTime.now(), 90, 90, 12
            ),
            SerieExercicio(
                2, 1, 1, LocalDateTime.now(), LocalDateTime.now(), 90, 90, 12
            ),
            SerieExercicio(
                3, 1, 1, LocalDateTime.now(), LocalDateTime.now(), 90, 90, 12
            ),
            SerieExercicio(
                4, 1, 1, LocalDateTime.now(), LocalDateTime.now(), 90, 90, 12
            ),
            SerieExercicio(
                5, 1, 1, LocalDateTime.now(), LocalDateTime.now(), 90, 90, 12
            ),
            SerieExercicio(
                6, 1, 1, LocalDateTime.now(), LocalDateTime.now(), 90, 90, 12
            ),
            SerieExercicio(
                7, 1, 1, LocalDateTime.now(), LocalDateTime.now(), 90, 90, 12
            ),
            SerieExercicio(
                8, 1, 1, LocalDateTime.now(), LocalDateTime.now(), 90, 90, 12
            ),
            SerieExercicio(
                9, 1, 1, LocalDateTime.now(), LocalDateTime.now(), 90, 90, 12
            ),
            SerieExercicio(
                10, 1, 1, LocalDateTime.now(), LocalDateTime.now(), 90, 90, 12
            ),
        ),
        nomeExercicio = "Supino reto",
    )
    )
}