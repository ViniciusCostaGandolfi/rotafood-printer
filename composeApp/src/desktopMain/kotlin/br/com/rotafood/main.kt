package br.com.rotafood

import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.Image
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import br.com.rotafood.res.Res
import br.com.rotafood.res.iconPng
import br.com.rotafood.res.rotafoodLogo
import br.com.rotafood.utils.PrintApi
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import kotlinx.coroutines.*
import org.jetbrains.compose.resources.painterResource
import java.awt.Desktop
import java.net.URI

fun main() = application {
    val serverScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    val server = embeddedServer(Netty, port = 9064) {
        PrintApi()
    }

    LaunchedEffect(Unit) {
        serverScope.launch {
            server.start(wait = true)
        }
    }

    Window(
        onCloseRequest = {
            server.stop(1000, 2000)
            serverScope.coroutineContext.cancelChildren()
            exitApplication()
        },
        title = "Rotafood Impressora",
        icon = painterResource(Res.drawable.iconPng)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(Res.drawable.rotafoodLogo),
                contentDescription = "RotaFood Logo",
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Text("Tudo certo! Agora é só você acessar o gestor de pedidos pelo navegador!")
            Button(
                onClick = {
                    if (Desktop.isDesktopSupported()) {
                        Desktop.getDesktop().browse(URI("https://rotafood.com.br/admin/pedidos/gestor"))
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color(0xFF0052C8),
                    contentColor    = Color.White
                ),
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Text("Abrir Gestor de Pedidos")
            }
        }
    }
}
