package br.com.rotafood

import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import br.com.rotafood.pages.PrinterManagerPage
import br.com.rotafood.pages.TokenGatePage
import model.MerchantUserDto
import utils.*

@Composable
fun App() {
    MaterialTheme(
        colors = lightColors(
            primary         = Color(0xFF1565C0),
            primaryVariant  = Color(0xFF003c8f),
            secondary       = Color(0xFF1565C0),
        )
    ) {
        var token        by remember { mutableStateOf<String?>(null) }
        var userDto      by remember { mutableStateOf<MerchantUserDto?>(null) }

        LaunchedEffect(Unit) {
            TokenStore.token
                ?.takeIf(JwtUtils::isValid)
                ?.let {
                    token   = it
                    userDto = JwtUtils.decode(it)
                }
        }

        if (token == null) {
            TokenGatePage { newToken ->
                token   = newToken
                userDto = JwtUtils.decode(newToken)
            }
        } else {
            PrinterManagerPage(
                user      = userDto!!,
                token     = token!!,
                onLogout  = {
                    TokenStore.token = null
                    token   = null
                    userDto = null
                }
            )
        }
    }
}
