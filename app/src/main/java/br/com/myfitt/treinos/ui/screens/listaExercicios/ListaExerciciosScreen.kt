@file:OptIn(ExperimentalMaterial3Api::class)

package br.com.myfitt.treinos.ui.screens.listaExercicios

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import br.com.myfitt.common.domain.Exercicio
import br.com.myfitt.treinos.ui.theme.MyFittTheme
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

object ListaExerciciosNavigation {
    const val route = "listaExercicios"
    fun composeNavigation(builder: NavGraphBuilder, navController: NavController) {
        builder.composable(
            route = route
        ) {
            ListaExerciciosScreen(navController::popBackStack)
        }
    }
}

@Composable
fun ListaExerciciosScreen(
    popBackstack: () -> Boolean, viewModel: ListaExerciciosViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val onItemCallback = remember { viewModel.getCallback() }
    Tela(
        state,
        onItemCallback,
        viewModel::resetaEventos,
        viewModel::pesquisaMudou,
        popBackstack = popBackstack
    )
}

@Composable
private fun Tela(
    state: ListaExerciciosState,
    onItemClick: (Exercicio) -> Unit,
    limpaEventos: () -> Unit,
    onTextChanged: (String) -> Unit,
    popBackstack: () -> Boolean
) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) { innerPadding ->
        if (state.carregando) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        state.erro?.let {
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = it
                )
                limpaEventos()
            }
        }
        SearchView(
            Modifier
                .fillMaxSize()
                .padding(innerPadding), state.exerciciosExibidos, { it, index ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onItemClick(it)
                        }) {
                    Text(
                        text = it.toString(),
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(24.dp)
                    )
                    if (index != state.exerciciosExibidos.size - 1) {
                        HorizontalDivider()
                    }
                }
            }, onTextChanged, popBackstack
        )

    }
}

@Composable
fun <T> SearchView(
    modifier: Modifier,
    items: List<T>,
    itemView: @Composable LazyItemScope.(T, Int) -> Unit,
    onTextChanged: (String) -> Unit,
    onBackClick: () -> Boolean,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        SearchBox(onBackClick = { onBackClick() }, onSearchTextChanged = onTextChanged)
        HorizontalDivider(color = Color.DarkGray)
        SearchListView(
            items = items, itemView = itemView
        )
    }
}

@Composable
fun <T> SearchListView(
    items: List<T>, itemView: @Composable LazyItemScope.(T, Int) -> Unit
) {
    LazyColumn {
        itemsIndexed(items) { index, item ->
            itemView(this, item, index)
        }
    }
}

@Composable
fun SearchBox(
    onBackClick: () -> Unit,
    onSearchTextChanged: (String) -> Unit,
) {
    var searchText: String by remember { mutableStateOf("") }

    Row(
        modifier = Modifier.padding(horizontal = 8.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        // Back icon
        Icon(
            modifier = Modifier.clickable { onBackClick() },
            imageVector = Icons.AutoMirrored.Default.ArrowBack,
            contentDescription = "",
            tint = MaterialTheme.colorScheme.onSurface
        )
        OutlinedTextField(
            modifier = Modifier.weight(1f),
            placeholder = {
                Text(
                    "Pesquisar exercícios",
                    style = MaterialTheme.typography.bodyLarge
                )
            },
            value = searchText,
            singleLine = true,
            onValueChange = { value ->
                searchText = value
                onSearchTextChanged(value)
            },
            colors = TextFieldDefaults.colors().copy(
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            )
        )

        // This is optional animated visibility change
        AnimatedVisibility(visible = searchText.isNotBlank()) {
            // Clear text icon
            Icon(
                modifier = Modifier.clickable {
                    searchText = ""
                    onSearchTextChanged("")
                },
                imageVector = Icons.Default.Clear,
                contentDescription = "",
                tint = MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}


@Preview
@Composable
private fun ListaExerciciosScreenPreview() {
    MyFittTheme {
        Tela(
            ListaExerciciosState(false, exerciciosExibidos = listOf(Exercicio(1, "adasdasd"))),
            {},
            {},
            {},
            { true })

    }
}