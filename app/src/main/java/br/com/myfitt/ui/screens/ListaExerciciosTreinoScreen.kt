package br.com.myfitt.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.myfitt.domain.models.Exercicio
import br.com.myfitt.ui.components.ExercicioItem
import br.com.myfitt.ui.components.SuggestionDropdown
import br.com.myfitt.ui.viewModels.ExerciciosTreinoViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaExerciciosTreinoScreen(
    treinoId: Int,
    exerciciosTreinoViewModel: ExerciciosTreinoViewModel = koinViewModel(parameters = {
        parametersOf(treinoId)
    })
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    val exerciciosDoTreino by exerciciosTreinoViewModel.exerciciosByTreino.collectAsState()
    val exercicioDigitado = remember { mutableStateOf("") }
    var showDialog = remember { mutableStateOf<Exercicio?>(null) }
    if (showDialog.value != null) {
        AlertDialog( // 3
            onDismissRequest = { // 4
                showDialog.value = null
            },
            // 5
            title = { Text(text = "Excluir exercício") },
            text = { Text(text = "Deseja realmente desativar o exercício ${showDialog.value!!.nome.uppercase()}\n* Isso não afeta seus treinos anteriores *") },
            dismissButton = {
                Button(onClick = { showDialog.value = null }) {
                    Text(text = "Cancelar", color = Color.White)
                }
            },
            confirmButton = { // 6
                Button(onClick = {
                    exerciciosTreinoViewModel.deleteExercicio(showDialog.value!!)
                    showDialog.value = null
                }) {
                    Text(
                        text = "Excluir", color = Color.White
                    )
                }
            })
    }
    Column(
        modifier = Modifier.padding(10.dp, 30.dp, 10.dp, 0.dp)
    ) {
        Row {
            Text("Exercícios do Treino", style = MaterialTheme.typography.titleLarge)
            Button(modifier = Modifier
                .height(IntrinsicSize.Min)
                .background(
                    color = MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(10)
                ), onClick = {
                val text =
                    exerciciosDoTreino.map { "${it.exercicioNome}:\n\tÚltimo treino: ${it.seriesUltimoTreino} x ${it.repeticoesUltimoTreino} - ${it.pesoKgUltimoTreino}\n\tAtual: ${it.series} x ${it.repeticoes} - ${it.pesoKg}" }
                        .joinToString(separator = "\n")
                clipboardManager.setText(AnnotatedString(text))
                try {
                    context.startActivity(context.packageManager.getLaunchIntentForPackage("com.hasz.gymrats.app"))
                } catch (t: Throwable) {
                    Log.d("Erro", "$t")
                }
            }) {
                Text("Gym Rats")
            }
        }
        Spacer(Modifier.height(16.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SuggestionDropdown(
                exercicioDigitado,
                { exerciciosTreinoViewModel.getSugestoes(it) },
                onSuggestionClicked = {
                    exerciciosTreinoViewModel.insertExercicio(it)
                },
                trailingIcon = Icons.Outlined.Delete,
                onIconClick = { exerciciosTreinoViewModel.deleteExercicio(it) },
                modifier = Modifier.width(
                    IntrinsicSize.Min
                )
            )
            Button(modifier = Modifier
                .fillMaxHeight()
                .background(
                    color = MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(10)
                ), onClick = {
                if (exercicioDigitado.value.isNotEmpty()) {
                    exerciciosTreinoViewModel.insertExercicio(exercicioDigitado.value)
                    exercicioDigitado.value = ""
                }
            }) {
                Icon(Icons.Default.Add, "Adicionar")
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxHeight(),
            contentPadding = PaddingValues(0.dp, 8.dp, 0.dp, 0.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.Top),
        ) {
            items(exerciciosDoTreino.size, key = { exerciciosDoTreino[it].exercicioId }) { i ->
                val exercicio = exerciciosDoTreino[i]
                Card {
                    ExercicioItem(exercicio,
                        onDelete = { exerciciosTreinoViewModel.deleteExercicioDoTreino(exercicio) },
                        onMoveUp = { exerciciosTreinoViewModel.moveExerciseUpByOne(exercicio) },
                        onMoveDown = { exerciciosTreinoViewModel.moveExerciseDownByOne(exercicio) },
                        onUpdate = { it, mudou ->
                            exerciciosTreinoViewModel.updateTreinoExercicio(
                                it, mudou
                            )
                        })
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun ExercicioScreenPreview() {
//    ExercicioScreen(treinoId = 1)
}
