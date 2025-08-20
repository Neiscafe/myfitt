package br.com.myfitt.ui.screens.exerciciosTreino

import android.content.Intent
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.myfitt.MyFittTimerService
import br.com.myfitt.domain.models.Exercicio
import br.com.myfitt.domain.models.ExercicioTreino
import br.com.myfitt.domain.models.Ficha
import br.com.myfitt.domain.models.HistoricoExercicioTreinos
import br.com.myfitt.domain.models.Serie
import br.com.myfitt.ui.components.ExercicioItem
import br.com.myfitt.ui.components.Loadable
import br.com.myfitt.ui.components.SuggestionDropdown
import br.com.myfitt.ui.theme.MyFittTheme
import br.com.myfitt.ui.viewModels.ExerciciosTreinoViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun _ListaExerciciosTreinoScreen(
    exerciciosByTreino: () -> Flow<List<ExercicioTreino>> = { flow { emptyList<ExercicioTreino>() } },
    deleteExercicio: (Exercicio) -> Unit = {},
    addAndInsertExercicio: (String) -> Unit = {},
    insertExercicioTreino: (Exercicio) -> Unit = {},
    getSugestoes: suspend (String) -> List<Exercicio> = { emptyList() },
    deleteExercicioTreino: (ExercicioTreino) -> Unit = {},
    moveExercicioUp: (ExercicioTreino) -> Unit = {},
    moveExercicioDown: (ExercicioTreino) -> Unit = {},
    updateSerie: (Serie) -> Unit = { },
    addSerie: (Serie) -> Unit = { },
    deleteSerie: (Serie) -> Unit = { },
    getHistoricoExercicio: (ExercicioTreino) -> Flow<Loadable<List<HistoricoExercicioTreinos>?>> = {
        flowOf()
    },
    initial: List<ExercicioTreino> = emptyList()
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    val exerciciosDoTreino by exerciciosByTreino().collectAsState(initial)
    var selectedFicha by remember { mutableStateOf<Ficha?>(null) }
    val exercicioDigitado = remember { mutableStateOf("") }
    val showDialog = remember { mutableStateOf<Exercicio?>(null) }

    @Composable
    fun ApplyFichaButton(mostrar: Boolean, confirma: () -> Unit) {
        if (selectedFicha != null) {
            TextButton(onClick = { confirma() }) { Text("APLICAR") }
        }
    }

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
                    deleteExercicio(showDialog.value!!)
                    showDialog.value = null
                }) {
                    Text(
                        text = "Excluir", color = Color.White
                    )
                }
            })
    }
    Scaffold(Modifier.padding(8.dp), floatingActionButton = {
        FloatingActionButton({
            // vai para tela nova
        }) {
            Icon(
                Icons.Default.Email,
                null
            )
        }
    }) {
        Column(
            modifier = Modifier.padding(it)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Exercícios do Treino", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.weight(1f))
                TextButton(modifier = Modifier.height(IntrinsicSize.Min), onClick = {
                    val text = StringBuilder()
                    exerciciosDoTreino.forEach {
                        text.append("${it.exercicio.nome}:\n")
                        it.seriesLista.forEach {
                            text.append("${it.pesoKg}KG X ${it.reps} - ${it.segundosDescanso}s de intervalo.\n")
                        }
                    }
                    clipboardManager.setText(AnnotatedString(text.toString()))
                    try {
                        context.startActivity(context.packageManager.getLaunchIntentForPackage("com.hasz.gymrats.app"))
                    } catch (t: Throwable) {
                        Log.d("Erro", "$t")
                    }
                }) {
                    Text("Ir para Gym Rats", color = MaterialTheme.colorScheme.onBackground)
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
                    textState = exercicioDigitado,
                    getSuggestions = { getSugestoes(it) },
                    onSuggestionClicked = {
                        insertExercicioTreino(it)
                    },
                    trailingIcon = Icons.Default.Delete,
                    onIconClick = { deleteExercicio(it) },
                    modifier = Modifier.weight(1f),
                    getText = { it.nome })
                Button(
                    modifier = Modifier
                        .fillMaxHeight()
                        .background(
                            color = MaterialTheme.colorScheme.primary,
                            shape = RoundedCornerShape(10)
                        ), onClick = {
                        if (exercicioDigitado.value.isNotEmpty()) {
                            addAndInsertExercicio(exercicioDigitado.value)
                            exercicioDigitado.value = ""
                        }
                    }) {
                    Icon(Icons.Default.Add, "Adicionar")
                }
            }
//        Row(verticalAlignment = Alignment.CenterVertically) {
//            DropdownTextField(fichas.value.toNullableSpinnerList(), { it?.nome ?: "NENHUMA" }, {
//                selectedFicha = it
//            }, "FICHA"
//            , modifier = Modifier.weight(1f))
//            ApplyFichaButton(selectedFicha != null) {
//                viewModel.applyFicha(selectedFicha!!)
//
//            }
//        }
            LazyColumn(
                modifier = Modifier.fillMaxHeight(),
                contentPadding = PaddingValues(0.dp, 8.dp, 0.dp, 24.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.Top),
            ) {
                items(items = exerciciosDoTreino, key = { it.id }) {
                    Card {
                        ExercicioItem(
                            it,
                            deleteExercicioTreino = deleteExercicioTreino,
                            moveExercicioTreinoUp = moveExercicioUp,
                            moveExercicioTreinoDown = moveExercicioDown,
                            showHistory = getHistoricoExercicio,
                            removeSerie = deleteSerie,
                            addSerie = addSerie,
                            updateSerie = updateSerie,
                        )
                    }
                }
            }
        }

    }
}

@Composable
fun ListaExerciciosTreinoScreen(
    treinoId: Int, viewModel: ExerciciosTreinoViewModel = koinViewModel(parameters = {
        parametersOf(treinoId)
    })
) {
    _ListaExerciciosTreinoScreen(
        exerciciosByTreino = viewModel::exerciciosByTreino,
        deleteExercicio = viewModel::deleteExercicio,
        addAndInsertExercicio = viewModel::addAndInsertExercicio,
        insertExercicioTreino = viewModel::insertExercicio,
        getSugestoes = viewModel::getSugestoes,
        deleteExercicioTreino = viewModel::deleteExercicioDoTreino,
        moveExercicioUp = viewModel::moveExerciseUpByOne,
        moveExercicioDown = viewModel::moveExerciseDownByOne,
        getHistoricoExercicio = viewModel::getHistorico,
        updateSerie = viewModel::updateSerie,
        addSerie = viewModel::addSerie,
        deleteSerie = viewModel::deleteSerie,
    )
}

@Preview(showBackground = true)
@Composable
fun ListaExerciciosTreinoScreenPreview() {
    MyFittTheme {
        Surface {
            _ListaExerciciosTreinoScreen(
                initial = listOf(
                    ExercicioTreino(
                        id = 0,
                        treinoId = 0,
                        exercicio = Exercicio("Supino banco", 0),
                        posicao = 0,
                        seriesLista = listOf()
                    ),
                    ExercicioTreino(
                        id = 1,
                        treinoId = 0,
                        exercicio = Exercicio("Supino Mesa", 0),
                        posicao = 0,
                        seriesLista = listOf(Serie(1, 0, 20.5f, 30, 20))
                    )
                )
            )
        }
    }
}
