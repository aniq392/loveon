package xyz.fsg123.loveon.ui.language

import android.content.SharedPreferences

class LanguagePreferences(private val preferences: SharedPreferences) {
    fun getLanguage(): AppLanguage {
        return preferences.getString(KEY_LANGUAGE, AppLanguage.SYSTEM.name)
            ?.let { name ->
                runCatching { AppLanguage.valueOf(name) }.getOrDefault(AppLanguage.SYSTEM)
            }
            ?: AppLanguage.SYSTEM
    }

    fun setLanguage(language: AppLanguage) {
        preferences.edit().putString(KEY_LANGUAGE, language.name).apply()
    }

    companion object {
        private const val KEY_LANGUAGE = "app_language"
    }
}
