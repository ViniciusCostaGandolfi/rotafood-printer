package br.com.rotafood.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.Modifier

@Composable
fun FontSizeWidget(
    fontSize: String,
    onFontSizeChange: (String) -> Unit
) {
    OutlinedTextField(
        value = fontSize,
        onValueChange = { onFontSizeChange(it) },
        label = { Text("Tamanho da Fonte (pt)") },
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
        modifier = Modifier.fillMaxWidth()
    )
}