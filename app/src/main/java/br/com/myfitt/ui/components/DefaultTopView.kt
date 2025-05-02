package br.com.myfitt.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp

@Composable
fun DefaultTopView(
    title: String,
    onComplete: (String) -> Unit,
    topComponents: (RowScope.() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val topBarText = remember { mutableStateOf("") }
    Column {
        Text(text = title, style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(16.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            topComponents?.let {
                topComponents()
            } ?: run {
                OutlinedTextField(value = topBarText.value,
                    placeholder = {
                        Text("Crie sua planilha...")
                    },
                    onValueChange = { topBarText.value = it },
                    modifier = Modifier.weight(1f),
                    textStyle = TextStyle(Color.Black),
                    singleLine = true
                )

                Button(modifier = Modifier
                    .fillMaxHeight()
                    .background(
                        color = MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(10)
                    ), onClick = {
                    if (topBarText.value.isNotEmpty()) {
                        onComplete(topBarText.value)
                        topBarText.value = ""
                    }
                }) {
                    Icon(Icons.Default.Add, "Criar")
                }
            }
        }
    }
}