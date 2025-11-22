package br.com.myfitt.treinos.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.myfitt.R
import br.com.myfitt.common.domain.ExercicioTreino
import br.com.myfitt.treinos.ui.screens.ExerciciosTreinoViewModel.Companion.arrastarClick
import br.com.myfitt.treinos.ui.screens.ExerciciosTreinoViewModel.Companion.cardClick
import br.com.myfitt.treinos.ui.screens.ExerciciosTreinoViewModel.Companion.removerClick
import br.com.myfitt.treinos.ui.screens.ExerciciosTreinoViewModel.Companion.substituirClick
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
    Tela(state.value, voltar, viewModel::clicks)
}

@Composable
private fun Tela(
    state: ExerciciosTreinoState, voltar: () -> Boolean, clicks: (Int, ExercicioTreino)->Unit
) {
    Scaffold(topBar = { TopAppBar(state, voltar = voltar) }) { innerPadding ->
        ListaExercicios(innerPadding, state, clicks)
    }
}

@Composable
private fun ListaExercicios(
    innerPadding: PaddingValues,
    state: ExerciciosTreinoState,
    clicks: (Int, ExercicioTreino) -> Unit
) {
    LazyColumn(modifier = Modifier.padding(innerPadding)) {
        items(items = state.exercicios, key = { it.exercicioTreinoId }) {
            ExercicioTreinoItem(it) { i -> clicks(i, it) }
        }
    }
}

@Composable
private fun ExercicioTreinoItem(it: ExercicioTreino, clicks: (Int) -> Unit) {
    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(true) { clicks(cardClick) },
        shape = RoundedCornerShape(0.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton({ clicks(arrastarClick) }) {
                Icon(
                    painter = painterResource(R.drawable.baseline_drag_handle_24),
                    contentDescription = "Arrastar exercício"
                )
            }
            Text(it.exercicioTreinoId.toString(), Modifier.weight(1f))
            IconButton({ clicks(removerClick) }) {
                Icon(
                    Icons.Filled.Delete, contentDescription = "Remover exercício"
                )
            }
            IconButton({ clicks(substituirClick) }) {
                Icon(
                    painter = painterResource(R.drawable.swap_horiz_24dp_e3e3e3_fill0_wght400_grad0_opsz24),
                    contentDescription = "Substituir exercício",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun TopAppBar(state: ExerciciosTreinoState, voltar: () -> Boolean) {
    MediumTopAppBar(title = {
        Text(
            "Duração: ${state.mensagemDuracao}", maxLines = 2, overflow = TextOverflow.Ellipsis
        )
    }, navigationIcon = {
        IconButton(onClick = { voltar() }) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack, "Voltar"
            )
        }
    })
}

@Preview
@Composable
private fun ExerciciosTreinoScreenPreview() {
    Tela(
        state = ExerciciosTreinoState(
            mensagemDuracao = "20min", listOf(ExercicioTreino(1, 1, 1))
        ), voltar = { true }, clicks = {_,_->})
}

@Preview
@Composable
private fun ExercicioItemPreview() {
    ExercicioTreinoItem(ExercicioTreino(1, 1, 1)) {

    }
}

