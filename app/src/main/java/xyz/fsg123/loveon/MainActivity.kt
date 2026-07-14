package xyz.fsg123.loveon

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.os.LocaleListCompat
import java.security.MessageDigest
import xyz.fsg123.loveon.auth.AuthStateManager
import xyz.fsg123.loveon.auth.SharedPreferencesAuthStateStore
import xyz.fsg123.loveon.navigation.LoveOnNavigation
import xyz.fsg123.loveon.ui.language.AppLanguage
import xyz.fsg123.loveon.ui.language.LanguagePreferences
import xyz.fsg123.loveon.ui.theme.LoveOnTheme
import xyz.fsg123.loveon.ui.theme.ThemeMode
import xyz.fsg123.loveon.ui.theme.ThemePreferences

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Key Hash 출력
        printKakaoKeyHash()

        val authStateManager = AuthStateManager(
            SharedPreferencesAuthStateStore(
                getSharedPreferences("loveon_auth", Context.MODE_PRIVATE)
            )
        )
        val themePreferences = ThemePreferences(getSharedPreferences("loveon_theme", Context.MODE_PRIVATE))
        val languagePreferences = LanguagePreferences(getSharedPreferences("loveon_language", Context.MODE_PRIVATE))

        applySelectedLanguage(languagePreferences.getLanguage())

        enableEdgeToEdge()

        setContent {
            var currentThemeMode by remember { mutableStateOf(themePreferences.getThemeMode()) }
            var currentLanguage by remember { mutableStateOf(languagePreferences.getLanguage()) }

            LoveOnTheme(themeMode = currentThemeMode) {
                LoveOnNavigation(
                    authStateManager = authStateManager,
                    themePreferences = themePreferences,
                    currentThemeMode = currentThemeMode,
                    onThemeModeChanged = { newThemeMode ->
                        themePreferences.setThemeMode(newThemeMode)
                        currentThemeMode = newThemeMode
                    },
                    languagePreferences = languagePreferences,
                    currentLanguage = currentLanguage,
                    onLanguageChanged = { newLanguage ->
                        languagePreferences.setLanguage(newLanguage)
                        currentLanguage = newLanguage
                        applySelectedLanguage(newLanguage)
                        recreate()
                    }
                )
            }
        }
    }

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

    private fun printKakaoKeyHash() {
        // 디버그 모드에서만 실행되도록 제한
        if (!BuildConfig.DEBUG) return

        try {
            val packageInfo = packageManager.getPackageInfo(
                packageName,
                PackageManager.GET_SIGNING_CERTIFICATES
            )

            packageInfo.signingInfo?.apkContentsSigners?.forEach { signature ->
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                val keyHash = Base64.encodeToString(md.digest(), Base64.NO_WRAP)
                Log.d("KAKAO_KEYHASH", "KeyHash: $keyHash")
            }
        } catch (e: Exception) {
            Log.e("KAKAO_KEYHASH", "KeyHash 추출 실패", e)
        }
    }
}