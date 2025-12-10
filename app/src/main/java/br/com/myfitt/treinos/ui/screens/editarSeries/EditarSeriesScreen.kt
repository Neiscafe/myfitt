@file:OptIn(ExperimentalMaterial3Api::class)

package br.com.myfitt.treinos.ui.screens.editarSeries

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.myfitt.treinos.ui.theme.MyFittTheme

@Composable
fun EditarSeriesScreen(modifier: Modifier = Modifier) {

}
data class CamposStateWrapper(val campos: List<String> = emptyList())
@Composable
fun Tela(
    camposState: CamposStateWrapper,
    onPesoChange: (Int, String) -> Unit = { _, _ -> },
    onRepChange: (Int, String) -> Unit = { _, _ -> },
    salvar: () -> Unit = {},
    voltar: () -> Unit = {},
) {
    Scaffold(topBar = {
        TopAppBar(title = { Text("ASDMADOM") }, navigationIcon = {
            IconButton(voltar) {
                Icon(Icons.AutoMirrored.Default.ArrowBack, "Voltar")
            }
        })
    }) { ip ->
        Column(
            modifier = Modifier
                .padding(ip)
                .padding(32.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            repeat(3) { id ->
                Column() {
                    Text("Série $id")
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        TextField(
                            "TESTE $id",
                            modifier = Modifier.weight(1f),
                            onValueChange = { onPesoChange(id, it) },
                            label = { Text("Peso") })
                        Text("X")
                        TextField(
                            "TESTE $id",
                            onValueChange = { onRepChange(id, it) },
                            label = { Text("Reps") }, modifier = Modifier.weight(1f),
                        )
                    }
                }
            }
            Button(
                salvar,
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(0.dp, 16.dp),
                shape = MaterialTheme.shapes.small
            ) {
                Text("Salvar")
            }
        }
    }
}

@Preview
@Composable
private fun EditarSeriesPreview() {
    MyFittTheme {
        Tela()
    }
}