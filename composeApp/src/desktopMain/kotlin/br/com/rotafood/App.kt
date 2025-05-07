package br.com.rotafood

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import br.com.rotafood.pages.PrinterManagerPage
import br.com.rotafood.pages.TokenGatePage
import model.MerchantUserDto
import utils.*

@Composable
fun App() {
    // 1. Estado de rolagem
    val scrollState = rememberScrollState()

    MaterialTheme(
        colors = lightColors(
            primary        = Color(0xFF1565C0),
            primaryVariant = Color(0xFF003c8f),
            secondary      = Color(0xFF1565C0),
        )
    ) {
        var token   by remember { mutableStateOf<String?>(null) }
        var userDto by remember { mutableStateOf<MerchantUserDto?>(null) }

        // 2. Recupera token salvo, se houver
        LaunchedEffect(Unit) {
            TokenStore.token
                ?.takeIf(JwtUtils::isValid)
                ?.let {
                    token   = it
                    userDto = JwtUtils.decode(it)
                }
        }

        Box {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(16.dp)
            ) {
                if (token == null) {
                    TokenGatePage { newToken ->
                        token   = newToken
                        userDto = JwtUtils.decode(newToken)
                    }
                } else {
                    PrinterManagerPage(
                        user     = userDto!!,
                        token    = token!!,
                        onLogout = {
                            TokenStore.token = null
                            token   = null
                            userDto = null
                        }
                    )
                }
            }

            VerticalScrollbar(
                adapter  = rememberScrollbarAdapter(scrollState),
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .fillMaxHeight()
            )
        }
    }
}
