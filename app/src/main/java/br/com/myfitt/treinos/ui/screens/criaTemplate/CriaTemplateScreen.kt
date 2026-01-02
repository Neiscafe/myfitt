@file:OptIn(ExperimentalMaterial3Api::class)

package br.com.myfitt.treinos.ui.screens.criaTemplate

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.myfitt.R
import br.com.myfitt.common.domain.Exercicio
import br.com.myfitt.treinos.ui.theme.MyFittTheme

val sampleExercicio = Exercicio(0, "Supino bosta")

@Composable
fun CriaTemplateScreen(modifier: Modifier = Modifier) {
    Tela()
}

@Composable
fun ConfirmacaoDialog(
    exercicio: Exercicio, onDismiss: () -> Unit = {}
) {
    BasicAlertDialog(onDismiss) {
        Card() {
            Column(
                Modifier.padding(16.dp),
                Arrangement.spacedBy(16.dp)
            ) {
                Text("Remover ${exercicio.nome}?", style = MaterialTheme.typography.titleLarge)
                Row(Modifier.fillMaxWidth(), Arrangement.End) {
                    TextButton(onDismiss) {
                        Text("Cancelar")
                    }
                    TextButton({}) {
                        Text("Remover", Modifier, MaterialTheme.colorScheme.error)
                    }
                }
            }
        }
    }
}

@Composable
fun Tela(
    exercicios: List<Exercicio> = emptyList(),
    remover: (Exercicio) -> Unit = {},
    editarNomeTemplate: () -> Unit = {},
    irParaBuscaExercicios: () -> Unit = {},
    confirmaRemover: @Composable () -> Unit = {}
) {
    confirmaRemover()
    Scaffold(floatingActionButton = {
        FloatingActionButton(irParaBuscaExercicios) {
            Icon(Icons.Default.Add, "Adicionar exercício")
        }
    }) {
        Column(Modifier.padding(it), Arrangement.spacedBy(8.dp)) {
            Row(Modifier.fillMaxWidth()) {
                Text("Nome do template")
                IconButton(editarNomeTemplate) {
                    Icon(Icons.Default.Edit, "Editar nome template")
                }
            }
            exercicios.forEach {
                ExercicioItem(it, { remover(it) })
            }
        }
    }
}

@Composable
fun ExercicioItem(exercicio: Exercicio, onRemover: () -> Unit = {}) {
    val modifier = Modifier.fillMaxWidth()
    OutlinedCard(modifier, shape = RoundedCornerShape(0.dp)) {
        Row(modifier.padding(16.dp), Arrangement.SpaceBetween, Alignment.CenterVertically) {
            Text(exercicio.nome, Modifier)
            Row() {
                IconButton(onRemover) {
                    Icon(
                        Icons.Default.Delete,
                        "Deletar exercício",
                        Modifier,
                        MaterialTheme.colorScheme.error
                    )
                }
                IconButton(onRemover) {
                    Icon(
                        painter = painterResource(R.drawable.baseline_drag_handle_24),
                        contentDescription = "Arrastar exercício",
                        Modifier,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun ConfirmacaoDialogPreview() {
    MyFittTheme {
        Tela(confirmaRemover = { ConfirmacaoDialog(sampleExercicio) })
    }
}

@Preview
@Composable
private fun ExercicioItemPreview() {
    MyFittTheme {
        ExercicioItem(sampleExercicio)
    }
}

@Preview
@Composable
private fun CriaTemplateScreenPreview() {
    MyFittTheme {
        Tela()
    }
}