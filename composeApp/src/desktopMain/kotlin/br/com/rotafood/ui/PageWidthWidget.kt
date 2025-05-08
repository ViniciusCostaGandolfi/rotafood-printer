package br.com.rotafood.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue        // <— adicionado
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue        // <— adicionado
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PageWidthWidget(
    pageWidth: String,
    onPageWidthChange: (String) -> Unit
) {
    val options = listOf("58", "80")
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = pageWidth,
            onValueChange = {  },
            label = { Text("Largura da Impressora (mm)") },
            readOnly = true,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded)
            },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    onClick = {
                        onPageWidthChange(option)
                        expanded = false
                    }
                ) {
                    Text(text = option)
                }
            }
        }
    }
}
