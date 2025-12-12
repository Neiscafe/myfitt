@file:OptIn(ExperimentalMaterial3Api::class)

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import br.com.myfitt.R
import br.com.myfitt.treinos.ui.screens.common.MyFittScaffold
import br.com.myfitt.treinos.ui.screens.editarSerie.EditarSerieState
import br.com.myfitt.treinos.ui.screens.editarSerie.EditarSerieViewModel
import br.com.myfitt.treinos.ui.theme.MyFittTheme
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

object EditarSerieNavigation {
    const val arg1 = "serieId"
    const val route = "editarSerie"
    const val routeWithArg = "$route/{$arg1}"
    val argList = listOf(navArgument(arg1) { type = NavType.IntType })

    fun getArg(entry: NavBackStackEntry) = entry.arguments?.getInt(arg1) ?: -1
    fun composeNavigation(builder: NavGraphBuilder, navController: NavController) {
        builder.composable(
            route = routeWithArg,
            arguments = argList,
        ) {
            EditarSerieScreen(getArg(it), navController)
        }
    }
}

@Composable
fun EditarSerieScreen(serieId: Int, navController: NavController) {
    val viewModel: EditarSerieViewModel = koinViewModel(parameters = { parametersOf(serieId) })
    val state by viewModel.state.collectAsStateWithLifecycle()
    val repeticoes by viewModel.repeticoes.collectAsStateWithLifecycle()
    val peso by viewModel.peso.collectAsStateWithLifecycle()
    Tela(
        state = state,
        pesoKg = String.format("%.1f", peso),
        repeticoes = repeticoes.toString(),
        onPesoChanged = viewModel::pesoChanged,
        onRepChanged = viewModel::repeticoesChanged,
        salvar = viewModel::salvar,
        limpaEventos = viewModel::limpaEventos,
        voltar = { navController.popBackStack() })
}


@Composable
fun Tela(
    state: EditarSerieState,
    pesoKg: String = "",
    repeticoes: String = "",
    onPesoChanged: (String) -> Unit = { },
    onRepChanged: (String) -> Unit = { },
    salvar: () -> Unit = {},
    limpaEventos: () -> Unit = {},
    voltar: () -> Unit = {},
) {
    MyFittScaffold(
        topBarTitle = { Text("Corrigir série") },
        error = state.erro,
        carregando = state.carregando,
        voltar = voltar,
        limpaEventos = limpaEventos,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        val textFieldModifier = remember { Modifier.fillMaxWidth(0.9f) }
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(R.drawable.exercise_24dp_000000_fill0_wght400_grad0_opsz24),
                contentDescription = "Peso"
            )
            TextField(
                pesoKg,
                onPesoChanged,
                modifier = textFieldModifier,
                label = { Text("Peso (KG)") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
            )
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(R.drawable.outline_repeat_24),
                contentDescription = "Repetições"
            )
            TextField(
                repeticoes,
                onRepChanged,
                modifier = textFieldModifier,
                label = { Text("Repetições") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Decimal)
            )
        }
        Button(
            salvar, enabled = state.salvarHabilitado
        ) {
            Text("Salvar")
            Icon(Icons.Default.Check, "Salvar")
        }
    }
}

@Preview
@Composable
private fun EditarSeriePreview() {
    MyFittTheme {
        Tela(
            EditarSerieState(
                carregando = false,
                erro = null,
                salvarHabilitado = true,
            )
        )
    }
}

