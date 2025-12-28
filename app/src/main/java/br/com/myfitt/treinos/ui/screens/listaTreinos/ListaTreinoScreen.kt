@file:OptIn(ExperimentalMaterial3Api::class)

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import br.com.myfitt.R
import br.com.myfitt.common.domain.TipoExercicio
import br.com.myfitt.common.domain.Treino
import br.com.myfitt.treinos.ui.screens.exerciciosTreino.ExerciciosTreinoNavigation
import br.com.myfitt.treinos.ui.theme.MyFittTheme
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object ListaTreinoNavigation {
    val route = "listaTreino"
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
    val viewModel: ListaTreinoViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()
    Tela(
        state = state,
        limpaEventos = viewModel::limpaEventos,
        irParaTreino = {
            navController.navigate(ExerciciosTreinoNavigation.route + "/${it.treinoId}")
        },
        deletar = viewModel::deletar,
        voltar = { navController.popBackStack() }
    )
}

@Composable
fun Tela(
    state: ListaTreinoState,
    limpaEventos: () -> Unit,
    irParaTreino: (Treino) -> Unit,
    deletar: (Treino)->Unit = {},
    voltar: () -> Unit = {}
) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    Scaffold(topBar = {
        TopAppBar(
            { Text("Histórico de treinos") },
            navigationIcon = {
                IconButton(voltar) {
                    Icon(
                        Icons.AutoMirrored.Default.ArrowBack,
                        "Voltar"
                    )
                }
            })
    }, snackbarHost = { SnackbarHost(snackbarHostState) }) { innerPadding ->
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
                itemsIndexed(
                    items = state.treinos, key = { i, it -> it.treino.treinoId }) { i, it ->
                    ListaTreinoItem(it, i, onClick = { irParaTreino(it.treino) }, deletar)
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
fun ListaTreinoItem(it: ListaTreinoModel, i: Int, onClick: () -> Unit = {}, deletar: (Treino)->Unit = {}) {
    val cardWidthModifier = Modifier.fillMaxWidth()
    ElevatedCard(
        cardWidthModifier.padding(16.dp)
    ) {
        Column(
            modifier = cardWidthModifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val textStyle = MaterialTheme.typography.bodySmall
            val rowSpacing = Arrangement.spacedBy(8.dp)
            Row(
                modifier = cardWidthModifier,
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = rowSpacing) {
                    Icon(
                        painterResource(R.drawable.exercise_24dp_000000_fill0_wght400_grad0_opsz24),
                        "Id treino"
                    )
                    Text(
                        "Treino ${it.treino.treinoId}", style = MaterialTheme.typography.titleLarge
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        "${
                            if (it.treino.dhInicio == null) "Não iniciado" else it.treino.dhInicio.format(
                                DateTimeFormatter.ofPattern("dd/MM/yyyy")
                            )
                        }",
                        style = textStyle.copy(MaterialTheme.colorScheme.inverseOnSurface),
                        modifier = Modifier
                            .background(
                                MaterialTheme.colorScheme.inverseSurface, MaterialTheme.shapes.medium
                            )
                            .padding(8.dp, 4.dp)
                    )
                    IconButton({deletar(it.treino)}) { Icon(Icons.Default.Delete, "Deletar") }
                }
            }
            LazyRow {
                items(items = it.tipoExercicios, key = { it.tipoExercicioId }) {
                    Text(
                        text = it.descricao, style = textStyle, modifier = Modifier
                            .background(
                                MaterialTheme.colorScheme.surfaceVariant,
                                MaterialTheme.shapes.medium
                            )
                            .padding(8.dp, 4.dp)
                    )
                }
            }
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
                        "${if(it.treino.dhInicio==null) "00:00" else it.treino.dhInicio?.format(DateTimeFormatter.ofPattern("hh:mm"))}",
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
                        "${it.treino.segundosDuracao / 60 / 60}h${it.treino.segundosDuracao % 60}m",
                        style = textStyle
                    )
                }
            }
            Button(onClick, modifier = cardWidthModifier, shape = MaterialTheme.shapes.small) {
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
            it = ListaTreinoModel(
                Treino(
                    1,
                    dhInicio = LocalDateTime.now().minusHours(2).minusMinutes(2),
                    dhFim = LocalDateTime.now()
                ), emptyList()
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
                    ListaTreinoModel(
                        Treino(
                            1,
                            dhInicio = LocalDateTime.now().minusHours(2).minusMinutes(2),
                            dhFim = LocalDateTime.now()
                        ), listOf(TipoExercicio(1, "Bíceps"))
                    )
                ),
                0,
            ), {}, {}, {})
    }
}