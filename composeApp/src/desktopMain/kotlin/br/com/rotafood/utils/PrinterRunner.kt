package br.com.rotafood.utils

import kotlinx.coroutines.*

object PrinterRunner {
    private val ctx = newSingleThreadContext("printer-thread")
    private val scope = CoroutineScope(ctx + SupervisorJob())


    fun launchJob(block: suspend () -> Unit) {
        scope.launch {
            try {
                block()
            } finally {
                delay(1_000)   // <- intervalo de segurança entre jobs
            }
        }
    }
}
