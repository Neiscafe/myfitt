package br.com.myfitt.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import br.com.myfitt.domain.models.Divisao
import br.com.myfitt.ui.viewModels.ListaDivisaoViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun ListaDivisoesScreen(
    navigate: (Int) -> Unit,
    viewModel: ListaDivisaoViewModel = koinViewModel(),
    modifier: Modifier = Modifier,
) {
    val divisoes by viewModel.divisoes.collectAsState(initial = emptyList())
    var nomeDivisao by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    Column(modifier = modifier.padding(10.dp, 30.dp, 10.dp, 0.dp)) {
        Text(text = "Suas divisões", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(16.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(value = nomeDivisao,
                placeholder = {
                    Text("Crie sua divisão...")
                },
                onValueChange = { nomeDivisao = it },
                modifier = Modifier.weight(1f),
                textStyle = TextStyle(Color.Black),
                singleLine = true
            )

            Button(modifier = Modifier
                .fillMaxHeight()
                .background(
                    color = MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(10)
                ), onClick = {
                coroutineScope.launch {
                    if (nomeDivisao.isNotEmpty()) {
                        navigate(viewModel.insert(Divisao(nome = nomeDivisao)))
                        nomeDivisao = "" // Limpa o campo}
                    }
                }
            }) {
                Icon(Icons.Default.Add, "Criar")
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxHeight(),
            contentPadding = PaddingValues(0.dp, 8.dp, 0.dp, 0.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.Top),
        ) {
            items(divisoes.size) { i ->
                val divisao = divisoes[i]
                Card(onClick = {
                    navigate(divisao.id)
                }) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            divisao.nome,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(10.dp)
                        )
                        IconButton(onClick = {
                            viewModel.delete(divisao)
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

}