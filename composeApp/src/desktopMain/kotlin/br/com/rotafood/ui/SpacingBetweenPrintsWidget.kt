package br.com.rotafood.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.Modifier

@Composable
fun SpacingBetweenPrintsWidget(
    spacing: String,
    onSpacingChange: (String) -> Unit
) {
    OutlinedTextField(
        value = spacing,
        onValueChange = { onSpacingChange(it) },
        label = { Text("Espaçamento entre as linhas (px ou mm)") },
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
        modifier = Modifier.fillMaxWidth()
    )
}