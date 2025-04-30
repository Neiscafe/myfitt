package br.com.myfitt.ui.components

import androidx.compose.animation.core.estimateAnimationDurationMillis
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import br.com.myfitt.domain.models.Treino
import br.com.myfitt.domain.utils.DateUtil

@Composable
fun TreinoItem(
    treino: Treino,
    navigate: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showDialog = remember { mutableStateOf(false) }
    if (showDialog.value) {
        AlertDialog( // 3
            onDismissRequest = { // 4
                showDialog.value = false
            },
            // 5
            title = { Text(text = "Excluir treino") },
            text = { Text(text = "Deseja realmente excluir ${treino.nome.uppercase()}?") },
            dismissButton = {
                Button(onClick = { showDialog.value = false }) {
                    Text(text = "Cancelar", color = Color.White)
                }
            },
            confirmButton = { // 6
                Button(
                    onClick = {
                        onDelete()
                        showDialog.value = false
                    }
                ) {
                    Text(
                        text = "Excluir",
                        color = Color.White
                    )
                }
            }
        )
    }
    Card(onClick = navigate) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "${DateUtil.format(DateUtil.fromDbNotation(treino.data))} - ${treino.nome}",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(10.dp)
            )
            IconButton(onClick = {showDialog.value = true }) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = "Remover")
            }
        }
    }
}