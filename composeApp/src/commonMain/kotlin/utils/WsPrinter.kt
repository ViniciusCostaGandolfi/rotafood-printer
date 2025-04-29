package utils

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.client.request.header
import io.ktor.websocket.Frame
import io.ktor.websocket.readText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import javax.print.PrintService

class WsPrinter(
    private val token: String,
    private val service: PrintService,
    private val larguraMm: Int,
) {
    private val client = HttpClient(CIO) { install(WebSockets) }
    private val scope  = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    fun start() = scope.launch {
        client.webSocket(
            urlString = "wss://api.rotafood.com.br/v1/ws/print?token=$token",
        ) {
            println("WS conectado ✓")
            for (msg in incoming) {
                when (msg) {
                    is Frame.Binary -> service.printPdf(msg.data, larguraMm)
                    is Frame.Text   -> println("Texto recebido: " + msg.readText())
                    else            -> {}
                }
            }
        }
    }

    fun stop() {
        scope.cancel()     // cancela todas as corrotinas
        client.close()     // fecha o HttpClient
    }
}
