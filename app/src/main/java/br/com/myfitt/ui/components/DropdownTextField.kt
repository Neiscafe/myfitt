package br.com.myfitt.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> DropdownTextField(
    items: List<T?>,
    getValue: (T?) -> String,
    onSelectedChanged: (T?) -> Unit,
    hint: String,
    selected: T? = items.firstOrNull(),
    acceptsNull: Boolean = true,
    enabled: Boolean = true,
    modifier: Modifier = Modifier
        .width(IntrinsicSize.Min)
        .height(IntrinsicSize.Min)
) {
    val _expanded = remember { mutableStateOf(false) }
    val _selected = remember { mutableStateOf(selected) }
    Column(
        modifier = modifier
    ) {
        ExposedDropdownMenuBox(expanded = _expanded.value && enabled,
            onExpandedChange = { _expanded.value = !_expanded.value }) {
            OutlinedTextField(
                readOnly = true,
                enabled = enabled,
                label = { Text(hint, Modifier.background(Color.Transparent)) },
                value = getValue(_selected.value ?: items.firstOrNull()),
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(MenuAnchorType.PrimaryNotEditable),
                onValueChange = { },
                textStyle = TextStyle(Color.White),
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = _expanded.value && enabled) },
                colors = ExposedDropdownMenuDefaults.textFieldColors(),
                singleLine = true
            )
            ExposedDropdownMenu(expanded = _expanded.value && enabled,
                onDismissRequest = { _expanded.value = false }) {
                if (items.isEmpty()) {
                    DropdownMenuItem(onClick = {
                        _expanded.value = false
                        _selected.value = null
                        onSelectedChanged(null)
                    }, text = { Text(getValue(null)) })
                }
                items.forEach {
                    DropdownMenuItem(onClick = {
                        _expanded.value = false
                        _selected.value = it
                        onSelectedChanged(it)
                    }, text = { Text(getValue(it)) })
                }
            }
        }

    }
}
