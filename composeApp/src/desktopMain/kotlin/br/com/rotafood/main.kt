package br.com.rotafood

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import br.com.rotafood.res.Res
import br.com.rotafood.res.iconPng
import org.jetbrains.compose.resources.painterResource


fun main() = application {

    Window(
        onCloseRequest = ::exitApplication,
        title = "RotaFood Printer",
        icon =  painterResource(Res.drawable.iconPng)
    ) {
        App()
    }
}
