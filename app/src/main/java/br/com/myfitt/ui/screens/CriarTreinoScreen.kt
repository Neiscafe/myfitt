package br.com.myfitt.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import br.com.myfitt.domain.models.Divisao
import br.com.myfitt.domain.models.Ficha
import br.com.myfitt.domain.models.Treino
import br.com.myfitt.domain.utils.DateUtil
import br.com.myfitt.ui.components.DropdownTextField
import br.com.myfitt.ui.viewModels.CriarTreinoViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CriarTreinoScreen(
    planilhaId: Int,
    navigate: (Int) -> Unit,
    viewModel: CriarTreinoViewModel = koinViewModel(),
    modifier: Modifier = Modifier
) {
    var nomeDoTreino by remember { mutableStateOf("") }
    var fichaSelecionada by remember { mutableStateOf<Ficha?>(null) }
    var divisaoSelecionada by remember { mutableStateOf<Divisao?>(null) }
    var fichaEnabled by remember { mutableStateOf(false) }
    var fichas = viewModel.fichas.collectAsState()
    var divisoes = viewModel.divisoes.collectAsState()
    var dataDoTreinoState by remember { mutableStateOf(DateUtil.now) }
    val datePickerState =
        rememberDatePickerState(initialSelectedDateMillis = DateUtil.toMillisUtcFromLocal(DateUtil.now))
    val dataTreino = DateUtil.format(dataDoTreinoState)
    var isDateDialogShown by remember { mutableStateOf(false) }
    var isLoadingState by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        if (isLoadingState) {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.6f))
            ) {
                CircularProgressIndicator()
            }
        }
        if (isDateDialogShown) {
            DatePickerDialog(onDismissRequest = { isDateDialogShown = false }, confirmButton = {
                Button(onClick = {
                    isDateDialogShown = false
                    dataDoTreinoState = DateUtil.fromMillisUtcToLocal(
                        datePickerState.selectedDateMillis
                            ?: DateUtil.toMillisUtcFromLocal(DateUtil.now)
                    )
                }) { Text("Ok") }
            }) {
                DatePicker(datePickerState, showModeToggle = false)
            }
        }
        Spacer(Modifier.height(32.dp))
        OutlinedTextField(
            value = nomeDoTreino,
            placeholder = {
                Text("Crie seu treino...")
            },
            modifier = Modifier.fillMaxWidth(),
            onValueChange = { nomeDoTreino = it },
            textStyle = TextStyle(Color.Black),
            singleLine = true
        )
        DropdownTextField<Divisao>(
            divisoes.value,
            getValue = { (it?.nome ?: "NENHUM").uppercase() },
            onSelectedChanged = {
                divisaoSelecionada = it
                fichaEnabled = it != null
                it?.let {
                    viewModel.getFichas(it.id)
                }
            })

        DropdownTextField<Ficha>(
            fichas.value,
            getValue = { (it?.nome ?: "NENHUM").uppercase() },
            onSelectedChanged = { fichaSelecionada = it }, enabled = fichaEnabled
        )

        OutlinedTextField(
            value = dataTreino,
            onValueChange = {},
            readOnly = true,
            modifier = Modifier
                .fillMaxWidth()
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
        Button(onClick = {
            coroutineScope.launch {
                isLoadingState = true
                navigate(
                    viewModel.insertTreino(
                        Treino(
                            planilhaId = planilhaId,
                            nome = nomeDoTreino,
                            data = DateUtil.toDbNotation(dataDoTreinoState)
                        )
                    )
                )
            }
        }, content = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Outlined.Add, "")
                Spacer(Modifier.width(8.dp))
                Text("Criar")
            }
        })
    }
}