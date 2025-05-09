package br.com.rotafood.pages

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import br.com.rotafood.ui.*
import kotlinx.coroutines.launch
import model.MerchantUserDto
import utils.TokenStore
import utils.WsPrinter
import javax.print.PrintServiceLookup

@Composable
fun PrinterManagerPage(
    user: MerchantUserDto,
    token: String,
    onLogout: () -> Unit,
) {
    var connected       by remember { mutableStateOf(false) }
    var selectedPrinter by remember { mutableStateOf<String?>(null) }
    var printerList     by remember { mutableStateOf(listOf<String>()) }
    var pageWidth       by remember { mutableStateOf("58") }
    var pageHeight      by remember { mutableStateOf("80") }
    var fontSize        by remember { mutableStateOf("10") }
    var pageMargin      by remember { mutableStateOf("1") }

    LaunchedEffect(Unit) {
        printerList = PrintServiceLookup.lookupPrintServices(null, null)
            .map { it.name }
            .sorted()
        selectedPrinter = printerList.firstOrNull()
    }

    val scope = rememberCoroutineScope()
    var wsPrinter by remember { mutableStateOf<WsPrinter?>(null) }

    fun startWs() {
        val printerName = selectedPrinter ?: return
        PrintServiceLookup.lookupPrintServices(null, null)
            .firstOrNull { it.name == printerName } ?: return

        wsPrinter?.stop()
        wsPrinter = WsPrinter(
            token        = token,
            printerName  = selectedPrinter,
            widthMm      = pageWidth,
            fontSizePt   = fontSize,
            marginMm     = pageMargin
        ).also { printer ->
            scope.launch {
                printer.start()
                connected = true
            }
        }
    }


    fun stopWs() {
        wsPrinter?.stop()
        wsPrinter = null
        connected = false
    }

    LaunchedEffect(selectedPrinter, pageWidth) { startWs() }

    DisposableEffect(Unit) { onDispose { stopWs() } }


    Column(
        modifier = Modifier.padding(24.dp).fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Person, null); Spacer(Modifier.width(8.dp))
            Text("Olá, ${user.name}", Modifier.weight(1f))
            IconButton({
                TokenStore.token = null
                onLogout()
            }) { Icon(Icons.Default.Delete, "Sair") }
        }

        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            val color = if (connected) Color(0xFF2E7D32) else MaterialTheme.colors.error
            val text  = if (connected) "Conectado ✓" else "Desconectado"
            Text(text, Modifier.weight(1f), color = color)
            if (!connected) Button(onClick = ::startWs) { Text("Reconectar") }
        }

        Divider()

        Text("Configurações da Impressão", style = MaterialTheme.typography.h6)

        PrinterSelectWidget(printerList, selectedPrinter)   { selectedPrinter = it }
        PageWidthWidget(pageWidth)                           { pageWidth      = it }
        FontSizeWidget(fontSize)                             { fontSize       = it }
        PageMarginWidget(pageMargin)                         { pageMargin     = it }
        Spacer(Modifier.height(24.dp))

        PrinterTestButton(
            selectedPrinter,
            pageWidth, fontSize,
            pageMargin
        )
    }
}
