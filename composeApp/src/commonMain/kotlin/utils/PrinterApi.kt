package utils

import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import model.PrintDto
import printText
import javax.print.PrintServiceLookup



fun Application.printApi() {
    install(ContentNegotiation) {
        json()
    }
    routing {
        get("/printe/list") {
            val services = PrintServiceLookup.lookupPrintServices(null, null)
            val names = services.map { it.name }
            call.respond(names)
        }

        post("/printe/print") {
            val req = call.receive<PrintDto>()
            printText(
                printerName   = req.printerName,
                text          = req.text,
                widthMmStr    = req.widthMm.toString(),
                marginMmStr   = "0"
            )
            call.respond(HttpStatusCode.OK, mapOf("status" to "Impressão enviada"))
        }
    }
}