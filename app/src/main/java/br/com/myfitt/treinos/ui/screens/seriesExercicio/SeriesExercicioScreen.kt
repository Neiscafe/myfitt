@file:OptIn(ExperimentalMaterial3Api::class)

package br.com.myfitt.treinos.ui.screens.seriesExercicio

import EditarSerieNavigation
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.VerticalDivider
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
import androidx.compose.ui.text.style.TextAlign
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
import br.com.myfitt.treinos.ui.screens.detalhesExercicio.DetalhesExercicioNavigation
import br.com.myfitt.treinos.ui.screens.detalhesExercicio.DetalhesExercicioViewModel
import br.com.myfitt.treinos.ui.screens.editarSerie.EditarSerieViewModel
import br.com.myfitt.treinos.ui.theme.MyFittTheme
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import java.time.Instant
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
            SeriesExercicioScreen(exercicioTreinoId = exercicioSerieId, irParaEditarSerie = {
                navController.navigate(EditarSerieNavigation.route + "/${it.serieId}")
            }, popBackstack = navController::popBackStack, irParaDetalhesExercicio = {
                navController.navigate(DetalhesExercicioNavigation.route + "/${it}")
            })
        }

    }
}

@Composable
fun SeriesExercicioScreen(
    exercicioTreinoId: Int,
    irParaEditarSerie: (SerieExercicio) -> Unit = {},
    irParaDetalhesExercicio: (Int) -> Unit = {},
    popBackstack: () -> Boolean = { true }
) {
    val viewModel: SeriesExercicioViewModel =
        koinViewModel(parameters = { parametersOf(exercicioTreinoId) })
    val state by viewModel.state.collectAsStateWithLifecycle()
    val cronometroState by viewModel.cronometroState.collectAsStateWithLifecycle()
    var exibeDialogFinalizaSerie by remember { mutableStateOf(false) }
    BackHandler {
        if (!state.carregando && !cronometroState.serieAtiva) {
            popBackstack()
        }
    }
    Tela(
        irParaEditarSerie = {
            EditarSerieViewModel.setCallback {
                popBackstack()
                viewModel.atualizaEstado(it)
            }
            irParaEditarSerie(it)
        },
        popBackstack = {
            !state.carregando && !cronometroState.serieAtiva && popBackstack()
        },
        resetaEventos = viewModel::resetaEventos,
        pesoMudou = viewModel::pesoMudou,
        iniciaSerie = viewModel::inicioExecucao,
        state = state,
        cronometroState = cronometroState,
        finalizaSerie = {
            viewModel.fimExecucao()
            exibeDialogFinalizaSerie = true
        },
        irParaDetalhesExercicio = {
            DetalhesExercicioViewModel.onSalvar {
                viewModel.atualiza(it)
                popBackstack()
            }
            irParaDetalhesExercicio(viewModel.exercicioTreino.exercicioId)
        })
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
                    "8", selection = TextRange(1)
                )
            )
        }
        LaunchedEffect(Unit) {
            repeticoesMudou(repeticoesTextFieldValue.text)
        }
        ElevatedCard() {
            Column(
                modifier = Modifier.padding(32.dp, 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Icon(Icons.Default.Check, "Série finalizada")
                Text("Série finalizada!")
                TextField(
                    shape = RoundedCornerShape(0.dp),
                    label = { Text("Repetições") },
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
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Button({
                            val diminuido = (repeticoesTextFieldValue.text.toIntOrNull()?.minus(1)
                                ?: 0).toString()
                            repeticoesMudou(diminuido)
                            repeticoesTextFieldValue = TextFieldValue(
                                text = diminuido, selection = TextRange(diminuido.length)
                            )
                        }) {
                            Text("- 1")
                        }

                        Button({
                            val diminuido = (repeticoesTextFieldValue.text.toIntOrNull()?.minus(2)
                                ?: 0).toString()
                            repeticoesMudou(diminuido)
                            repeticoesTextFieldValue = TextFieldValue(
                                text = diminuido, selection = TextRange(diminuido.length)
                            )
                        }) {
                            Text("- 2")
                        }

                    }
                    Column {
                        Button({
                            val aumentado = (repeticoesTextFieldValue.text.toIntOrNull()?.plus(1)
                                ?: 0).toString()
                            repeticoesMudou(aumentado)
                            repeticoesTextFieldValue = TextFieldValue(
                                text = aumentado, selection = TextRange(aumentado.length),
                            )
                        }) {
                            Text("+ 1")
                        }

                        Button({
                            val aumentado = (repeticoesTextFieldValue.text.toIntOrNull()?.plus(2)
                                ?: 0).toString()
                            repeticoesMudou(aumentado)
                            repeticoesTextFieldValue = TextFieldValue(
                                text = aumentado, selection = TextRange(aumentado.length),
                            )
                        }) {
                            Text("+ 2")
                        }
                    }
                }
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
    cronometroState: TickCronometro,
    state: SeriesExercicioState,
    popBackstack: () -> Boolean = { true },
    resetaEventos: () -> Unit = {},
    pesoMudou: (String) -> Unit = {},
    iniciaSerie: () -> Unit = {},
    finalizaSerie: () -> Unit = {},
    irParaDetalhesExercicio: () -> Unit = {},
    irParaEditarSerie: (SerieExercicio) -> Unit = {},
) {
    var pesoText by remember { mutableStateOf(TextFieldValue("10", TextRange(2))) }
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(Unit) {
        pesoMudou(pesoText.text)
    }
    Scaffold(
        Modifier.padding(0.dp, 0.dp, 0.dp, 32.dp),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar({ Text(state.nomeExercicio) }, navigationIcon = {
                IconButton(onClick = { popBackstack() }) {
                    Icon(
                        Icons.AutoMirrored.Default.ArrowBack,
                        "Voltar para exercícios",
                    )
                }
            })
        }) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (cronometroState.descansoAtivo || cronometroState.serieAtiva) {
                OutlinedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp, 0.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize().padding(0.dp, 8.dp, 0.dp, 0.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            painterResource(R.drawable.timer_24dp_000000_fill0_wght400_grad0_opsz24),
                            "Cronômetro de exercícios"
                        )
                        if (cronometroState.descansoAtivo) {
                            Text(
                                "Descansando: ${cronometroState.numero / 60}m${cronometroState.numero % 60}",
                                Modifier.padding(0.dp, 0.dp, 0.dp, 8.dp),
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                        if (cronometroState.serieAtiva) {
                            Text(
                                "Duração: ${cronometroState.numero / 60}m${cronometroState.numero % 60}",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Column(Modifier.fillMaxWidth()) {
                                HorizontalDivider()
                                TextButton(
                                    finalizaSerie,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(MaterialTheme.colorScheme.surfaceContainer),
                                    contentPadding = PaddingValues(24.dp)
                                ) {
                                    Text("Terminei a série!")
                                }
                            }
                        }
                    }
                }
            }

            if (!cronometroState.serieAtiva) {
                OutlinedCard(
                    modifier = Modifier.padding(24.dp, 0.dp)
                ) {
                    Column {
                        Text(
                            "Iniciar série",
                            style = MaterialTheme.typography.titleSmall,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(0.dp, 8.dp),
                            textAlign = TextAlign.Center
                        )
                        Column(Modifier.padding(24.dp, 24.dp, 24.dp, 8.dp)) {
                            TextField(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(0.dp, 12.dp),
                                value = pesoText,
                                onValueChange = {
                                    pesoMudou(it.text)
                                    pesoText = it
                                },
                                trailingIcon = { Text("KG") },
                                shape = RoundedCornerShape(0.dp),
                                maxLines = 1,
                                singleLine = true,
                                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Decimal),
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Button({
                                    val aumentado =
                                        (pesoText.text.toIntOrNull()?.minus(2) ?: 0).toString()
                                    pesoMudou(aumentado)
                                    pesoText =
                                        TextFieldValue(aumentado, TextRange(aumentado.length))
                                }) { Text("- 2") }
                                Button({
                                    val aumentado =
                                        (pesoText.text.toIntOrNull()?.plus(2) ?: 0).toString()
                                    pesoMudou(aumentado)
                                    pesoText =
                                        TextFieldValue(aumentado, TextRange(aumentado.length))
                                }) { Text("+ 2") }
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {

                                Button({
                                    val reduzido =
                                        (pesoText.text.toIntOrNull()?.minus(10) ?: 0).toString()
                                    pesoMudou(reduzido)
                                    pesoText = TextFieldValue(reduzido, TextRange(reduzido.length))
                                }) { Text("- 10") }
                                Button({
                                    val aumentado =
                                        (pesoText.text.toIntOrNull()?.plus(10) ?: 0).toString()
                                    pesoMudou(aumentado)
                                    pesoText =
                                        TextFieldValue(aumentado, TextRange(aumentado.length))
                                }) { Text("+ 10") }
                            }
                        }
                        HorizontalDivider()
                        TextButton(
                            iniciaSerie,
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.surfaceContainer),
                            contentPadding = PaddingValues(24.dp)
                        ) {
                            Text("Iniciar série")
                        }
                    }
                }
            }

            OutlinedCard(
                modifier = Modifier
                    .height(IntrinsicSize.Min)
                    .fillMaxWidth()
                    .padding(24.dp, 0.dp),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    if (state.observacaoExercicio.isNotBlank()) {
                        Icon(Icons.Default.Info, "Observações exercício")
                        Text(
                            text = state.observacaoExercicio,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                    } else {
                        TextButton(
                            onClick = irParaDetalhesExercicio, modifier = Modifier.fillMaxSize()
                        ) {
                            Icon(Icons.Default.Edit, "Adicionar observação")
                            Text("Observação")
                        }
                    }
                }
            }


            OutlinedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp, 0.dp)
            ) {
                Text(
                    "Resumo das séries",
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(0.dp, 8.dp),
                    textAlign = TextAlign.Center
                )
                if (state.series.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Aguardando início das séries...")
                    }
                } else {
                    Column(verticalArrangement = Arrangement.Top) {
                        Row(modifier = Modifier.padding(16.dp, 0.dp)) {
                            Text("", modifier = Modifier.weight(0.5f))
                            Text("Peso", modifier = Modifier.weight(1f))
                            Text("Reps", modifier = Modifier.weight(1f))
                            Text("Interv.", modifier = Modifier.weight(1f))
                        }
                        state.series.forEachIndexed { i, it ->
                            itemResumoSeries(i, it) {
                                irParaEditarSerie(it)
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
}

@Composable
private fun itemResumoSeries(
    i: Int, it: SerieExercicio, onClick: () -> Unit = {}
) {
    OutlinedCard(
        shape = RoundedCornerShape(0.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
        ) {
            Text("${i + 1}", Modifier.weight(0.5f))

            Text(
                "${
                    String.format("%.1f", it.pesoKg)
                }kg", modifier = Modifier.weight(1f)
            )

            Text(
                "${it.repeticoes}", modifier = Modifier.weight(1f)
            )
            Text(
                "${it.segundosDescanso / 60}m${it.segundosDescanso % 60}s",
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Preview(showSystemUi = true, showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun SeriesExercicioScreenPreview() {
    MyFittTheme {
        Tela(
            irParaEditarSerie = {},
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
                        pesoKg = 90f,
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
            cronometroState = TickCronometro(1, Instant.now(), false, true),
            irParaDetalhesExercicio = {},
        )
    }
}