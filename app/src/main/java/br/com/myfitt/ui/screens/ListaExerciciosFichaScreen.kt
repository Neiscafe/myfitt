package br.com.myfitt.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import br.com.myfitt.domain.models.Exercicio
import br.com.myfitt.ui.components.DefaultCard
import br.com.myfitt.ui.components.DropdownTextField
import br.com.myfitt.ui.components.InsertionTopBar
import br.com.myfitt.ui.components.SuggestionDropdown
import br.com.myfitt.ui.utils.toNullableSpinnerList
import br.com.myfitt.ui.utils.toUserFriendlyName
import br.com.myfitt.ui.viewModels.ExerciciosFichaViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaExerciciosFichaScreen(
    fichaId: Int, viewModel: ExerciciosFichaViewModel = koinViewModel(parameters = {
        parametersOf(fichaId)
    })
) {
    val ficha by viewModel.exerciciosFicha.collectAsState(emptyList())
    val tiposExercicio by viewModel.tiposExercicio.collectAsState()
    val exercicioDigitado = remember { mutableStateOf("") }
    val showDialog = remember { mutableStateOf<Exercicio?>(null) }
    if (showDialog.value != null) {
        Dialog(showDialog)
    }
    Column(
        modifier = Modifier.padding(10.dp, 30.dp, 10.dp, 0.dp)
    ) {
        InsertionTopBar(title = "Exercício Ficha", onAddClicked = {
            viewModel.insertExercicio(exercicioDigitado.value)
            exercicioDigitado.value = ""
        }, InsertionField = {
            SuggestionDropdown<Exercicio>(textState = exercicioDigitado,
                getSuggestions = { viewModel.getExerciciosSugestao(it) },
                onSuggestionClicked = {
                    viewModel.insertExercicioFicha(it.id)
                },
                trailingIcon = Icons.Default.Delete,
                onIconClick = { viewModel.deleteExercicio(it) },
                modifier = Modifier.weight(1f),
                getText = { it.nome })
        })
        LazyColumn(
            modifier = Modifier.fillMaxHeight(),
            contentPadding = PaddingValues(0.dp, 8.dp, 0.dp, 0.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.Top),
        ) {
            items(ficha.size, key = { ficha[it].id }) { i ->
                val exercicio = ficha[i]
                DefaultCard<Exercicio>(exercicio, { it.toUserFriendlyName() }, components = {
                    IconButton(onClick = {
                        viewModel.moveExerciseDown(exercicio)
                    }) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown, contentDescription = ""
                        )
                    }
                    IconButton(onClick = {
                        viewModel.moveExerciseUp(exercicio)
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
                    DropdownTextField(tiposExercicio.toNullableSpinnerList(),
                        { it?.nome ?: "NENHUM" },
                        { viewModel.setTipoExercicio(exercicio, it) },
                        "TIPO",
                        selected = exercicio.tipo
                    )
                })
            }
        }
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
