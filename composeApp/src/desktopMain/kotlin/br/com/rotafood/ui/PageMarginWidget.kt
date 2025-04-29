package br.com.rotafood.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.Modifier

@Composable
fun PageMarginWidget(
    pageMargin: String,
    onPageMarginChange: (String) -> Unit
) {
    OutlinedTextField(
        value = pageMargin,
        onValueChange = { onPageMarginChange(it) },
        label = { Text("Margem da Página (mm)") },
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
        modifier = Modifier.fillMaxWidth()
    )
}