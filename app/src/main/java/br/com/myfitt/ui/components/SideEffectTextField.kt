package br.com.myfitt.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val INTERVALO_DEBOUNCE = 1000L

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SideEffectTextField(
    initialValue: String,
    onTextChanged: (String) -> Unit = {},
    onUpdate: suspend (String) -> Unit = {}
) {
    var textValue by remember { mutableStateOf(TextFieldValue(initialValue)) }
    var wasFocused by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    DisposableEffect(textValue) {
        if (textValue.text == initialValue || textValue.text.isEmpty()) return@DisposableEffect onDispose {}
        val job = scope.launch {
            delay(INTERVALO_DEBOUNCE)
            if (textValue.text.isDigitsOnly()) {
                onUpdate(textValue.text)
            }
        }
        return@DisposableEffect onDispose {
            job.cancel()
        }
    }
    Column(horizontalAlignment = Alignment.End) {
        BasicTextField(
            value = textValue,
            onValueChange = {
                textValue = it
                onTextChanged(it.text)
            },
            keyboardOptions = KeyboardOptions(
                autoCorrectEnabled = false,
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),

            singleLine = true,
            textStyle = MaterialTheme.typography.bodyMedium.copy(
                textAlign = TextAlign.End,
                color = Color.White
            ),
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

@Preview
@Composable
private fun SideEffectTextViewPreview() {
    SideEffectTextField("")
}