package br.com.rotafood

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import br.com.rotafood.res.Res
import br.com.rotafood.res.iconPng
import br.com.rotafood.ui.UpdateCheckerWidget
import kotlinx.coroutines.runBlocking
import org.jetbrains.compose.resources.painterResource


fun currentVersion(): String =
    UpdateCheckerWidget::class.java.`package`
        .implementationVersion
        ?: "1.0.0"

fun main() = application {
//    runBlocking {
//        UpdateCheckerWidget.run(currentVersion())
//    }

    Window(
        onCloseRequest = ::exitApplication,
        title = "RotaFood Printer",
        icon =  painterResource(Res.drawable.iconPng)
    ) {
        App()
    }
}
