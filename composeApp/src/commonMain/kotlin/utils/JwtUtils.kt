package utils

import kotlinx.serialization.json.*
import model.MerchantUserDto
import java.util.Base64
import java.util.Date

object JwtUtils {

    private val json = Json { ignoreUnknownKeys = true }

    fun decode(token: String): MerchantUserDto? = try {
        val payload = token.split(".").getOrNull(1) ?: return null
        val jsonStr = Base64.getUrlDecoder().decode(payload).toString(Charsets.UTF_8)
        val root    = json.parseToJsonElement(jsonStr).jsonObject

        val merchantUserElement = root["merchantUser"] ?: return null
        json.decodeFromJsonElement<MerchantUserDto>(merchantUserElement)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }

    private fun parsePayload(token: String): JsonObject? = try {
        val payload = token.split(".").getOrNull(1) ?: return null
        val jsonStr = Base64.getUrlDecoder()
            .decode(payload)
            .toString(Charsets.UTF_8)
        json.parseToJsonElement(jsonStr).jsonObject
    } catch (_: Exception) {
        null
    }


    fun isValid(token: String): Boolean = try {
        val parts = token.split(".")

        val payload = parts[1]
        val jsonStr = Base64.getUrlDecoder().decode(payload).toString(Charsets.UTF_8)
        val root    = json.parseToJsonElement(jsonStr).jsonObject

        val exp = root["exp"]?.jsonPrimitive?.longOrNull ?: return false
        exp > Date().time / 1000
    } catch (_: Exception) { false }

    private fun getExp(token: String): Long? {
        val root = parsePayload(token) ?: return null
        return root["exp"]?.jsonPrimitive?.longOrNull
    }


    fun shouldRefresh(token: String, thresholdDays: Long = 1): Boolean {
        val exp = getExp(token) ?: return false
        val now = Date().time / 1000
        val remainingSeconds = exp - now
        val thresholdSeconds = thresholdDays * 24 * 60 * 60 * 3
        return remainingSeconds < thresholdSeconds
    }
}
