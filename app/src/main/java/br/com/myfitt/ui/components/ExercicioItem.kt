package br.com.myfitt.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.com.myfitt.domain.models.ExercicioMudou
import br.com.myfitt.domain.models.TreinoExercicioComNome

@Composable
fun ExercicioItem(
    exercicio: TreinoExercicioComNome,
    onDelete: () -> Unit = {},
    onMoveUp: () -> Unit = {},
    onMoveDown: () -> Unit = {},
    onUpdate: (TreinoExercicioComNome, ExercicioMudou) -> Unit = { it, mudou -> },
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            Modifier
                .fillMaxHeight()
                .fillMaxWidth(0.5f)
        ) {
            Text(
                exercicio.exercicioNome, style = MaterialTheme.typography.titleMedium
            )
            Row(
                modifier = Modifier.fillMaxHeight(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val iconModifier = Modifier.size(20.dp)
                IconButton(onClick = {
                    onMoveDown()
                }) {
                    Icon(
                        modifier = iconModifier,
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = "Elevar"
                    )
                }
                IconButton(onClick = { onMoveUp() }) {
                    Icon(
                        modifier = iconModifier,
                        imageVector = Icons.Default.KeyboardArrowUp,
                        contentDescription = "Abaixar"
                    )
                }
                IconButton(onClick = { onDelete() }) {
                    Icon(
                        modifier = iconModifier,
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Remover"
                    )
                }
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(
                16.dp, Alignment.End
            )
        ) {
            ExercicioAntesAgoraColumn(title = "Séries",
                valorAnterior = exercicio.seriesUltimoTreino,
                valorTreinoAtual = exercicio.series,
                onUpdate = {
                    runCatching {
                        onUpdate(
                            exercicio.copy(series = it.toInt()), ExercicioMudou.SERIES
                        )
                    }
                })
            ExercicioAntesAgoraColumn(
                "Reps",
                exercicio.repeticoesUltimoTreino,
                exercicio.repeticoes,
                onUpdate = {
                    runCatching {
                        onUpdate(
                            exercicio.copy(repeticoes = it.toInt()), ExercicioMudou.REPS
                        )
                    }
                })
            ExercicioAntesAgoraColumn(
                "Peso",
                exercicio.pesoKgUltimoTreino.toInt(),
                exercicio.pesoKg.toInt(),
                onUpdate = {
                    runCatching {
                        onUpdate(
                            exercicio.copy(pesoKg = it.toFloat()), ExercicioMudou.PESO
                        )
                    }
                })
        }
    }
}
