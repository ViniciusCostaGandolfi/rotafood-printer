package br.com.rotafood.pages


import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

import androidx.compose.ui.unit.dp
import utils.JwtUtils
import utils.TokenStore
import java.awt.Desktop
import java.net.URI

@Composable
fun TokenGatePage(onTokenReady: (String) -> Unit) {

    LaunchedEffect(Unit) {
        TokenStore.token
            ?.takeIf(JwtUtils::isValid)
            ?.let(onTokenReady)
    }

    var tokenInput   by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier.padding(24.dp).fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Text("Precisamos do token de impressão", style = MaterialTheme.typography.h5)

        val url = "https://rotafood.com.br/admin/configurar-impressora"
        Button(
            onClick = {
                runCatching { Desktop.getDesktop().browse(URI(url)) }
            },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color(0xFF1565C0),
                contentColor = Color.White
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Ver Tutorial")
        }




        OutlinedTextField(
            value = tokenInput,
            onValueChange = {
                tokenInput = it
                errorMessage = null
            },
            label = { Text("Cole aqui o seu token JWT") },
            modifier = Modifier.fillMaxWidth()
        )

        errorMessage?.let {
            Text(it, color = MaterialTheme.colors.error)
        }

        Button(
            onClick = {
                val trimmedToken = tokenInput.trim()

                if (JwtUtils.isValid(trimmedToken)) {
                    val merchantUser = JwtUtils.decode(trimmedToken)

                    if (merchantUser != null) {
                        TokenStore.token = trimmedToken
                        onTokenReady(trimmedToken)

                        println("Token decodificado:")
                        println("Merchant ID: ${merchantUser.merchantId}")
                        println("Merchant Name: ${merchantUser.name}")
                    } else {
                        errorMessage = "Erro ao decodificar token."
                    }

                } else {
                    errorMessage = "Token inválido – verifique se copiou completo."
                }
            },
            enabled  = tokenInput.isNotBlank(),
            modifier = Modifier.fillMaxWidth(),
            colors   = ButtonDefaults.buttonColors(
                backgroundColor = Color(0xFF1565C0),
                contentColor    = Color.White
            )
        ) {
            Text("Validar e Salvar")
        }

    }
}
