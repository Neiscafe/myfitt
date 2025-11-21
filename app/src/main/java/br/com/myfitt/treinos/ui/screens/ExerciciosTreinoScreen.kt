package br.com.myfitt.treinos.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciciosTreinoScreen(
    treinoId: Int,
    voltar: () -> Boolean,
    modifier: Modifier = Modifier,
    viewModel: ExerciciosTreinoViewModel = koinViewModel(parameters = {
        parametersOf(treinoId)
    })
) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    Tela(state.value, voltar)
}

@Composable
private fun Tela(
    state: ExerciciosTreinoState,
    voltar: () -> Boolean
) {
    Scaffold(topBar = { TopAppBar(state, voltar = voltar) }) { innerPadding ->
        Box(Modifier.padding(innerPadding))
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun TopAppBar(state: ExerciciosTreinoState, voltar: () -> Boolean) {
    MediumTopAppBar(
        title = {
            Text(
                "Duração: ${state.mensagemDuracao}", maxLines = 2, overflow = TextOverflow.Ellipsis
            )
        },
        navigationIcon = {
            IconButton(onClick = { voltar() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    "Voltar"
                )
            }
        })
}

@Preview
@Composable
private fun ExerciciosTreinoScreenPreview() {
    Tela(
        state = ExerciciosTreinoState(
            mensagemDuracao = "20min"
        ), voltar = { true })
}

