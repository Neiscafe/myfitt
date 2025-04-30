package br.com.myfitt.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun <T> DefaultCardList(
    items: List<T>,
    onClick: (T) -> Unit,
    getName: (T) -> String,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = Modifier.fillMaxHeight(),
        contentPadding = PaddingValues(0.dp, 8.dp, 0.dp, 0.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.Top),
    ) {
        items(items.size) { i ->
            val item = items[i]
            Card(onClick = {
                onClick(item)
            }) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        getName(item),
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(10.dp)
                    )
                    IconButton(onClick = {
//                            planilhaViewModel.deletePlanilha(planilha)
                    }) {
                        Icon(
                            imageVector = Icons.Default.Delete, contentDescription = "Remover"
                        )
                    }
                }

            }
        }
    }
}