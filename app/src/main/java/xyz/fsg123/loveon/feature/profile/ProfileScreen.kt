// feature/profile/ProfileScreen.kt
package xyz.fsg123.loveon.feature.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import xyz.fsg123.loveon.auth.AuthStateManager
import xyz.fsg123.loveon.ui.language.AppLanguage
import xyz.fsg123.loveon.ui.language.LanguagePreferences
import xyz.fsg123.loveon.ui.theme.ThemeMode
import xyz.fsg123.loveon.ui.theme.ThemePreferences

@Composable
fun ProfileScreen(
    authStateManager: AuthStateManager,
    onLogout: () -> Unit = {},
    onLiveStream: () -> Unit = {},
    themePreferences: ThemePreferences,
    currentThemeMode: ThemeMode,
    onThemeModeChanged: (ThemeMode) -> Unit,
    languagePreferences: LanguagePreferences,
    currentLanguage: AppLanguage,
    onLanguageChanged: (AppLanguage) -> Unit
) {
    val member = authStateManager.member.value
    val isBlacklisted = authStateManager.isBlacklisted.value
    val mbLevel = authStateManager.mbLevel.value

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // 프로필 정보 카드
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "내 프로필",
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "닉네임: ${member?.mb_nick ?: "Unknown"}",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "이메일: ${member?.mb_email ?: "Unknown"}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(4.dp))

                val permissionText = if (isBlacklisted) {
                    "⚠️ 읽기 전용 (블랙리스트)"
                } else {
                    "✅ 일반 회원 (레벨 $mbLevel)"
                }
                Text(
                    text = permissionText,
                    style = MaterialTheme.typography.bodySmall,
                    color = if (isBlacklisted) {
                        MaterialTheme.colorScheme.error
                    } else {
                        MaterialTheme.colorScheme.primary
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 블랙리스트 경고
        if (isBlacklisted) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Text(
                    text = "⚠️ 현재 읽기 전용 모드입니다.\n게시글 작성 및 댓글 기능이 제한됩니다.",
                    modifier = Modifier.padding(16.dp),
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
        }

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

        OutlinedButton(onClick = { onThemeModeChanged(ThemeMode.SYSTEM) }) {
            Text(text = "시스템 설정 따름")
        }
        OutlinedButton(onClick = { onThemeModeChanged(ThemeMode.LIGHT) }) {
            Text(text = "화이트 모드")
        }
        OutlinedButton(onClick = { onThemeModeChanged(ThemeMode.DARK) }) {
            Text(text = "다크 모드")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "언어 설정",
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedButton(onClick = { onLanguageChanged(AppLanguage.SYSTEM) }) {
            Text(text = "시스템 언어")
        }
        OutlinedButton(onClick = { onLanguageChanged(AppLanguage.KOREAN) }) {
            Text(text = "한글")
        }
        OutlinedButton(onClick = { onLanguageChanged(AppLanguage.ENGLISH) }) {
            Text(text = "English")
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = onLiveStream) {
            Text(text = "실시간 방송 보기")
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = onLogout) {
            Text(text = "로그아웃")
        }
    }
}