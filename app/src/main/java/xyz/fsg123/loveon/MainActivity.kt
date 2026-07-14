package xyz.fsg123.loveon

import android.content.Context
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.os.LocaleListCompat
import xyz.fsg123.loveon.auth.AuthStateManager
import xyz.fsg123.loveon.auth.SharedPreferencesAuthStateStore
import xyz.fsg123.loveon.navigation.LoveOnNavigation
import xyz.fsg123.loveon.ui.language.AppLanguage
import xyz.fsg123.loveon.ui.language.LanguagePreferences
import xyz.fsg123.loveon.ui.theme.LoveOnTheme
import xyz.fsg123.loveon.ui.theme.ThemePreferences

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        // 1. 언어 설정 불러오기 및 초기 적용 (super.onCreate 이전에 실행하여 깜빡임 방지)
        val languagePreferences = LanguagePreferences(
            getSharedPreferences("loveon_language", Context.MODE_PRIVATE)
        )
        applySelectedLanguage(languagePreferences.getLanguage())

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // 2. 인증 및 테마 데이터 저장소 초기화
        val authStateManager = AuthStateManager(
            SharedPreferencesAuthStateStore(
                getSharedPreferences("loveon_auth", Context.MODE_PRIVATE)
            )
        )
        val themePreferences = ThemePreferences(
            getSharedPreferences("loveon_theme", Context.MODE_PRIVATE)
        )

        // 3. Compose UI 렌더링
        setContent {
            var currentThemeMode by remember { mutableStateOf(themePreferences.getThemeMode()) }
            var currentLanguage by remember { mutableStateOf(languagePreferences.getLanguage()) }

            LoveOnTheme(themeMode = currentThemeMode) {
                LoveOnNavigation(
                    authStateManager = authStateManager,
                    themePreferences = themePreferences,
                    currentThemeMode = currentThemeMode,
                    onThemeModeChanged = { mode ->
                        themePreferences.setThemeMode(mode)
                        currentThemeMode = mode
                    },
                    languagePreferences = languagePreferences,
                    currentLanguage = currentLanguage,
                    onLanguageChanged = { language ->
                        languagePreferences.setLanguage(language)
                        currentLanguage = language
                        applySelectedLanguage(language)
                    }
                )
            }
        }
    }

    /**
     * 선택된 언어 설정을 앱 전체 시스템 로케일에 반영합니다.
     */
    private fun applySelectedLanguage(language: AppLanguage) {
        val localeTag = when (language) {
            AppLanguage.KOREAN -> "ko"
            AppLanguage.ENGLISH -> "en"
            AppLanguage.SYSTEM -> ""
        }

        val locales = if (localeTag.isEmpty()) {
            LocaleListCompat.getEmptyLocaleList()
        } else {
            LocaleListCompat.forLanguageTags(localeTag)
        }

        AppCompatDelegate.setApplicationLocales(locales)
    }
}