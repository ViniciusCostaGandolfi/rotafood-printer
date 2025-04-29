package br.com.rotafood.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun PageHeightWidget(
    pageHeight: String,
    onPageHeightChange: (String) -> Unit
) {
    OutlinedTextField(
        value = pageHeight,
        onValueChange = { onPageHeightChange(it) },
        label = { Text("Altura da Página (mm)") },
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
        modifier = Modifier.fillMaxWidth()
    )
}
