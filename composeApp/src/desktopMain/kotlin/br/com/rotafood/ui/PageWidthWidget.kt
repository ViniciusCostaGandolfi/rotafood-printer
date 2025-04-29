package br.com.rotafood.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType


@Composable
fun PageWidthWidget(
    pageWidth: String,
    onPageWidthChange: (String) -> Unit
) {
    OutlinedTextField(
        value = pageWidth,
        onValueChange = { onPageWidthChange(it) },
        label = { Text("Largura da Impressora (mm)") },
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
        modifier = Modifier.fillMaxWidth()
    )
}