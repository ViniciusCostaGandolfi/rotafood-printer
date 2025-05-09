package utils

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.websocket.Frame
import io.ktor.websocket.readBytes
import io.ktor.websocket.readText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import printText

class WsPrinter(
    private val token: String,
    private val printerName: String?,
    private val widthMm: String,
    private val fontSizePt: String,
    private val marginMm: String,
) {
    private val client = HttpClient(CIO) { install(WebSockets) }
    private val scope  = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    fun start() = scope.launch {
        client.webSocket(
            urlString = "wss://rotafood-api-production.up.railway.app/v1/ws/print?token=$token",
        ) {
            println("WS conectado ✓")
            for (msg in incoming) {
                when (msg) {
                    is Frame.Binary -> {
                        val text = msg.readBytes().decodeToString()
                        printText(
                            printerName,
                            text,
                            widthMm,
                            fontSizePt,
                            marginMm
                        )
                    }
                    is Frame.Text -> printText( printerName,
                        msg.readText(),
                        widthMm,
                        fontSizePt,
                        marginMm)
                    else -> {}
                }
            }
        }
    }

    fun stop() {
        scope.cancel()
        client.close()
    }
}
