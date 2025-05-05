package br.com.myfitt.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import br.com.myfitt.domain.models.Exercicio
import br.com.myfitt.ui.components.DefaultCard
import br.com.myfitt.ui.components.SuggestionDropdown
import br.com.myfitt.ui.viewModels.ExerciciosFichaViewModel
import kotlinx.coroutines.flow.map
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaExerciciosFichaScreen(
    fichaId: Int, viewModel: ExerciciosFichaViewModel = koinViewModel(parameters = {
        parametersOf(fichaId)
    })
) {
    val ficha by viewModel.ficha.collectAsState(emptyList())
    val exercicioDigitado = remember { mutableStateOf("") }
    val showDialog = remember { mutableStateOf<Exercicio?>(null) }
    if (showDialog.value != null) {
        Dialog(showDialog)
    }
    Column(
        modifier = Modifier.padding(10.dp, 30.dp, 10.dp, 0.dp)
    ) {
        Row {
            Text("Exercícios do Treino", style = MaterialTheme.typography.titleLarge)
        }
        Spacer(Modifier.height(16.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SuggestionDropdown<Exercicio>(textState = exercicioDigitado,
                getSuggestions = { viewModel.getExerciciosSugestao(it) },
                onSuggestionClicked = { viewModel.insertExercicioFicha(it.id) })
            Button(modifier = Modifier
                .fillMaxHeight()
                .background(
                    color = MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(10)
                ), onClick = {
                createExerciseAction(exercicioDigitado, viewModel)
            }) {
                Icon(Icons.Default.Add, "Adicionar")
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxHeight(),
            contentPadding = PaddingValues(0.dp, 8.dp, 0.dp, 0.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.Top),
        ) {
            items(ficha.size, key = { ficha[it].id }) { i ->
                val exercicio = ficha[i]
                DefaultCard<Exercicio>(exercicio, components = {
                    IconButton(onClick = {
                        viewModel.moveExercisePositionDown(exercicio)
                    }) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown, contentDescription = ""
                        )
                    }
                    IconButton(onClick = {
                        viewModel.moveExercisePositionUp(exercicio)
                    }) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowUp, contentDescription = ""
                        )
                    }
                    IconButton(onClick = { viewModel.removeExercise(exercicio) }) {
                        Icon(
                            imageVector = Icons.Default.Delete, contentDescription = ""
                        )
                    }
                })
            }
        }
    }
}

fun createExerciseAction(
    exercicioDigitado: MutableState<String>, viewModel: ExerciciosFichaViewModel
) {
    if (exercicioDigitado.value.isNotEmpty()) {
        val novoExercicio = Exercicio(
            nome = exercicioDigitado.value,
            tipo = null,
        )
        viewModel.insertExercicio(novoExercicio)
        exercicioDigitado.value = ""
    }
}

@Composable
private fun Dialog(showDialogState: MutableState<Exercicio?>) {
    AlertDialog( // 3
        onDismissRequest = { // 4
            showDialogState.value = null
        },
        // 5
        title = { Text(text = "Excluir exercício") },
        text = { Text(text = "Deseja realmente desativar o exercício ${showDialogState.value!!.nome.uppercase()}\n* Isso não afeta seus treinos anteriores *") },
        dismissButton = {
            Button(onClick = { showDialogState.value = null }) {
                Text(text = "Cancelar", color = Color.White)
            }
        },
        confirmButton = { // 6
            Button(onClick = {
//                    viewModel.deleteExercicio(showDialog!!)
                showDialogState.value = null
            }) {
                Text(
                    text = "Excluir", color = Color.White
                )
            }
        })
}
