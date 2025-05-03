package br.com.myfitt.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import br.com.myfitt.domain.models.Divisao
import br.com.myfitt.domain.models.Ficha
import br.com.myfitt.domain.models.Treino
import br.com.myfitt.domain.utils.DateUtil
import br.com.myfitt.ui.components.DropdownTextField
import br.com.myfitt.ui.components.TreinoSemanaItem
import br.com.myfitt.ui.viewModels.TreinosPlanilhaViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.WeekFields
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaTreinosPlanilhaScreen(
    planilhaId: Int,
    navigate: (Int) -> Unit,
    goToDivisoes: () -> Unit,
    treinosPlanilhaViewModel: TreinosPlanilhaViewModel = koinViewModel(),
) {
    val treinos by treinosPlanilhaViewModel.getTreinosByPlanilha(planilhaId)
        .collectAsState(initial = emptyList())
    var selectedFicha by remember { mutableStateOf<Ficha?>(null) }
    var selectedDivisao by remember { mutableStateOf<Divisao?>(null) }
    var dataSelecionada by remember { mutableStateOf(DateUtil.now) }
    val isDateDialogShown = remember { mutableStateOf(false) }
    Column(
        modifier = Modifier.padding(10.dp, 30.dp, 10.dp, 0.dp)
    ) {
        DatePickerDialog(isDateDialogShown) {
            dataSelecionada = it
        }
        Text("Treinos da Planilha", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(16.dp))
        InsertionTopBar(onAddClicked = {
            val novoTreino = Treino(
                planilhaId = planilhaId,
                nome = it,
                data = DateUtil.toDbNotation(dataSelecionada)
            )
            treinosPlanilhaViewModel.insertTreino(novoTreino)
        },
            textHint = "Crie seu treino...",
            suffixText = DateUtil.format(dataSelecionada),
            icon = Icons.Outlined.DateRange,
            onIconClick = { isDateDialogShown.value = true })
        Column {
            Row {
                DropdownTextField<Divisao>(
                    treinosPlanilhaViewModel.divisoes.collectAsState().value,
                    { it?.toString() ?: "Nenhuma" },
                    { selectedDivisao = it },
                    "Divisão",
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .height(
                            IntrinsicSize.Min
                        )
                )
                DropdownTextField<Ficha>(selectedDivisao?.let {
                    treinosPlanilhaViewModel.fichas(it.id).collectAsState().value
                } ?: listOf(null),
                    { it?.toString() ?: "Nenhuma" },
                    { selectedFicha = it },
                    "Próxima ficha",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(
                            IntrinsicSize.Min
                        )
                )
            }
            Button(modifier = Modifier.fillMaxWidth(), onClick = {
                goToDivisoes()
            }) {
                Text("Buscar divisões e fichas")
            }
        }
        LazyColumn(
            modifier = Modifier.fillMaxHeight(),
            contentPadding = PaddingValues(0.dp, 8.dp, 0.dp, 0.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.Top),
        ) {
            val weekFields = WeekFields.of(Locale.getDefault())
            val treinosSeparadosPorSemana = treinos.groupBy {
                DateUtil.fromDbNotation(it.data).with(
                    weekFields.dayOfWeek(), DayOfWeek.MONDAY.value.toLong()
                )
            }.toList()
            items(treinosSeparadosPorSemana.size) { i ->
                val data = DateUtil.format(treinosSeparadosPorSemana[i].first)
                val treinosSemana = treinosSeparadosPorSemana[i].second
                TreinoSemanaItem(treinosSeparadosPorSemana.size - i,
                    data,
                    treinosSemana,
                    onClick = { navigate(it.id) },
                    onDelete = { treinosPlanilhaViewModel.deleteTreino(it) })
            }
        }
    }
}

@Composable
private fun InsertionTopBar(
    onAddClicked: suspend (String) -> Unit,
    textHint: String,
    suffixText: String? = null,
    icon: ImageVector? = null,
    onIconClick: () -> Unit = {}
) {
    var nomeDoTreino = remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        DefaultTextField(textValue = nomeDoTreino,
            hint = textHint,
            suffixText = suffixText,
            modifier = Modifier
                .width(IntrinsicSize.Max)
                .weight(1f),
            icon = icon,
            onIconClick = { onIconClick() })
        Button(modifier = Modifier
            .height(56.dp)
            .width(64.dp)
            .background(
                color = MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(10)
            ), onClick = {
            scope.launch {
                onAddClicked(nomeDoTreino.value)
            }
        }) {
            Icon(Icons.Default.Add, "Criar")
        }
    }
}

@Composable
private fun DefaultTextField(
    textValue: MutableState<String>,
    hint: String,
    suffixText: String? = null,
    icon: ImageVector? = null,
    onIconClick: (String) -> Unit = {},
    modifier: Modifier = Modifier,
) {
    OutlinedTextField(
        value = textValue.value,
        onValueChange = {
            textValue.value = it
        },
        suffix = suffixText?.let {
            { Text(it) }
        },
        placeholder = {
            Text(hint)
        },
        trailingIcon = icon?.let {
            { Icon(it, "", Modifier.clickable { onIconClick(textValue.value) }) }
        },
        modifier = modifier,
        textStyle = TextStyle(Color.LightGray),
        singleLine = true,
    )
}

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

