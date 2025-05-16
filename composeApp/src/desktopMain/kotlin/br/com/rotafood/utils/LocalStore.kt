package utils

import java.util.prefs.Preferences

object LocalStore {
    private val prefs = Preferences.userRoot().node("rotafood-printer")
    private const val TOKEN_KEY = "jwt"
    private const val PRITNER_NAME = "printer_name"

    var token: String?
        get() = prefs.get(TOKEN_KEY, null)
        set(value) { value?.let { prefs.put(TOKEN_KEY, it) } ?: prefs.remove(TOKEN_KEY) }

    var printerName: String?
        get() = prefs.get(PRITNER_NAME, null)
        set(value) { value?.let { prefs.put(PRITNER_NAME, it) } ?: prefs.remove(PRITNER_NAME) }
}
