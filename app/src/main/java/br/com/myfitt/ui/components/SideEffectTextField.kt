package br.com.myfitt.ui.components

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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

private const val INTERVALO_DEBOUNCE = 1000L

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SideEffectTextField(
    initialValue: String,
    modifier: Modifier = Modifier,
    onTextChanged: (String) -> Unit = {},
    onUpdate: (String) -> Unit = {}
) {
    var textValue by remember { mutableStateOf(TextFieldValue(initialValue)) }
    var wasFocused by remember { mutableStateOf(false) }
    var triggerSelectAll by remember { mutableStateOf(false) }
    LaunchedEffect(triggerSelectAll) {
        if (triggerSelectAll) {
            textValue = textValue.copy(selection = TextRange(0, textValue.text.length))
            triggerSelectAll = false
        }
    }
    BasicTextField(
        value = textValue, onValueChange = {
            textValue = it
            if (it.text.isEmpty() || it.text == initialValue) return@BasicTextField
            onUpdate(it.text)
            onTextChanged(it.text)
        }, keyboardOptions = KeyboardOptions(
            autoCorrectEnabled = false,
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        ),

        singleLine = true, textStyle = MaterialTheme.typography.bodyMedium.copy(
            textAlign = TextAlign.End, color = Color.White
        ), modifier = modifier.onFocusChanged {
            if (!wasFocused && it.isFocused) {
                triggerSelectAll = true
            }
            wasFocused = it.isFocused
        })
}

@Preview
@Composable
private fun SideEffectTextViewPreview() {
    SideEffectTextField(
        "adasddas", modifier = Modifier
            .height(40.dp)
            .wrapContentHeight()
    )
}