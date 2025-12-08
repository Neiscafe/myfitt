@file:OptIn(ExperimentalMaterial3Api::class)

package br.com.myfitt.treinos.ui.screens.seriesExercicio

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import br.com.myfitt.R
import br.com.myfitt.common.domain.SerieExercicio
import br.com.myfitt.treinos.ui.TickCronometro
import org.koin.compose.koinInject
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
    popBackstack: () -> Boolean
) {
    val viewModel: SeriesExercicioViewModel =
        koinInject(parameters = parametersOf(exercicioTreinoId))
    val state by viewModel.state.collectAsStateWithLifecycle()
    val cronometroState by viewModel.cronometroState.collectAsStateWithLifecycle()
    var exibeDialogFinalizaSerie by remember { mutableStateOf(false) }
    Tela(
        irParaEditarSeries = irParaEditarSeries,
        popBackstack = popBackstack,
        resetaEventos = viewModel::resetaEventos,
        pesoMudou = viewModel::pesoMudou,
        iniciaSerie = viewModel::inicioExecucao,
        state = state,
        cronometroState = cronometroState,
        finalizaSerie = {
            viewModel.fimExecucao()
            exibeDialogFinalizaSerie = true
        },
        finalizaTreino = {})
    if (exibeDialogFinalizaSerie) {
        DialogRepeticoes(
            onDismiss = { exibeDialogFinalizaSerie = false },
            repeticoesMudou = viewModel::repeticoesMudou,
            aplicaRepeticoes = {
                viewModel.informaRepeticoes()
                exibeDialogFinalizaSerie = false
            })
    }
}

@Composable
private fun DialogRepeticoes(
    onDismiss: () -> Unit, repeticoesMudou: (String) -> Unit, aplicaRepeticoes: () -> Unit
) {
    BasicAlertDialog(
        onDismissRequest = onDismiss, properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false,
            usePlatformDefaultWidth = true
        )
    ) {
        val focusRequester = remember { FocusRequester() }
        var textFieldLoaded by remember { mutableStateOf(false) }
        var repeticoesTextFieldValue by remember {
            mutableStateOf(
                TextFieldValue(
                    "1", selection = TextRange(1)
                )
            )
        }
        LaunchedEffect(Unit) {
            repeticoesMudou(repeticoesTextFieldValue.text)
        }
        Card() {
            Column(
                modifier = Modifier.padding(32.dp, 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Icon(Icons.Default.Check, "Série finalizada")
                Text("Série finalizada!")
                OutlinedTextField(
                    modifier = Modifier
                        .width(IntrinsicSize.Min)
                        .focusRequester(focusRequester)
                        .onGloballyPositioned {
                            if (!textFieldLoaded) {
                                focusRequester.requestFocus()
                                textFieldLoaded = true
                            }
                        },
                    value = repeticoesTextFieldValue,
                    onValueChange = {
                        repeticoesMudou(it.text)
                        repeticoesTextFieldValue = it
                    },
                    maxLines = 1,
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Decimal),
                    leadingIcon = {
                        IconButton(onClick = {
                            val diminuido = (repeticoesTextFieldValue.text.toIntOrNull()?.minus(2)
                                ?: 0).toString()
                            repeticoesMudou(diminuido)
                            repeticoesTextFieldValue = TextFieldValue(
                                text = diminuido, selection = TextRange(diminuido.length)
                            )
                        }) {
                            Icon(
                                painterResource(R.drawable.remove_24dp_000000_fill0_wght400_grad0_opsz24),
                                "Diminuir 2 repetições"
                            )
                        }
                    },
                    trailingIcon = {
                        IconButton(
                            onClick = {
                                val aumentado =
                                    (repeticoesTextFieldValue.text.toIntOrNull()?.plus(2)
                                        ?: 0).toString()
                                repeticoesMudou(aumentado)
                                repeticoesTextFieldValue = TextFieldValue(
                                    text = aumentado, selection = TextRange(aumentado.length),
                                )
                            },
                        ) {
                            Icon(
                                Icons.Default.Add, "Adicionar 2 repetições"
                            )
                        }
                    })
                Box(modifier = Modifier.fillMaxWidth()) {
                    TextButton(
                        modifier = Modifier.align(Alignment.CenterEnd), onClick = aplicaRepeticoes
                    ) { Text("Confirmar") }
                }
            }
        }
    }
}

internal fun Context.findActivity(): Activity {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }
    throw IllegalStateException("Permissions should be called in the context of an Activity")
}

@Composable
private fun Tela(
    irParaEditarSeries: () -> Unit,
    popBackstack: () -> Boolean,
    resetaEventos: () -> Unit,
    pesoMudou: (String) -> Unit,
    iniciaSerie: () -> Unit,
    finalizaSerie: () -> Unit,
    finalizaTreino: () -> Unit,
    cronometroState: TickCronometro,
    state: SeriesExercicioState
) {
    var pesoText by remember { mutableStateOf(TextFieldValue("10", TextRange(2))) }
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(Unit) {
        pesoMudou(pesoText.text)
    }
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
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
        Column(
            modifier = Modifier.padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LazyColumn(
                modifier = Modifier
                    .height(200.dp)
                    .fillMaxWidth()
            ) {
                itemsIndexed(items = state.series, key = { i, it -> it.serieId }) { i, it ->
                    OutlinedCard(
                        shape = RoundedCornerShape(0.dp), modifier = Modifier.fillMaxWidth()
                    ) {
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
            if (cronometroState.descansoAtivo) {
                Text("Descansando: ${cronometroState.numero / 60}m${cronometroState.numero % 60}")
            }
            if (cronometroState.serieAtiva) {
                Text("Duração: ${cronometroState.numero / 60}m${cronometroState.numero % 60}")
                Button(finalizaSerie) {
                    Text("Finalizar")
                }
            }
            if (state.observacaoExercicio.isNotEmpty()) {
                Text("Observação:")
                Text(modifier = Modifier.padding(32.dp, 0.dp), text = state.observacaoExercicio)
            }
            if (!cronometroState.serieAtiva) {
                OutlinedTextField(
                    value = pesoText,
                    onValueChange = {
                        pesoMudou(it.text)
                        pesoText = it
                    },
                    maxLines = 1,
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    label = { Text("Peso (Kg)") },
                    leadingIcon = {
                        IconButton(onClick = {
                            val diminuido = (pesoText.text.toIntOrNull()?.minus(10) ?: 0).toString()
                            pesoMudou(diminuido)
                            pesoText = TextFieldValue(diminuido, TextRange(diminuido.length))
                        }) {
                            Icon(
                                painterResource(R.drawable.remove_24dp_000000_fill0_wght400_grad0_opsz24),
                                "Diminuir mais 10kg"
                            )
                        }
                    },
                    trailingIcon = {
                        IconButton(onClick = {
                            val aumentado = (pesoText.text.toIntOrNull()?.plus(10) ?: 0).toString()
                            pesoMudou(aumentado)
                            pesoText = TextFieldValue(aumentado, TextRange(aumentado.length))
                        }) {
                            Icon(
                                Icons.Default.Add, "Adicionar mais 10kg"
                            )
                        }
                    })
                Button(iniciaSerie) {
                    Icon(
                        painterResource(R.drawable.timer_24dp_000000_fill0_wght400_grad0_opsz24),
                        "Iniciar contador da série"
                    )
                    Text("Iniciar série")
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
        irParaEditarSeries = {},
        popBackstack = { true },
        resetaEventos = {},
        pesoMudou = {},
        iniciaSerie = {},
        finalizaSerie = {},
        state = SeriesExercicioState(
            series = listOf(
                SerieExercicio(
                    serieId = 1,
                    exercicioTreinoId = 1,
                    exercicioId = 1,
                    dhInicioExecucao = LocalDateTime.now(),
                    dhFimExecucao = LocalDateTime.now(),
                    duracaoSegundos = 90,
                    segundosDescanso = 90,
                    pesoKg = 90,
                    repeticoes = 12,
                    treinoId = 1,
                    dhInicioDescanso = null,
                    dhFimDescanso = null,
                    finalizado = false
                ),
            ),
            nomeExercicio = "Supino reto",
            observacaoExercicio = "Fazer pensando na morte da bezerra"
        ),
        cronometroState = TickCronometro(),
        finalizaTreino = {},
    )
}