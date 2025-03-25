package br.com.myfitt.ui.components

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val INTERVALO_DEBOUNCE = 1000L

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExercicioAntesAgoraColumn(
    title: String, valorAnterior: Int, valorTreinoAtual: Int, onUpdate: suspend (String) -> Unit
) {
    val context = LocalContext.current
    var textValue by remember { mutableStateOf(TextFieldValue(valorTreinoAtual.toString())) }
    var wasFocused by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    DisposableEffect(textValue) {
        if (textValue.text == valorTreinoAtual.toString() || textValue.text.isEmpty()) return@DisposableEffect onDispose {}
        val job = scope.launch {
            delay(INTERVALO_DEBOUNCE)
            if (textValue.text.isDigitsOnly()) {
                onUpdate(textValue.text)
                Log.d("TEST", "updated ${textValue.text} for $title")
            }
        }
        return@DisposableEffect onDispose {
            job.cancel()
        }
    }
    Column(horizontalAlignment = Alignment.End) {
        Text(text = title, style = MaterialTheme.typography.bodyMedium)
        Spacer(Modifier.size(8.dp))
        Text("$valorAnterior", style = MaterialTheme.typography.bodyMedium)
        Spacer(Modifier.size(8.dp))
        BasicTextField(value = textValue,
            onValueChange = {
                textValue = it
            },
            keyboardOptions = KeyboardOptions(
                autoCorrectEnabled = false,
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),

            singleLine = true,
            textStyle = MaterialTheme.typography.bodyMedium.copy(textAlign = TextAlign.End),
            modifier = Modifier
                .onFocusChanged {
                    if (!wasFocused && it.isFocused) {
                        textValue = textValue.copy(selection = TextRange(0, textValue.text.length))
                    }
                    wasFocused = it.isFocused
                }
                .width(40.dp))
    }
}