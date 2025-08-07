package br.com.myfitt.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.myfitt.domain.models.ExercicioMudou
import br.com.myfitt.domain.models.TreinoExercicioComNome
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

@Composable
fun ExercicioItem(
    exercicios: List<TreinoExercicioComNome>,
    onDelete: () -> Unit = {},
    onMoveUp: () -> Unit = {},
    onMoveDown: () -> Unit = {},
    onShowHistory: (TreinoExercicioComNome) -> Flow<Loadable<List<TreinoExercicioComNome>>> = { flowOf() },
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
            Surface(
                shape = MaterialTheme.shapes.medium, color = MaterialTheme.colorScheme.surface
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
                            return@Surface
                        }
                        LazyColumn() {
                            items(items = result.data!!, key = { it.serieId }) {
                                Card(modifier = Modifier.fillMaxWidth()) {
                                    Row() {
                                        Column(
                                            Modifier.width(100.dp),
                                            horizontalAlignment = Alignment.End
                                        ) {
                                            Text("Descanso(s)")
                                            Text(it.segundosDescanso.toString())
                                        }
                                        Column(
                                            Modifier.width(100.dp),
                                            horizontalAlignment = Alignment.End
                                        ) {
                                            Text("Reps")
                                            Text(it.repeticoes.toString())
                                        }
                                        Column(
                                            Modifier.width(100.dp),
                                            horizontalAlignment = Alignment.End
                                        ) {
                                            Text("Peso(kg)")
                                            Text(it.pesoKg.toString())
                                        }
                                    }
                                }
                            }
                        }
                    }

                    Loadable.Loading -> {
                        CircularProgressIndicator()
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
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            Modifier
                .fillMaxHeight()
                .fillMaxWidth(0.4f)
        ) {
            Text(
                exercicio.exercicioNome, style = MaterialTheme.typography.titleMedium
            )
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.Top
            ) {
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
            Button({
                showHistoryDialog.value = exercicio
            }, colors = ButtonDefaults.buttonColors().copy(containerColor = Color.White)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.DateRange, null, tint = Color.Black)
                    Spacer(Modifier.width(8.dp))
                    Text("Histórico", color = Color.Black)
                }
            }
        }
        Column(horizontalAlignment = Alignment.End) {
            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Column(horizontalAlignment = Alignment.End) {
                    Text("Interv. (s)")
                    exercicios.forEach {
                        if (it.serieId == 0) return@forEach
                        SideEffectTextField(
                            it.segundosDescanso.toString(),
                            modifier = Modifier
                                .height(40.dp)
                                .wrapContentHeight(),
                            onUpdate = { updated ->
                                onUpdatedSeries(
                                    it.copy(segundosDescanso = updated.toInt()),
                                    ExercicioMudou.DESCANSO
                                )
                            })
                        Spacer(Modifier.height(12.dp))
                    }
                }
                Column(horizontalAlignment = Alignment.End, modifier = Modifier.weight(1f)) {
                    Text("Reps")
                    exercicios.forEach {
                        if (it.serieId == 0) return@forEach
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
                        Spacer(Modifier.height(12.dp))
                    }
                }
                Column(
                    horizontalAlignment = Alignment.End, modifier = Modifier.weight(1f)
                ) {
                    Text("Kg")
                    exercicios.forEach {
                        if (it.serieId == 0) return@forEach
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
                        Spacer(Modifier.height(12.dp))
                    }
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
    ExercicioItem(
        listOf(
            TreinoExercicioComNome(0, 0, "akosmdkoasmdako", serieId = 1),
            TreinoExercicioComNome(0, 0, "akosmdkoasmdakosd", serieId = 2),
            TreinoExercicioComNome(0, 0, "akosmdkoasmdakosd", serieId = 3),
            TreinoExercicioComNome(0, 0, "akosmdkoasmdakosd", serieId = 5),
            TreinoExercicioComNome(0, 0, "akosmdkoasmdakosd", serieId = 6),
        )
    )
}
