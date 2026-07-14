package xyz.fsg123.loveon.ui.theme

import android.content.SharedPreferences

class ThemePreferences(private val preferences: SharedPreferences) {
    fun getThemeMode(): ThemeMode {
        return preferences.getString(KEY_THEME_MODE, ThemeMode.SYSTEM.name)
            ?.let { name ->
                runCatching { ThemeMode.valueOf(name) }.getOrDefault(ThemeMode.SYSTEM)
            }
            ?: ThemeMode.SYSTEM
    }

    fun setThemeMode(themeMode: ThemeMode) {
        preferences.edit().putString(KEY_THEME_MODE, themeMode.name).apply()
    }

    companion object {
        private const val KEY_THEME_MODE = "theme_mode"
    }
}
