package utils

import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import model.PrintDto
import printText

fun Application.PrintApi() {
    install(CORS) {
        anyHost()
        allowNonSimpleContentTypes = true
        allowMethod(HttpMethod.Options)
        allowMethod(HttpMethod.Get)
        allowMethod(HttpMethod.Post)
        allowHeader(HttpHeaders.ContentType)
        allowCredentials = true
    }
    install(ContentNegotiation) { json() }

    routing {
        get("/printer/list") {
            val names = javax.print.PrintServiceLookup
                .lookupPrintServices(null, null)
                .map { it.name }
            call.respond(names)
        }

        post("/printer/print") {
            val req = call.receive<PrintDto>()
            val result = try {
                withContext(Dispatchers.IO) {
                    printText(
                        printerName = req.printerName,
                        text        = req.text,
                        widthMm     = req.widthMm,
                        marginPt    = req.marginPt,
                        useStyle    = req.useStyle
                    )
                }
                mapOf("status" to "Impressão enviada")
            } catch (t: Throwable) {
                application.environment.log.error("Erro ao imprimir", t)
                mapOf("error" to (t.message ?: "erro desconhecido"))
            }
            call.respond(HttpStatusCode.OK, result)
        }
    }
}
