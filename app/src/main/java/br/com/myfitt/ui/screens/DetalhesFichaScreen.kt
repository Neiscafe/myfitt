package br.com.myfitt.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.material3.ExposedDropdownMenuDefaults.TrailingIcon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.myfitt.domain.models.Exercicio
import br.com.myfitt.domain.models.TreinoExercicioComNome
import br.com.myfitt.ui.components.ExercicioItem
import br.com.myfitt.ui.viewModels.DetalhesFichaViewModel
import br.com.myfitt.ui.viewModels.ExercicioViewModel
import br.com.myfitt.ui.viewModels.FichasViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalhesFichaScreen(
    fichaId: Int, viewModel: DetalhesFichaViewModel = koinViewModel(parameters = {
        parametersOf(fichaId)
    })
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    val exerciciosDoTreino by viewModel.getExerciciosByTreino()
        .collectAsStateWithLifecycle(
            initialValue = emptyList(), lifecycle = LocalLifecycleOwner.current.lifecycle
        )
    var exercicioDigitado by remember { mutableStateOf("") }
    val sugestoesDeExercicio by viewModel.getSugestoes(exercicioDigitado)
        .collectAsState(initial = emptyList())

    var dropDownExpanded by remember { mutableStateOf(sugestoesDeExercicio.isNotEmpty()) }
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
                    viewModel.deleteExercicio(showDialog.value!!)
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
            ExposedDropdownMenuBox(modifier = Modifier.fillMaxWidth(0.8f),
                expanded = dropDownExpanded,
                onExpandedChange = { dropDownExpanded = !dropDownExpanded }) {
                OutlinedTextField(value = exercicioDigitado,
                    keyboardOptions = KeyboardOptions(
                        autoCorrectEnabled = false, imeAction = ImeAction.Search
                    ),
                    keyboardActions = KeyboardActions(onSearch = {
                        if (exercicioDigitado.isNotEmpty()) {
                            viewModel.insertExercicio(
                                TreinoExercicioComNome(
                                    treinoId = fichaId,
                                    exercicioNome = exercicioDigitado,
                                    posicao = exerciciosDoTreino.size,
                                )
                            )
                            exercicioDigitado = ""
                        }
                    }),
                    onValueChange = {
                        viewModel.getSugestoes(it)
                        exercicioDigitado = it
                    },
                    modifier = Modifier
                        .menuAnchor(MenuAnchorType.PrimaryEditable)
                        .weight(1f)
                        .fillMaxWidth(),
                    trailingIcon = { TrailingIcon(expanded = dropDownExpanded) },
                    textStyle = TextStyle(Color.Black),
                    singleLine = true
                )
                ExposedDropdownMenu(
                    dropDownExpanded,
                    onDismissRequest = { dropDownExpanded = false }) {
                    sugestoesDeExercicio.forEach {
                        DropdownMenuItem(onClick = {
                            viewModel.insertExercicio(
                                TreinoExercicioComNome(
                                    treinoId = fichaId,
                                    exercicioNome = it.nome,
                                    posicao = exerciciosDoTreino.size,
                                )
                            )
                            exercicioDigitado = ""
                        }, trailingIcon = {
                            Icon(Icons.Default.Delete, "Delete", modifier = Modifier.clickable {
                                showDialog.value = it
                            })
                        }, text = {
                            Text(text = it.nome)
                        })
                    }
                }
            }
            Button(modifier = Modifier
                .fillMaxHeight()
                .background(
                    color = MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(10)
                ), onClick = {
                if (exercicioDigitado.isNotEmpty()) {
                    val novoExercicio = TreinoExercicioComNome(
                        treinoId = fichaId,
                        exercicioNome = exercicioDigitado,
                        posicao = exerciciosDoTreino.size,
                    )
                    viewModel.insertExercicio(novoExercicio)
                    exercicioDigitado = ""
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
                        onDelete = { viewModel.deleteExercicioDoTreino(exercicio) },
                        onMoveUp = { viewModel.moveExerciseUpByOne(exercicio) },
                        onMoveDown = { viewModel.moveExerciseDownByOne(exercicio) },
                        onUpdate = { it, mudou ->
                            viewModel.updateTreinoExercicio(
                                it, mudou
                            )
                        })
                }
            }
        }
    }
}
