@file:OptIn(ExperimentalMaterial3Api::class)

package br.com.myfitt.treinos.ui.screens.detalhesExercicio

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import br.com.myfitt.common.domain.Exercicio
import br.com.myfitt.treinos.ui.theme.MyFittTheme
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

object DetalhesExercicioNavigation {
    val route = "detalhesExercicio"
    val arg1 = "exercicioId"
    val routeWithArg = "$route/{$arg1}"
    val argList = listOf(navArgument(arg1) { type = NavType.IntType })
    fun getArg(entry: NavBackStackEntry) = entry.arguments?.getInt(arg1) ?: -1
    fun composeNavigation(builder: NavGraphBuilder, navController: NavController) {
        builder.composable(
            route = routeWithArg,
            arguments = argList,
        ) {
            DetalhesExercicioScreen(getArg(it), navController)
        }
    }
}

@Composable
fun DetalhesExercicioScreen(exercicioId: Int, navController: NavController) {
    val viewModel = koinViewModel<DetalhesExercicioViewModel>(parameters = {
        parametersOf(
            exercicioId
        )
    })
    val state by viewModel.state.collectAsStateWithLifecycle()
    val observacao by viewModel.observacao.collectAsState()
//    if (state.retornar) {
//        navController.popBackStack()
//    }
    Tela(
        voltar = { navController.popBackStack() },
        observacao = observacao,
        observacaoChanged = viewModel::observacaoMudou,
        limpaEventos = viewModel::resetaEventos,
        salvar = viewModel::salvar,
        state = state,
    )
}

@Composable
fun Tela(
    voltar: () -> Unit = {},
    observacao: String = "",
    observacaoChanged: (String) -> Unit = {},
    salvar: () -> Unit = {},
    limpaEventos: () -> Unit = {},
    state: DetalhesExercicioState,
) {
    val fillParentWidth = remember { Modifier.fillMaxWidth() }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }, topBar = {
        TopAppBar({
            Text(
                state.exercicio?.nome ?: "Carregando..."
            )
        }, navigationIcon = {
            IconButton(voltar) {
                Icon(
                    Icons.AutoMirrored.Default.ArrowBack, "Voltar"
                )
            }
        })
    }) { innerPadding ->
        state.erro?.let {
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = it
                )
                limpaEventos()
            }
        }
        if (state.carregando) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = fillParentWidth
                .padding(innerPadding)
                .padding(32.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                modifier = fillParentWidth,
                value = observacao,
                onValueChange = observacaoChanged,
                label = { Text("Observação") })
            Button(
                salvar,
                modifier = fillParentWidth,
                shape = MaterialTheme.shapes.small,
                contentPadding = PaddingValues(0.dp, 12.dp)
            ) {
                Text("Salvar")
            }
        }
    }
}

@Preview
@Composable
private fun DetalhesExercicioPreview() {
    MyFittTheme {
        Tela(
            state = DetalhesExercicioState(
                exercicio = Exercicio(1, "Supino reto"),
                carregando = false,
            ),
        )
    }
}