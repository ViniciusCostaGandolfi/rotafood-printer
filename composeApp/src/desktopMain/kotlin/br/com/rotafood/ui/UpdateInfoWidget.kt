package br.com.rotafood.ui

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import kotlinx.coroutines.*
import kotlinx.serialization.Serializable
import java.awt.Desktop
import java.security.MessageDigest
import kotlin.system.exitProcess

@Serializable
private data class UpdateInfo(val version: String, val url: String, val sha256: String)

object UpdateCheckerWidget {
    private const val MANIFEST_URL =
        "https://bucket-production-ec49.up.railway.app/rotafood/printer/update.json"

    suspend fun run(currentVersion: String) = coroutineScope {
        val client = HttpClient(CIO)
        val info: UpdateInfo = client.get(MANIFEST_URL).body()

        if (isNewer(info.version, currentVersion)) {
            println("Nova versão disponível: ${info.version}")
            downloadAndInstall(info)
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


    private suspend fun downloadAndInstall(info: UpdateInfo) {
        val tmpFile = kotlin.io.path.createTempFile(suffix = ".msi").toFile()
        println("Baixando instalador para ${tmpFile.name}")

        HttpClient(CIO).use { client ->
            client.get(info.url).body<ByteArray>().also { bytes ->
                val digest = MessageDigest.getInstance("SHA-256")
                    .digest(bytes).joinToString("") { "%02x".format(it) }
                require(digest.equals(info.sha256, ignoreCase = true)) {
                    "SHA-256 não confere!"
                }
                tmpFile.writeBytes(bytes)
            }
        }

        println("Abrindo instalador…")
        Desktop.getDesktop().open(tmpFile)
        exitProcess(0)
    }
}