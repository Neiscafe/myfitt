package br.com.myfitt.treinos.ui.screens

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
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
import androidx.compose.ui.graphics.GraphicsLayerScope
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.PointerInputEventHandler
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
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
    Tela(state.value, voltar, viewModel::clicks, viewModel::limpaEvents)
}

@Composable
private fun Tela(
    state: ExerciciosTreinoState,
    voltar: () -> Boolean,
    clicks: (Int, ExercicioTreino) -> Unit,
    limpaEventos: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = { TopAppBar(state, voltar = voltar) }) { innerPadding ->
        state.erro?.let {
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = it
                )
                limpaEventos()
            }
        }
        if (state.carregando) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        ListaExercicios(innerPadding, state, clicks)
    }
}

@Composable
private fun ListaExercicios(
    innerPadding: PaddingValues,
    state: ExerciciosTreinoState,
    clicks: (Int, ExercicioTreino) -> Unit,
) {
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    var draggingItemIndex by remember { mutableStateOf<Int?>(null) }
    var delta by remember { mutableFloatStateOf(0f) }
    var isDragging by remember { mutableStateOf(false) }
    LazyColumn(state = listState, modifier = Modifier.padding(innerPadding)) {
        itemsIndexed(
            items = state.exercicios, key = { _, item -> item.exercicioTreinoId }) { index, it ->
            val isCurrentItemDragging = draggingItemIndex == index
            val elevation by animateDpAsState(
                if (isCurrentItemDragging) 8.dp else 0.dp, label = "elevation"
            )
            val dragCallback = PointerInputEventHandler {
                detectDragGesturesAfterLongPress(onDragStart = {
                    draggingItemIndex = index
                    isDragging = true
                }, onDragEnd = {
                    if(draggingItemIndex!=null) {
                        clicks(arrastarClick, it.copy(ordem = (draggingItemIndex?:0)+1))
                    }
                    draggingItemIndex = null
                    delta = 0f
                    isDragging = false
                }, onDragCancel = {
                    draggingItemIndex = null
                    delta = 0f
                    isDragging = false
                }, onDrag = { change, dragAmount ->
                    change.consume()
                    delta += dragAmount.y

                    val currentDraggingIndex =
                        draggingItemIndex ?: return@detectDragGesturesAfterLongPress
                    val currentItemInfo =
                        listState.layoutInfo.visibleItemsInfo.find { it.index == currentDraggingIndex }
                            ?: return@detectDragGesturesAfterLongPress

                    val viewportEnd = listState.layoutInfo.viewportEndOffset
                    if (change.position.y > viewportEnd - 150) {
                        scope.launch { listState.scrollBy(10f) }
                    }
                    if (change.position.y < 150) {
                        scope.launch { listState.scrollBy(-10f) }
                    }

                    // Matemática do Swap
                    if (delta > 0) { // Arrastando para baixo
                        val nextItem =
                            listState.layoutInfo.visibleItemsInfo.find { it.index == currentDraggingIndex + 1 }

                        if (nextItem != null && (currentItemInfo.offset + delta + currentItemInfo.size) > (nextItem.offset + nextItem.size / 2)) {
                            Collections.swap(
                                state.exercicios, currentDraggingIndex, currentDraggingIndex + 1
                            )
                            draggingItemIndex = currentDraggingIndex + 1
                            delta -= nextItem.size + 8.dp.toPx()
                        }
                    } else if (delta < 0) { // Arrastando para cima
                        val prevItem =
                            listState.layoutInfo.visibleItemsInfo.find { it.index == currentDraggingIndex - 1 }

                        if (prevItem != null && (currentItemInfo.offset + delta) < (prevItem.offset + prevItem.size / 2)) {
                            Collections.swap(
                                state.exercicios, currentDraggingIndex, currentDraggingIndex - 1
                            )
                            draggingItemIndex = currentDraggingIndex - 1
                            delta += prevItem.size + 8.dp.toPx()
                        }
                    }
                })
            }
            val graphicsLayer: GraphicsLayerScope.() -> Unit = {
                translationY = if (isCurrentItemDragging) delta else 0f
                scaleX = if (isCurrentItemDragging) 1.05f else 1f
                scaleY = if (isCurrentItemDragging) 1.05f else 1f
            }

            ExercicioTreinoItem(
                it,
                index,
                clicks,
                elevation = elevation,
                dragCallback = dragCallback,
                graphicsLayer = graphicsLayer
            )
        }
    }
}

@Composable
private fun ExercicioTreinoItem(
    it: ExercicioTreino,
    index: Int,
    clicks: (Int, ExercicioTreino) -> Unit,
    elevation: Dp,
    dragCallback: PointerInputEventHandler,
    graphicsLayer: GraphicsLayerScope.() -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedCard(
        modifier = modifier
            .fillMaxWidth()
            .clickable(true) { clicks(cardClick, it) }
            .graphicsLayer(graphicsLayer),
        shape = RoundedCornerShape(0.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = elevation)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(
                modifier = Modifier.pointerInput(index, dragCallback),
                onClick = { clicks(arrastarClick, it) }) {
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
            mensagemDuracao = "20min",
            exercicios = listOf(ExercicioTreino(1, 1, 1)),
            carregando = true,
            erro = "TESTE ERRO"
        ), voltar = { true }, clicks = { _, _ -> }, {})
}

@Preview
@Composable
private fun ExercicioItemPreview() {
    ExercicioTreinoItem(
        it = ExercicioTreino(1, 1, 1),
        index = 0,
        clicks = { _, _ -> },
        elevation = 1.dp,
        dragCallback = {},
        graphicsLayer = {})
}

