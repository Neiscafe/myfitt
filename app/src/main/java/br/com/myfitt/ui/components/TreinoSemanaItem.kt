package br.com.myfitt.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.com.myfitt.domain.models.Treino
import br.com.myfitt.ui.screens.DateUtil

@Composable
fun TreinoSemanaItem(
    posicao: Int,
    dataInicio: String,
    treinosSemana: List<Treino>,
    onClick: (Treino) -> Unit,
    onDelete: (Treino) -> Unit,
    modifier: Modifier = Modifier
) {
    var expandido by remember { mutableStateOf(false) }
    Card(onClick = { expandido = !expandido }) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Semana $posicao - $dataInicio",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(10.dp)
                )
                Icon(
                    imageVector = if (expandido) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = "Remover"
                )
            }
            if (expandido) {
                Column() {
                    treinosSemana.forEachIndexed { i, it ->
                        val treino = it
                        TreinoItem(treino, { onClick(treino) }, { onDelete(treino) })
                    }
                }
            }
        }
    }

}