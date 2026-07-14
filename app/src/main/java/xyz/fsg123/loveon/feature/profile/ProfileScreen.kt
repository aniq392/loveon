package xyz.fsg123.loveon.feature.profile

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import xyz.fsg123.loveon.ui.language.AppLanguage
import xyz.fsg123.loveon.ui.language.LanguagePreferences
import xyz.fsg123.loveon.ui.theme.ThemeMode
import xyz.fsg123.loveon.ui.theme.ThemePreferences

@Composable
fun ProfileScreen(
    onLogout: () -> Unit = {},
    themePreferences: ThemePreferences,
    currentThemeMode: ThemeMode,
    onThemeModeChanged: (ThemeMode) -> Unit,
    languagePreferences: LanguagePreferences,
    currentLanguage: AppLanguage,
    onLanguageChanged: (AppLanguage) -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "This is your profile screen. Edit your info later.",
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "테마 설정",
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedButton(onClick = {
                onThemeModeChanged(ThemeMode.SYSTEM)
            }) {
                Text(text = "시스템 설정 따름")
            }

            OutlinedButton(onClick = {
                onThemeModeChanged(ThemeMode.LIGHT)
            }) {
                Text(text = "화이트 모드")
            }

            OutlinedButton(onClick = {
                onThemeModeChanged(ThemeMode.DARK)
            }) {
                Text(text = "다크 모드")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "언어 설정",
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedButton(onClick = {
                onLanguageChanged(AppLanguage.SYSTEM)
            }) {
                Text(text = "시스템 언어")
            }

            OutlinedButton(onClick = {
                onLanguageChanged(AppLanguage.KOREAN)
            }) {
                Text(text = "한글")
            }

            OutlinedButton(onClick = {
                onLanguageChanged(AppLanguage.ENGLISH)
            }) {
                Text(text = "English")
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(onClick = onLogout) {
                Text(text = "로그아웃")
            }
        }
    }
}
