package br.com.myfitt.treinos.ui.screens.listaTreinos

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import br.com.myfitt.R
import br.com.myfitt.common.domain.Treino
import br.com.myfitt.treinos.ui.theme.MyFittTheme
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object ListaTreinoNavigation {
    val route = "listaTreinos"
    fun composeNavigation(builder: NavGraphBuilder, navController: NavController) {
        builder.composable(
            route = route
        ) {
            ListaTreinosScreen(navController)
        }
    }
}

@Composable
fun ListaTreinosScreen(navController: NavController) {

}

@Composable
fun Tela(state: ListaTreinoState, limpaEventos: () -> Unit, irParaTreino: (Treino) -> Unit) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) { innerPadding ->
        state.erro?.let {
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = it
                )
                limpaEventos()
            }
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                itemsIndexed(items = state.treinos, key = { i, it -> it.treinoId }) { i, it ->
                    ListaTreinoItem(it, i)
                }
                if (state.carregando) {
                    item {
                        Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ListaTreinoItem(it: Treino, i: Int) {
    val cardWidthModifier = Modifier.fillMaxWidth()
    ElevatedCard(
        cardWidthModifier.padding(16.dp)
    ) {
        Column(
            modifier = cardWidthModifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            val textStyle = MaterialTheme.typography.bodySmall
            val rowSpacing = Arrangement.spacedBy(8.dp)
            Row(
                modifier = cardWidthModifier,
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Row(horizontalArrangement = rowSpacing) {
                    Icon(
                        painterResource(R.drawable.exercise_24dp_000000_fill0_wght400_grad0_opsz24),
                        "Id treino"
                    )
                    Text("Treino ${it.treinoId}", style = MaterialTheme.typography.titleMedium)
                }
                if (it.tipoTreinoDescr != null) {
                    Text(
                        it.tipoTreinoDescr,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier
                            .background(
                                MaterialTheme.colorScheme.surfaceVariant, MaterialTheme.shapes.small
                            )
                            .padding(8.dp, 4.dp),
                    )
                }
            }
            Text(
                "${it.dhInicio!!.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))}",
                style = textStyle.copy(MaterialTheme.colorScheme.inverseOnSurface),
                modifier = Modifier
                    .background(
                        MaterialTheme.colorScheme.inverseSurface, MaterialTheme.shapes.medium
                    )
                    .padding(8.dp, 4.dp)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = rowSpacing,
            ) {
                Icon(Icons.Default.Star, "Inicio treino")
                Column {
                    Text(
                        "Início", style = textStyle
                    )
                    Text(
                        "${it.dhInicio.format(DateTimeFormatter.ofPattern("hh:mm"))}",
                        style = textStyle
                    )
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically, horizontalArrangement = rowSpacing
            ) {
                Icon(
                    painterResource(R.drawable.timer_24dp_000000_fill0_wght400_grad0_opsz24),
                    "Duração treino"
                )
                Column {
                    Text("Duração:", style = textStyle)
                    Text(
                        "${it.segundosDuracao / 60 / 60}h${(it.segundosDuracao / 60) % 60}m",
                        style = textStyle
                    )
                }
            }
            Button({}, modifier = cardWidthModifier, shape = MaterialTheme.shapes.small) {
                Text("Visualizar")
            }
        }
    }
}

@Preview
@Composable
private fun ListaTreinoItemPreview() {
    MyFittTheme {
        ListaTreinoItem(
            Treino(
                1,
                dhInicio = LocalDateTime.now().minusHours(2).minusMinutes(2),
                dhFim = LocalDateTime.now()
            ), 0
        )
    }
}

@Preview
@Composable
private fun TelaPreview() {
    MyFittTheme {
        Tela(
            ListaTreinoState(
            treinos = listOf(
                Treino(
                    1,
                    dhInicio = LocalDateTime.now().minusHours(2).minusMinutes(2),
                    dhFim = LocalDateTime.now(),
                    tipoTreinoDescr = "Pernas"
                )
            ),
            0,
        ), {}, {})
    }
}