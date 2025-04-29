package utils

import java.util.prefs.Preferences

object TokenStore {
    private val prefs = Preferences.userRoot().node("rotafood-printer")
    private const val KEY = "jwt"

    var token: String?
        get() = prefs.get(KEY, null)
        set(value) { value?.let { prefs.put(KEY, it) } ?: prefs.remove(KEY) }
}
