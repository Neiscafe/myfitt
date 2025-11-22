package br.com.myfitt.treinos.ui.screens

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
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
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import java.util.Collections

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
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    // Estados de controle do arraste
    var draggingItemIndex by remember { mutableStateOf<Int?>(null) }
    var delta by remember { mutableFloatStateOf(0f) }
    var isDragging by remember { mutableStateOf(false) }
    LazyColumn(state = listState,modifier = Modifier.padding(innerPadding)) {
        itemsIndexed(items = state.exercicios, key = { _, item -> item.exercicioTreinoId }) { index, it ->
            val isCurrentItemDragging = draggingItemIndex == index
            val elevation by animateDpAsState(if (isCurrentItemDragging) 8.dp else 0.dp, label = "elevation")
            val zIndex = if (isCurrentItemDragging) 1f else 0f
            OutlinedCard(
                modifier = Modifier
                    .fillMaxWidth().animateItem()
                    .clickable(true) { clicks(cardClick, it) }.graphicsLayer {
                        // Aplica o movimento vertical SOMENTE visualmente
                        translationY = if (isCurrentItemDragging) delta else 0f

                        scaleX = if (isCurrentItemDragging) 1.05f else 1f
                        scaleY = if (isCurrentItemDragging) 1.05f else 1f
                    },
                shape = RoundedCornerShape(0.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = elevation)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(modifier = Modifier.pointerInput(index) { // 'index' como key para recriar se a posição mudar
                        detectDragGesturesAfterLongPress(
                            onDragStart = {
                                draggingItemIndex = index
                                isDragging = true
                            },
                            onDragEnd = {
                                draggingItemIndex = null
                                delta = 0f
                                isDragging = false
                            },
                            onDragCancel = {
                                draggingItemIndex = null
                                delta = 0f
                                isDragging = false
                            },
                            onDrag = { change, dragAmount ->
                                change.consume()
                                delta += dragAmount.y

                                // Lógica de Swap (Reutilizada)
                                val currentDraggingIndex = draggingItemIndex ?: return@detectDragGesturesAfterLongPress
                                val currentItemInfo = listState.layoutInfo.visibleItemsInfo
                                    .find { it.index == currentDraggingIndex } ?: return@detectDragGesturesAfterLongPress

                                // Scroll Automático nas bordas
                                val viewportEnd = listState.layoutInfo.viewportEndOffset
                                if (change.position.y > viewportEnd - 150) {
                                    scope.launch { listState.scrollBy(10f) }
                                }
                                if (change.position.y < 150) {
                                    scope.launch { listState.scrollBy(-10f) }
                                }

                                // Matemática do Swap
                                if (delta > 0) { // Arrastando para baixo
                                    val nextItem = listState.layoutInfo.visibleItemsInfo
                                        .find { it.index == currentDraggingIndex + 1 }

                                    if (nextItem != null &&
                                        (currentItemInfo.offset + delta + currentItemInfo.size) > (nextItem.offset + nextItem.size / 2)) {
                                        Collections.swap(state.exercicios, currentDraggingIndex, currentDraggingIndex + 1)
                                        draggingItemIndex = currentDraggingIndex + 1
                                        delta -= nextItem.size + 8.dp.toPx()
                                    }
                                } else if (delta < 0) { // Arrastando para cima
                                    val prevItem = listState.layoutInfo.visibleItemsInfo
                                        .find { it.index == currentDraggingIndex - 1 }

                                    if (prevItem != null &&
                                        (currentItemInfo.offset + delta) < (prevItem.offset + prevItem.size / 2)) {
                                        Collections.swap(state.exercicios, currentDraggingIndex, currentDraggingIndex - 1)
                                        draggingItemIndex = currentDraggingIndex - 1
                                        delta += prevItem.size + 8.dp.toPx()
                                    }
                                }
                            }
                        )
                    }, onClick = { clicks(arrastarClick, it) }) {
                        Icon(
                            painter = painterResource(R.drawable.baseline_drag_handle_24),
                            contentDescription = "Arrastar exercício"
                        )
                    }
                    Text(it.exercicioTreinoId.toString(), Modifier.weight(1f))
                    IconButton({ clicks(removerClick, it) }) {
                        Icon(
                            Icons.Filled.Delete, contentDescription = "Remover exercício"
                        )
                    }
                    IconButton({ clicks(substituirClick, it) }) {
                        Icon(
                            painter = painterResource(R.drawable.swap_horiz_24dp_e3e3e3_fill0_wght400_grad0_opsz24),
                            contentDescription = "Substituir exercício",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
            }

        }
    }
}

//@Composable
//private fun ExercicioTreinoItem(it: ExercicioTreino, clicks: (Int) -> Unit) {
//}

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
//    ExercicioTreinoItem(ExercicioTreino(1, 1, 1)) {
//
//    }
}

