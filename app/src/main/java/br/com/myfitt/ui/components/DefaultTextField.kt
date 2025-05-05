package br.com.myfitt.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle

@Composable
fun DefaultTextField(
    textValue: MutableState<String>,
    hint: String,
    suffixText: String? = null,
    icon: ImageVector? = null,
    onIconClick: (String) -> Unit = {},
    modifier: Modifier = Modifier,
) {
    OutlinedTextField(
        value = textValue.value,
        onValueChange = {
            textValue.value = it
        },
        suffix = suffixText?.let {
            { Text(it) }
        },
        placeholder = {
            Text(hint)
        },
        trailingIcon = icon?.let {
            { Icon(it, "", Modifier.clickable { onIconClick(textValue.value) }) }
        },
        modifier = modifier,
        textStyle = TextStyle(Color.LightGray),
        singleLine = true,
    )
}