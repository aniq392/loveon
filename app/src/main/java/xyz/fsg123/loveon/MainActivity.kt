package xyz.fsg123.loveon

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import java.security.MessageDigest
import xyz.fsg123.loveon.auth.AuthStateManager
import xyz.fsg123.loveon.auth.SharedPreferencesAuthStateStore
import xyz.fsg123.loveon.navigation.LoveOnNavigation
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

        enableEdgeToEdge()

        setContent {
            var currentThemeMode by remember { mutableStateOf(themePreferences.getThemeMode()) }

            LoveOnTheme(themeMode = currentThemeMode) {
                LoveOnNavigation(
                    authStateManager = authStateManager,
                    themePreferences = themePreferences,
                    currentThemeMode = currentThemeMode,
                    onThemeModeChanged = { newThemeMode ->
                        themePreferences.setThemeMode(newThemeMode)
                        currentThemeMode = newThemeMode
                    }
                )
            }
        }
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