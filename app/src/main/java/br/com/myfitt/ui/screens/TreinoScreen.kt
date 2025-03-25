package br.com.myfitt.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.myfitt.domain.models.Treino
import br.com.myfitt.ui.components.TreinoSemanaItem
import br.com.myfitt.ui.viewModels.TreinoViewModel
import org.koin.androidx.compose.koinViewModel
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.WeekFields
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TreinoScreen(
    planilhaId: Int,
    treinoViewModel: TreinoViewModel = koinViewModel(),
    navigate: (Int) -> Unit,
) {
    val treinos by treinoViewModel.getTreinosByPlanilha(planilhaId)
        .collectAsState(initial = emptyList())
    var nomeDoTreino by remember { mutableStateOf("") }
    var dataDoTreinoState by remember { mutableStateOf(DateUtil.now) }
    val datePickerState =
        rememberDatePickerState(initialSelectedDateMillis = DateUtil.toMillisUtcFromLocal(DateUtil.now))
    val dataTreino = DateUtil.format(dataDoTreinoState)
    var isDateDialogShown by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier.padding(10.dp, 30.dp, 10.dp, 0.dp)
    ) {
        if (isDateDialogShown) {
            DatePickerDialog(onDismissRequest = { isDateDialogShown = false }, confirmButton = {
                Button(onClick = {
                    isDateDialogShown = false
                    dataDoTreinoState = DateUtil.fromMillisUtcToLocal(
                        datePickerState.selectedDateMillis ?: DateUtil.toMillisUtcFromLocal(DateUtil.now)
                    )
                }) { Text("Ok") }
            }) {
                DatePicker(datePickerState, showModeToggle = false)
            }
        }
        Text("Treinos da Planilha", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(16.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(value = nomeDoTreino,
                placeholder = {
                    Text("Crie seu treino...")
                },
                modifier = Modifier.fillMaxWidth(0.5f),
                onValueChange = { nomeDoTreino = it },
                textStyle = TextStyle(Color.Black),
                singleLine = true
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = dataTreino,
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier
                        .fillMaxWidth(0.6f)
                        .clickable {
                            isDateDialogShown = true
                        },
                    trailingIcon = {
                        Icon(Icons.Default.DateRange,
                            "Select date",
                            modifier = Modifier.clickable { isDateDialogShown = true })
                    },
                    textStyle = TextStyle(Color.LightGray),
                    singleLine = true,
                )
                Button(modifier = Modifier
                    .fillMaxHeight()
                    .background(
                        color = MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(10)
                    ), onClick = {
                    if (dataTreino.isNotEmpty()) {
                        val novoTreino = Treino(
                            planilhaId = planilhaId,
                            nome = nomeDoTreino,
                            data = DateUtil.toDbNotation(dataDoTreinoState)
                        )
                        treinoViewModel.insertTreino(novoTreino)
                    }
                }) {
                    Icon(Icons.Default.Add, "Criar")
                }
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
                TreinoSemanaItem(i + 1,
                    data,
                    treinosSemana,
                    onClick = { navigate(it.id) },
                    onDelete = { treinoViewModel.deleteTreino(it) })
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TreinoScreenPreview() {
//    TreinoScreen(planilhaId = "planilha1")
}

object DateUtil {
    private val dateTimeFormater =
        DateTimeFormatter.ofPattern("dd/MM").withZone(ZoneId.systemDefault())
    private val dbFormatter =
        DateTimeFormatter.ofPattern("yyyy-MM-dd").withZone(ZoneId.systemDefault())
    val now get() = LocalDate.now()
    fun format(date: LocalDate): String {
        return date.format(dateTimeFormater)
    }

    fun fromMillisUtcToLocal(millis: Long): LocalDate {
        return LocalDateTime.from(Instant.ofEpochMilli(millis).atZone(ZoneId.of("UTC"))).atZone(
            ZoneId.systemDefault()).toLocalDate()
    }

    fun fromDbNotation(date: String): LocalDate {
        return LocalDate.parse(date, dbFormatter)
    }

    fun toDbNotation(date: LocalDate): String {
        return date.format(dbFormatter)
    }

    fun toMillis(date: LocalDate): Long {
        return Instant.from(date.atStartOfDay().atZone(ZoneId.systemDefault())).toEpochMilli()
    }
    fun toMillisUtcFromLocal(date: LocalDate): Long {
        return Instant.from(date.atStartOfDay().atZone(ZoneId.of("UTC"))).toEpochMilli()
    }
}