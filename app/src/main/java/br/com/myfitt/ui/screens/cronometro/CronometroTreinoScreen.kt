package br.com.myfitt.ui.screens.cronometro

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.myfitt.MyFittTimerService
import br.com.myfitt.domain.models.Exercicio
import br.com.myfitt.ui.components.DropdownTextField
import br.com.myfitt.ui.theme.MyFittTheme
import br.com.myfitt.ui.viewModels.CronometroTreinoViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun _CronometroTreinoScreen(
    onCronometroAction: (CronometroActions) -> Unit = {},
//    viewModelState: Flow<CronometroTreinoViewModel.CronometroTreinoState> = flowOf(),
    viewModel: CronometroTreinoViewModel = koinViewModel(),
    selectExercise: (Exercicio) -> Unit = {}
) {
//    val state by viewModelState.collectAsStateWithLifecycle(CronometroTreinoViewModel.CronometroTreinoState())
    val state by viewModel.state.collectAsStateWithLifecycle()
    var typeOfCounterText by remember {
        mutableStateOf(state.selectedExercicioTreino?.let { "Próxima série" } ?: "Início do treino")
    }
    var counterText by remember { mutableStateOf("00m00s") }
    var buttonControl by remember { mutableStateOf(true) }
    val startCronometroCallback = object : MyFittTimerService.MyFittTimerCallback {
        override fun onTick(elapsedSeconds: Int, timerType: Int) {
            counterText = "${elapsedSeconds / 60}m${elapsedSeconds%60}s"
        }
    }
//    LaunchedEffect(Unit) {
//        onCronometroAction(
//            CronometroActions.StartListeningCronometro(
//                startCronometroCallback
//            )
//        )
//    }
    Column(
        Modifier
            .fillMaxHeight()
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        DropdownTextField(
            state.exercicios,
            { it?.nome ?: "Nenhum" },
            {
                it?.let {
                    selectExercise(it)
                }
            },
            selected = state.selectedExercicioTreino?.exercicio,
            hint = "Exercício",
            acceptsNull = false
        )
        Text(typeOfCounterText)
        Text(counterText)
        if (buttonControl) {
            Button({
                if (state.selectedExercicioTreino == null) {
                    return@Button
                }
                onCronometroAction(
                    CronometroActions.StartAndListenCronometro(
                        MyFittTimerService.EXTRA_START_EXERCISE, startCronometroCallback
                    )
                )
                typeOfCounterText = "Duração da série"
                buttonControl = !buttonControl
            }) { Text("Iniciar") }
        } else {
            Button({
                if (state.selectedExercicioTreino == null) {
                    return@Button
                }
                onCronometroAction(
                    CronometroActions.StartAndListenCronometro(
                        MyFittTimerService.EXTRA_START_REST, startCronometroCallback
                    )
                )
                typeOfCounterText = "Duração do descanso"
                buttonControl = !buttonControl
            }) { Text("Descansar") }
        }
    }
}

@Composable
fun CronometroTreinoScreen(
    treinoId: Int,
    lastExercicioId: Int,
    viewModel: CronometroTreinoViewModel = koinViewModel { parametersOf(treinoId, lastExercicioId) }
) {
    _CronometroTreinoScreen(
        viewModel::onCronometroAction, viewModel, viewModel::selectExercise
    )
}

@Preview(showBackground = true)
@Composable
private fun CronometroTreinoScreenPreview() {
    MyFittTheme {
        _CronometroTreinoScreen()
    }
}