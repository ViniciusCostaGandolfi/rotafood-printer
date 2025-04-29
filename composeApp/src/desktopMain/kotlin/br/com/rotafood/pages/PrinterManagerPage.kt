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
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.websocket.*
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.websocket.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import model.MerchantUserDto
import utils.TokenStore
import utils.printText
import javax.print.PrintServiceLookup


@Composable
fun PrinterManagerPage(
    user: MerchantUserDto,
    token: String,
    onLogout: () -> Unit,
) {
    var connected        by remember { mutableStateOf(false) }
    var selectedPrinter  by remember { mutableStateOf<String?>(null) }
    var printerList      by remember { mutableStateOf(listOf<String>()) }
    var pageWidth        by remember { mutableStateOf("58") }
    var pageHeight       by remember { mutableStateOf("80") }
    var fontSize         by remember { mutableStateOf("10") }
    var pageMargin       by remember { mutableStateOf("1") }
    var spacingBetween   by remember { mutableStateOf("2") }

    LaunchedEffect(Unit) {
        printerList = PrintServiceLookup.lookupPrintServices(null, null)
            .map { it.name }.sorted()
        selectedPrinter = printerList.firstOrNull()
    }

    val scope  = rememberCoroutineScope()
    val client = remember { HttpClient(CIO) { install(WebSockets) } }
    var wsJob  by remember { mutableStateOf<Job?>(null) }

    fun connectWs() {
        wsJob?.cancel()
        wsJob = scope.launch {
            runCatching {
                client.webSocket(
                    urlString = "ws://localhost:8080/v1/ws/print?token=$token"
                ) {
                    connected = true
                    println("WS conectado!")

                    for (msg in incoming) if (msg is Frame.Text) {
                        printText(
                            printerName = selectedPrinter,
                            text        = msg.readText(),
                            widthMm     = pageWidth,
                            heightMm    = pageHeight,
                            fontSizePt  = fontSize,
                            marginMm    = pageMargin,
                            spacingPt   = spacingBetween
                        )
                    }
                }
            }.onFailure {
                it.printStackTrace()
                connected = false
            }
        }
    }

    LaunchedEffect(selectedPrinter) { if (selectedPrinter != null) connectWs() }

    DisposableEffect(Unit) {
        onDispose { wsJob?.cancel(); client.close() }
    }

    Column(
        modifier = Modifier.padding(24.dp).fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(Modifier.fillMaxWidth(), verticalAlignment=Alignment.CenterVertically) {
            Icon(Icons.Default.Person, null); Spacer(Modifier.width(8.dp))
            Text("Olá, ${user.name}", Modifier.weight(1f))
            IconButton({
                TokenStore.token = null
                onLogout()
            }) { Icon(Icons.Default.Delete, "Sair") }
        }

        Row(Modifier.fillMaxWidth(), verticalAlignment=Alignment.CenterVertically) {
            val color = if (connected) Color(0xFF2E7D32) else MaterialTheme.colors.error
            val text  = if (connected) "Conectado ✓" else "Desconectado"
            Text(text, Modifier.weight(1f), color = color)
            if (!connected) Button(onClick = ::connectWs) { Text("Reconectar") }
        }

        Divider()

        Text("Configurações da Impressão", style = MaterialTheme.typography.h6)

        PrinterSelectWidget(printerList, selectedPrinter) { selectedPrinter = it }
        PageWidthWidget(pageWidth)  { pageWidth  = it }
        PageHeightWidget(pageHeight){ pageHeight = it }
        FontSizeWidget(fontSize)    { fontSize   = it }
        PageMarginWidget(pageMargin){ pageMargin = it }
        SpacingBetweenPrintsWidget(spacingBetween) { spacingBetween = it }

        Spacer(Modifier.height(24.dp))

        PrinterTestButton(
            selectedPrinter, pageWidth, pageHeight,
            fontSize, pageMargin, spacingBetween
        )
    }
}
