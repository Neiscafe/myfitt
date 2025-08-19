package br.com.myfitt.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.myfitt.domain.models.Treino
import br.com.myfitt.domain.utils.DateUtil
import br.com.myfitt.ui.components.DefaultTextField
import br.com.myfitt.ui.components.InsertionTopBar
import br.com.myfitt.ui.components.TreinoSemanaItem
import br.com.myfitt.ui.theme.MyFittTheme
import br.com.myfitt.ui.utils.TreinoByWeekMapper
import br.com.myfitt.ui.viewModels.TreinosPlanilhaViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.koin.androidx.compose.koinViewModel
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun _ListaTreinosPlanilhaScreen(
    navigate: (Int) -> Unit = {},
    getTreinosByPlanilha: Flow<List<Treino>> = flowOf(),
    insertTreino: suspend (name: String, date: LocalDate) -> Unit = { _, _ -> },
    deleteTreino: (treino: Treino) -> Unit = {}
) {
    val treinos by getTreinosByPlanilha.collectAsState(initial = emptyList())
    val exercicioEscrito = remember { mutableStateOf("") }
    var dataSelecionada by remember { mutableStateOf(DateUtil.now) }
    val isDateDialogShown = remember { mutableStateOf(false) }
    Column(
        modifier = Modifier.padding(10.dp, 30.dp, 10.dp, 0.dp)
    ) {
        DatePickerDialog(isDateDialogShown) {
            dataSelecionada = it
        }
        InsertionTopBar(title = "Treinos da Planilha", onAddClicked = {
            insertTreino(exercicioEscrito.value, dataSelecionada)
        }, InsertionField = {
            DefaultTextField(
                suffixText = DateUtil.format(dataSelecionada),
                icon = Icons.Outlined.DateRange,
                onIconClick = { isDateDialogShown.value = true },
                textValue = exercicioEscrito,
                hint = "Crie seu treino...",
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
        })
        LazyColumn(
            modifier = Modifier.fillMaxHeight(),
            contentPadding = PaddingValues(0.dp, 8.dp, 0.dp, 0.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.Top),
        ) {
            val treinosSeparadosPorSemana = TreinoByWeekMapper(treinos).getList()
            items(treinosSeparadosPorSemana.size) { i ->
                val data = DateUtil.format(treinosSeparadosPorSemana[i].first)
                val treinosSemana = treinosSeparadosPorSemana[i].second
                TreinoSemanaItem(
                    treinosSeparadosPorSemana.size - i,
                    data,
                    treinosSemana,
                    onClick = { navigate(it.id) },
                    onDelete = { deleteTreino(it) })
            }
        }
    }
}

@Composable
fun ListaTreinosPlanilhaScreen(
    navigate: (Int) -> Unit, viewModel: TreinosPlanilhaViewModel = koinViewModel()
) {
    _ListaTreinosPlanilhaScreen(
        navigate,
        viewModel.getTreinosByPlanilha(),
        { it, it1 -> viewModel.insertTreino(it, it1) },
        viewModel::deleteTreino
    )
}

@Preview
@Composable
private fun ListaTreinosPlanilhaScreenPreview() {
    MyFittTheme {
        _ListaTreinosPlanilhaScreen()
    }
}

//@Composable
//private fun InsertionTopBar(
//    onAddClicked: suspend (String) -> Unit,
//    textHint: String,
//    suffixText: String? = null,
//    icon: ImageVector? = null,
//    onIconClick: () -> Unit = {}
//) {
//    var nomeDoTreino = remember { mutableStateOf("") }
//    val scope = rememberCoroutineScope()
//    Row(
//        modifier = Modifier.fillMaxWidth(),
//        horizontalArrangement = Arrangement.spacedBy(8.dp),
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//        DefaultTextField(textValue = nomeDoTreino,
//            hint = textHint,
//            suffixText = suffixText,
//            modifier = Modifier
//                .width(IntrinsicSize.Max)
//                .weight(1f),
//            icon = icon,
//            onIconClick = { onIconClick() })
//        Button(modifier = Modifier
//            .height(56.dp)
//            .width(64.dp)
//            .background(
//                color = MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(10)
//            ), onClick = {
//            scope.launch {
//                onAddClicked(nomeDoTreino.value)
//            }
//        }) {
//            Icon(Icons.Default.Add, "Criar")
//        }
//    }
//}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DatePickerDialog(
    isDateDialogShown: MutableState<Boolean>, onDateSelected: (LocalDate) -> Unit
) {
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = DateUtil.toMillisUtcFromLocal(
            DateUtil.now
        )
    )
    if (isDateDialogShown.value) {
        DatePickerDialog(onDismissRequest = { isDateDialogShown.value = false }, confirmButton = {
            Button(onClick = {
                isDateDialogShown.value = false
                onDateSelected(
                    DateUtil.fromMillisUtcToLocal(
                        datePickerState.selectedDateMillis ?: DateUtil.toMillisUtcFromLocal(
                            DateUtil.now
                        )
                    )
                )
            }) { Text("Ok") }
        }) {
            DatePicker(datePickerState, showModeToggle = false)
        }
    }
}

