package br.com.myfitt.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.myfitt.domain.utils.DateUtil
import br.com.myfitt.ui.components.TreinoSemanaItem
import br.com.myfitt.ui.viewModels.TreinoViewModel
import org.koin.androidx.compose.koinViewModel
import java.time.DayOfWeek
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

    Scaffold(floatingActionButton = {
        ExtendedFloatingActionButton(onClick = { navigate(planilhaId) }, content = {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Outlined.Add, "")
                Text("ADICIONAR")
            }
        })
    }) { padding ->
        Column(
            modifier = Modifier.padding(10.dp, 30.dp, 10.dp, 0.dp)
        ) {
            Text("Treinos da Planilha", style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(16.dp))
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
                        onDelete = { treinoViewModel.deleteTreino(it) })
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TreinoScreenPreview() {
//    TreinoScreen(planilhaId = "planilha1")
}

