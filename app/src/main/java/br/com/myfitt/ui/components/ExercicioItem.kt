package br.com.myfitt.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import br.com.myfitt.domain.models.ExercicioMudou
import br.com.myfitt.domain.models.HistoricoExercicioTreinos
import br.com.myfitt.domain.models.TreinoExercicioComNome
import br.com.myfitt.domain.utils.DateUtil
import br.com.myfitt.ui.theme.MyFittTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

@Composable
fun ExercicioItem(
    exercicios: List<TreinoExercicioComNome>,
    onDelete: () -> Unit = {},
    onMoveUp: () -> Unit = {},
    onMoveDown: () -> Unit = {},
    onShowHistory: (TreinoExercicioComNome) -> Flow<Loadable<List<HistoricoExercicioTreinos>?>> = { flowOf() },
    onUpdatedSeries: (TreinoExercicioComNome, ExercicioMudou) -> Unit = { _, _ -> }
) {
    val showHistoryDialog = remember { mutableStateOf<TreinoExercicioComNome?>(null) }
    val exercicio = exercicios.first()
    val scope = rememberCoroutineScope()
    if (showHistoryDialog.value != null) {
        val dialogData =
            remember { mutableStateOf<Loadable<List<HistoricoExercicioTreinos>?>>(Loadable.Loading) }
        LaunchedEffect(Unit) {
            scope.launch {
                onShowHistory(showHistoryDialog.value!!).collect {
                    dialogData.value = it
                }
            }
        }
        Dialog({ showHistoryDialog.value = null }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(500.dp),
//                    .padding(16.dp)

                shape = RoundedCornerShape(16.dp),
            ) {
                when (val result = dialogData.value) {
                    is Loadable.Loaded -> {
                        if (result == null) {
                            Icon(
                                Icons.Default.Close,
                                null,
                                modifier = Modifier
                                    .width(48.dp)
                                    .height(48.dp)
                            )
                            return@Card
                        }
                        LazyColumn(
                            modifier = Modifier.padding(all = 8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            itemsIndexed(
                                items = result.data!!,
                                key = { i, it -> it.serieId }) { i, it ->
                                if (i == 0) {
                                    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text(
                                            "Histórico",
                                            style = MaterialTheme.typography.titleLarge
                                        )
                                        Text(showHistoryDialog.value?.exercicioNome ?: "")
                                    }
                                }
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = CardDefaults.cardColors()
                                        .copy(containerColor = Color.Gray)
                                ) {
                                    Column(modifier = Modifier.fillMaxWidth().padding(4.dp), ) {
                                        Text(DateUtil.format(DateUtil.fromDbNotation(it.dataTreino)))
                                        Row(
                                            horizontalArrangement = Arrangement.End,
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Column(
                                                Modifier.width(100.dp),
                                                horizontalAlignment = Alignment.End
                                            ) {
                                                Text(
                                                    "Descanso(s)",
                                                    //                                                color = Color.Black,
                                                    fontSize = TextUnit(14f, TextUnitType.Sp)
                                                )
                                                Text(it.segundosDescanso.toString())
                                            }
                                            Column(
                                                Modifier.width(100.dp),
                                                horizontalAlignment = Alignment.End
                                            ) {
                                                Text(
                                                    "Reps",
                                                    fontSize = TextUnit(14f, TextUnitType.Sp),
                                                    //                                                color = Color.Black
                                                )
                                                Text(it.repeticoes.toString())
                                            }
                                            Column(
                                                Modifier.width(100.dp),
                                                horizontalAlignment = Alignment.End
                                            ) {
                                                Text(
                                                    "Peso(kg)",
                                                    fontSize = TextUnit(14f, TextUnitType.Sp),
                                                    //                                                color = Color.Black
                                                )
                                                Text(it.pesoKg.toString())
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    Loadable.Loading -> {
                        Box(
                            contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }
            }
        }
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .padding(8.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                exercicio.exercicioNome, style = MaterialTheme.typography.titleMedium
            )
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Button({
                showHistoryDialog.value = exercicio
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
                    onMoveDown()
                }) {
                    Icon(
                        modifier = iconModifier,
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = "Elevar"
                    )
                }
                IconButton(onClick = { onMoveUp() }) {
                    Icon(
                        modifier = iconModifier,
                        imageVector = Icons.Default.KeyboardArrowUp,
                        contentDescription = "Abaixar"
                    )
                }
                IconButton(onClick = { onDelete() }) {
                    Icon(
                        modifier = iconModifier,
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Remover"
                    )
                }
            }
        }
        Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.End) {
            Row() {
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
            exercicios.forEach {
                if (it.serieId == 0) return@forEach
                Row {
                    IconButton({
                        onUpdatedSeries(
                            it, ExercicioMudou.REMOVER
                        )
                    }) { Icon(Icons.Default.Close, null) }
                    SideEffectTextField(
                        it.segundosDescanso.toString(),
                        modifier = Modifier
                            .height(40.dp)
                            .wrapContentHeight(),
                        onUpdate = { updated ->
                            onUpdatedSeries(
                                it.copy(segundosDescanso = updated.toInt()), ExercicioMudou.DESCANSO
                            )
                        })
                    SideEffectTextField(
                        it.repeticoes.toString(),
                        modifier = Modifier
                            .height(40.dp)
                            .wrapContentHeight(),
                        onUpdate = { updated ->
                            onUpdatedSeries(
                                it.copy(repeticoes = updated.toInt()), ExercicioMudou.REPS
                            )
                        })
                    SideEffectTextField(
                        it.pesoKg.toInt().toString(),
                        modifier = Modifier
                            .height(40.dp)
                            .wrapContentHeight(),
                        onUpdate = { updated ->
                            onUpdatedSeries(
                                it.copy(pesoKg = updated.toFloat()), ExercicioMudou.PESO
                            )
                        })
                }
            }
            IconButton({
                onUpdatedSeries(
                    exercicio.copy(
                        serieId = 0, pesoKg = 0f, segundosDescanso = 0, repeticoes = 0
                    ), ExercicioMudou.ADICIONAR
                )
            }) { Icon(Icons.Default.Add, null, tint = Color.White) }
        }
    }
}

@Preview
@Composable
private fun ExercicioItemPreview() {
    MyFittTheme {
        Surface {
            ExercicioItem(
                listOf(
                    TreinoExercicioComNome(
                        0, 0, "akosmdkoasmdako", segundosDescanso = 180, serieId = 1
                    ),
                    TreinoExercicioComNome(
                        0, 0, "akosmdkoasmdakosd", segundosDescanso = 180, serieId = 2
                    ),
                    TreinoExercicioComNome(
                        0, 0, "akosmdkoasmdakosd", segundosDescanso = 180, serieId = 3
                    ),
                    TreinoExercicioComNome(
                        0, 0, "akosmdkoasmdakosd", segundosDescanso = 180, serieId = 5
                    ),
                    TreinoExercicioComNome(
                        0,
                        0,
                        "akosmdkoasmdakosd",
                        segundosDescanso = 180,
                        pesoKg = 180f,
                        repeticoes = 8,
                        serieId = 6
                    ),
                )
            )

        }
    }
}
