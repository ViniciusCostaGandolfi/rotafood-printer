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
    val scrollState = rememberScrollState()

    MaterialTheme(
        colors = lightColors(
            primary = Color(0xFF1565C0),
            primaryVariant = Color(0xFF003c8f),
            secondary = Color(0xFF1565C0),
        )
    ) {
        var token by remember { mutableStateOf<String?>(null) }
        var userDto by remember { mutableStateOf<MerchantUserDto?>(null) }

        LaunchedEffect(Unit) {
            TokenStore.token?.let { saved ->
                if (JwtUtils.isValid(saved)) {
                    val finalToken = if (JwtUtils.shouldRefresh(saved)) {
                        try {
                            val fresh = AuthApi.refreshToken(saved)
                            TokenStore.token = fresh
                            fresh
                        } catch (e: Exception) {
                            saved
                        }
                    } else {
                        saved
                    }

                    token = finalToken
                    userDto = JwtUtils.decode(finalToken)
                }
            }
        }

        Box {
            Column(
                Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                if (token == null) {
                    TokenGatePage { newToken ->
                        TokenStore.token = newToken
                        token = newToken
                        userDto = JwtUtils.decode(newToken)
                    }
                } else {
                    PrinterManagerPage(
                        user = userDto!!,
                        token = token!!,
                        onLogout = {
                            TokenStore.token = null
                            token = null
                            userDto = null
                        }
                    )
                }
            }

            VerticalScrollbar(
                adapter = rememberScrollbarAdapter(rememberScrollState()),
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .fillMaxHeight()
            )
        }
    }
}