package br.com.myfitt.treinos.ui.screens.menuPrincipal

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import br.com.myfitt.R
import br.com.myfitt.treinos.ui.screens.exerciciosTreino.ExerciciosTreinoNavigation
import br.com.myfitt.treinos.ui.theme.MyFittTheme
import org.koin.androidx.compose.koinViewModel

object MenuPrincipalNavigation {
    const val route = "menuPrincipal"
    const val treinoDest = 1
    const val listaTreinoDest = 2
    fun composeNavigation(builder: NavGraphBuilder, navController: NavController) {
        builder.composable(
            route = route,
        ) {
            MenuPrincipalScreen(
                navegaNovoTreino = { navController.navigate(ExerciciosTreinoNavigation.route + "/${it}") },
                navegaListaTreinos = {})
        }
    }

}

@Composable
fun MenuPrincipalScreen(
    navegaNovoTreino: (Int) -> Unit,
    navegaListaTreinos: () -> Unit,
    viewModel: MenuPrincipalViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(lifecycleOwner) {
        viewModel.eventos.flowWithLifecycle(lifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .collect {
                when (it) {
                    MenuPrincipalEvents.NavegaListaTreinos -> {}
                    is MenuPrincipalEvents.NavegaTreino -> navegaNovoTreino(it.treinoId)
                }
            }
    }
    Tela(
        state = state,
        resetaEventos = viewModel::resetaEventos,
        navegaListaTreinos = navegaListaTreinos,
        novoTreinoClick = viewModel::novoTreino
    )
}

@Composable
private fun Tela(
    state: MenuPrincipalState,
    resetaEventos: () -> Unit,
    navegaListaTreinos: () -> Unit,
    novoTreinoClick: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = Modifier.padding(16.dp, 48.dp, 16.dp, 0.dp)
    ) { innerPadding ->
        if (state.carregando) {
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator()
            }
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.background)
                .padding(innerPadding)
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            Text("Bem-vindo!", style = MaterialTheme.typography.headlineLarge)
            Text("O que vamos treinar hoje?", style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(24.dp))
            ElevatedCard(
                modifier = Modifier.fillMaxWidth()

            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Box(modifier = Modifier.size(40.dp), contentAlignment = Alignment.Center) {
                            Icon(
                                painter = painterResource(R.drawable.exercise_24dp_000000_fill0_wght400_grad0_opsz24),
                                contentDescription = "Peso de treino"
                            )
                        }
                        Column {
                            Text("Meus treinos", style = MaterialTheme.typography.titleMedium)
                            Text(
                                "Hora de superar seus limites!",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp, 0.dp, 8.dp, 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    OutlinedButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        shape = RoundedCornerShape(10.dp),
                        onClick = navegaListaTreinos
                    ) {
                        Text(
                            "Histórico", color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        shape = RoundedCornerShape(10.dp),
                        onClick = novoTreinoClick
                    ) { Text("Novo") }
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
private fun MenuPrincipalScreenPreview() {
    MyFittTheme {
        Tela(MenuPrincipalState(false, null), {}, {}, {})
    }
}