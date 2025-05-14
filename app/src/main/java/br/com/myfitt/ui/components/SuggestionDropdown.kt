package br.com.myfitt.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults.TrailingIcon
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.ImeAction
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val INTERVALO_DEBOUNCE = 1000L

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> SuggestionDropdown(
    textState: MutableState<String>,
    getSuggestions: suspend (String) -> List<T>,
    onSuggestionClicked: (T) -> Unit,
    getText: (T) -> String,
    trailingIcon: ImageVector? = null,
    onIconClick: (T) -> Unit = {},
    modifier: Modifier = Modifier
) {
    var suggestions = remember { mutableStateOf(listOf<T>()) }
    var dropDownExpanded by remember { mutableStateOf(suggestions.value.isNotEmpty()) }
    val exercicioDigitado = textState
    val scope = rememberCoroutineScope()
    fun performActionAndResetField(clickedItem: T) {
        onSuggestionClicked(clickedItem)
        exercicioDigitado.value = ""
    }
    DisposableEffect(exercicioDigitado.value) {
        val getSuggestionsJob = scope.launch {
            delay(INTERVALO_DEBOUNCE)
            suggestions.value = getSuggestions(exercicioDigitado.value)
        }
        onDispose { getSuggestionsJob.cancel() }
    }
    ExposedDropdownMenuBox(modifier = modifier,
        expanded = dropDownExpanded,
        onExpandedChange = { dropDownExpanded = !dropDownExpanded }) {
        OutlinedTextField(
            value = exercicioDigitado.value,
            keyboardOptions = KeyboardOptions(
                autoCorrectEnabled = false, imeAction = ImeAction.Search
            ),
            onValueChange = {
                exercicioDigitado.value = it
            },
            modifier = Modifier
                .menuAnchor(MenuAnchorType.PrimaryEditable)
                .fillMaxWidth(),
            trailingIcon = { TrailingIcon(expanded = dropDownExpanded) },
            singleLine = true
        )

        ExposedDropdownMenu(dropDownExpanded, onDismissRequest = { dropDownExpanded = false }) {
            suggestions.value.forEach {
                DropdownMenuItem(onClick = {
                    performActionAndResetField(it)
                }, trailingIcon = {
                    trailingIcon?.let { icon ->
                        Icon(
                            icon,
                            "",
                            Modifier.clickable {
                                onIconClick(it)
                                scope.launch {
                                    suggestions.value = getSuggestions(exercicioDigitado.value)
                                }
                            })
                    }
                }, text = {
                    Text(text = getText(it))
                })
            }
        }
    }


}