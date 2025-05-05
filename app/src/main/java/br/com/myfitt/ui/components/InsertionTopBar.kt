package br.com.myfitt.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun InsertionTopBar(
    title: String,
    onAddClicked: suspend (String) -> Unit,
    InsertionField: @Composable RowScope.(textValue: MutableState<String>) -> Unit,
) {
    var nomeDoTreino = remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    Column {
        Text(title, style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            InsertionField(
                nomeDoTreino,
            )
            Button(modifier = Modifier
                .height(56.dp)
                .width(64.dp)
                .background(
                    color = MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(10)
                ), onClick = {
                scope.launch {
                    onAddClicked(nomeDoTreino.value)
                }
            }) {
                Icon(Icons.Default.Add, "Criar")
            }
        }

    }
}