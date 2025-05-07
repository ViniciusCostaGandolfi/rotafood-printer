package br.com.rotafood.ui

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.okhttp.OkHttp        // ← engine correto
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.coroutineScope
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.awt.Desktop
import javax.swing.JOptionPane
import kotlin.io.path.createTempFile
import kotlin.system.exitProcess

@Serializable
private data class UpdateInfo(
    val lastVersion: String,
    val downloadLink: String
)

private fun createClient() = HttpClient(OkHttp) {
    install(ContentNegotiation) {
        json(
            Json {
                ignoreUnknownKeys = true
                prettyPrint       = false
                isLenient         = true
            }
        )
    }
}

object UpdateCheckerWidget {

    private const val UPDATE_URL =
        "https://rotafood-bucket.up.railway.app/rotafood/printer/update.json"

    suspend fun run(currentVersion: String) = coroutineScope {
        val client = createClient()
        val info: UpdateInfo = client.get(UPDATE_URL).body()

        if (isNewer(info.lastVersion, currentVersion)) {
            val choice = JOptionPane.showConfirmDialog(
                null,
                "Nova versão disponível: ${info.lastVersion}\nDeseja baixar e instalar agora?",
                "Atualização Disponível",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.INFORMATION_MESSAGE
            )

            if (choice == JOptionPane.YES_OPTION) {
                downloadAndInstall(info.downloadLink)
            } else {
                println("Atualização cancelada pelo usuário.")
            }
        } else {
            println("Já está na versão mais recente ($currentVersion)")
        }
    }

    private fun isNewer(remote: String, local: String): Boolean {
        val r = remote.split('.').map { it.toIntOrNull() ?: 0 }
        val l = local.split('.').map { it.toIntOrNull() ?: 0 }
        val max = maxOf(r.size, l.size)
        for (i in 0 until max) {
            val rv = r.getOrElse(i) { 0 }
            val lv = l.getOrElse(i) { 0 }
            if (rv != lv) return rv > lv
        }
        return false
    }

    private suspend fun downloadAndInstall(downloadLink: String) {
        val suffix = downloadLink.substringAfterLast('.', ".exe")
        val tmp = createTempFile(suffix = suffix).toFile()
        println("Baixando instalador para ${tmp.absolutePath}…")

        HttpClient(OkHttp).use { client ->
            val bytes: ByteArray = client.get(downloadLink).body()
            tmp.writeBytes(bytes)
        }

        println("Abrindo instalador…")
        Desktop.getDesktop().open(tmp)
        exitProcess(0)
    }
}
