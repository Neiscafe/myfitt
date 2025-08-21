package br.com.myfitt.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import br.com.myfitt.domain.models.Exercicio
import br.com.myfitt.domain.models.ExercicioTreino
import br.com.myfitt.domain.models.Serie
import br.com.myfitt.ui.screens.exerciciosTreino.ListaExerciciosActions
import br.com.myfitt.ui.theme.MyFittTheme
import kotlinx.collections.immutable.toImmutableList

@Composable
fun ExercicioItem(
    exercicioTreino: ExercicioTreino,
    actions: (ListaExerciciosActions) -> Unit = {},
) {
    val seriesLista by remember { mutableStateOf(exercicioTreino.seriesLista.toImmutableList()) }
    val showHistoryDialog = remember { mutableStateOf<ExercicioTreino?>(null) }
    val rememberedActions by remember { mutableStateOf(actions) }
    val scope = rememberCoroutineScope()
    ShowDialog()
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 64.dp)
            .height(IntrinsicSize.Min)
            .padding(8.dp),
    ) {
        Text(
            exercicioTreino.exercicio.nome, style = MaterialTheme.typography.titleMedium
        )
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Button({
                showHistoryDialog.value = exercicioTreino
            }, colors = ButtonDefaults.buttonColors().copy(containerColor = Color.White)) {
                Row(horizontalArrangement = Arrangement.Center) {
                    Icon(Icons.Default.DateRange, null, tint = Color.Black)
                    Spacer(Modifier.width(8.dp))
                    Text("Histórico", color = Color.Black)
                }
            }
            Row {
                val iconModifier = Modifier.size(20.dp)
                IconButton(onClick = {
                    rememberedActions(ListaExerciciosActions.MoveDownExercicioTreino(exercicioTreino))
                }) {
                    Icon(
                        modifier = iconModifier,
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = "Elevar"
                    )
                }
                IconButton(onClick = {
                    rememberedActions(ListaExerciciosActions.MoveUpExercicioTreino(exercicioTreino))
                }) {
                    Icon(
                        modifier = iconModifier,
                        imageVector = Icons.Default.KeyboardArrowUp,
                        contentDescription = "Abaixar"
                    )
                }
                IconButton(onClick = {
                    rememberedActions(ListaExerciciosActions.DeleteExercicioTreino(exercicioTreino))
                }) {
                    Icon(
                        modifier = iconModifier,
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Remover"
                    )
                }
            }
        }
        Column {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                Text(
                    "Descanso(s)",
                    textAlign = TextAlign.End,
                    modifier = Modifier.width(100.dp),
                    fontSize = TextUnit(
                        14f, TextUnitType.Sp
                    )
                )
                Text(
                    "Reps",
                    textAlign = TextAlign.End,
                    modifier = Modifier.width(100.dp),
                    fontSize = TextUnit(
                        14f, TextUnitType.Sp
                    )
                )
                Text(
                    "Peso(kg)",
                    textAlign = TextAlign.End,
                    modifier = Modifier.width(100.dp),
                    fontSize = TextUnit(
                        14f, TextUnitType.Sp
                    )
                )
            }
            for (serie in seriesLista) {
                key(serie.id) {
                    SerieItem(
                        serie,
                        rememberedActions,
                    )
                }
            }
        }
        IconButton({
            rememberedActions(
                ListaExerciciosActions.AddSerie(
                    Serie(
                        id = 0,
                        pesoKg = 0f,
                        segundosDescanso = 0,
                        reps = 0,
                        exercicioTreinoId = exercicioTreino.id
                    )
                )
            )
        }) { Icon(Icons.Default.Add, null, tint = Color.White) }
    }
}

@Composable
fun SerieItem(
    it: Serie,
    actions: (ListaExerciciosActions) -> Unit = {},
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 64.dp),
        horizontalArrangement = Arrangement.End
    ) {
        IconButton({
            actions(ListaExerciciosActions.DeleteSerie(it))
        }) { Icon(Icons.Default.Close, null) }
        SideEffectTextField(
            it.segundosDescanso.toString(),
            modifier = Modifier
                .height(40.dp)
                .wrapContentHeight(),
            onUpdate = { updated ->
                actions(ListaExerciciosActions.UpdateSerie(it.copy(segundosDescanso = updated.toInt())))
            })
        SideEffectTextField(
            it.reps.toString(),
            modifier = Modifier
                .height(40.dp)
                .wrapContentHeight(),
            onUpdate = { updated ->
                actions(ListaExerciciosActions.UpdateSerie(it.copy(reps = updated.toInt())))
            })
        SideEffectTextField(
            it.pesoKg.toInt().toString(),
            modifier = Modifier
                .height(40.dp)
                .wrapContentHeight(),
            onUpdate = { updated ->
                actions(ListaExerciciosActions.UpdateSerie(it.copy(pesoKg = updated.toFloat())))
            })
    }

}

@Preview
@Composable
private fun ExercicioItemPreview() {
    MyFittTheme {
        Surface {
            ExercicioItem(
                exercicioTreino = ExercicioTreino(
                    0, 0, Exercicio("Supino inclinado", 0), 0, null, listOf()
                ),
            )
        }
    }
}

@Composable
fun ShowDialog(modifier: Modifier = Modifier) {
    //    if (showHistoryDialog.value != null) {
//        val dialogData =
//            remember { mutableStateOf<Loadable<List<HistoricoExercicioTreinos>?>>(Loadable.Loading) }
//        LaunchedEffect(Unit) {
//            scope.launch {
//                showHistory(showHistoryDialog.value!!).collect {
//                    dialogData.value = it
//                }
//            }
//        }
//        Dialog({ showHistoryDialog.value = null }) {
//            Card(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(500.dp),
//                shape = RoundedCornerShape(16.dp),
//            ) {
//                when (val result = dialogData.value) {
//                    is Loadable.Loaded -> {
//                        if (result == null) {
//                            Icon(
//                                Icons.Default.Close,
//                                null,
//                                modifier = Modifier
//                                    .width(48.dp)
//                                    .height(48.dp)
//                            )
//                            return@Card
//                        }
//                        LazyColumn(
//                            modifier = Modifier.padding(all = 8.dp),
//                            verticalArrangement = Arrangement.spacedBy(8.dp)
//                        ) {
//                            itemsIndexed(
//                                items = result.data!!, key = { i, it -> it.serieId }) { i, it ->
//                                if (i == 0) {
//                                    Column(
//                                        modifier = Modifier.fillMaxWidth(),
//                                        horizontalAlignment = Alignment.CenterHorizontally
//                                    ) {
//                                        Text(
//                                            "Histórico", style = MaterialTheme.typography.titleLarge
//                                        )
//                                        Text(showHistoryDialog.value?.exercicio?.nome ?: "")
//                                    }
//                                }
//                                Card(
//                                    modifier = Modifier.fillMaxWidth(),
//                                    colors = CardDefaults.cardColors()
//                                        .copy(containerColor = Color.Gray)
//                                ) {
//                                    Column(
//                                        modifier = Modifier
//                                            .fillMaxWidth()
//                                            .padding(4.dp)
//                                    ) {
//                                        Text(DateUtil.format(DateUtil.fromDbNotation(it.dataTreino)))
//                                        Row(
//                                            horizontalArrangement = Arrangement.End,
//                                            modifier = Modifier.fillMaxWidth()
//                                        ) {
//                                            Column(
//                                                Modifier.width(100.dp),
//                                                horizontalAlignment = Alignment.End
//                                            ) {
//                                                Text(
//                                                    "Descanso(s)",
//                                                    //                                                color = Color.Black,
//                                                    fontSize = TextUnit(14f, TextUnitType.Sp)
//                                                )
//                                                Text(it.segundosDescanso.toString())
//                                            }
//                                            Column(
//                                                Modifier.width(100.dp),
//                                                horizontalAlignment = Alignment.End
//                                            ) {
//                                                Text(
//                                                    "Reps",
//                                                    fontSize = TextUnit(14f, TextUnitType.Sp),
//                                                    //                                                color = Color.Black
//                                                )
//                                                Text(it.repeticoes.toString())
//                                            }
//                                            Column(
//                                                Modifier.width(100.dp),
//                                                horizontalAlignment = Alignment.End
//                                            ) {
//                                                Text(
//                                                    "Peso(kg)",
//                                                    fontSize = TextUnit(14f, TextUnitType.Sp),
//                                                    //                                                color = Color.Black
//                                                )
//                                                Text(it.pesoKg.toString())
//                                            }
//                                        }
//                                    }
//                                }
//                            }
//                        }
//                    }
//
//                    Loadable.Loading -> {
//                        Box(
//                            contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()
//                        ) {
//                            CircularProgressIndicator()
//                        }
//                    }
//                }
//            }
//        }
//    }

}
