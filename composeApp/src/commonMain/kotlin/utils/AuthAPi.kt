package utils

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.Serializable


@Serializable
private data class TokenJwtDto(val accessToken: String)
object AuthApi {
    private val client = HttpClient(CIO)

    suspend fun refreshToken(oldToken: String): String {
        val resp: TokenJwtDto = client.post("https://rotafood-api-production.up.railway.app/v1/auth/refresh") {
            contentType(ContentType.Application.Json)
            setBody(TokenJwtDto(oldToken))
        }.body()

        return resp.accessToken
    }
}
