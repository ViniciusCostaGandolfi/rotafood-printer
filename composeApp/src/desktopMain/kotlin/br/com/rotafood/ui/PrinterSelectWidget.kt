package br.com.rotafood.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PrinterSelectWidget(
    printers: List<String>,
    selectedPrinter: String?,
    onPrinterSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column {
        Text(text = "Selecionar Impressora", style = MaterialTheme.typography.subtitle1)

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            TextField(
                value = selectedPrinter ?: "",
                onValueChange = {},
                readOnly = true,
                label = { Text("Selecionar Impressora") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded)
                },
                modifier = Modifier
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                printers.forEach { printer ->
                    DropdownMenuItem(onClick = {
                        onPrinterSelected(printer)
                        expanded = false
                    }) {
                        Text(printer)
                    }
                }
            }
        }
    }
}
