// navigation/LoveOnNavigation.kt
package xyz.fsg123.loveon.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.delay
import xyz.fsg123.loveon.R
import xyz.fsg123.loveon.auth.AuthStateManager
import xyz.fsg123.loveon.feature.auth.login.LoginScreen
import xyz.fsg123.loveon.ui.language.AppLanguage
import xyz.fsg123.loveon.ui.language.LanguagePreferences
import xyz.fsg123.loveon.ui.theme.ThemeMode
import xyz.fsg123.loveon.ui.theme.ThemePreferences

@Composable
fun LoveOnNavigation(
    authStateManager: AuthStateManager,
    themePreferences: ThemePreferences,
    currentThemeMode: ThemeMode,
    onThemeModeChanged: (ThemeMode) -> Unit,
    languagePreferences: LanguagePreferences,
    currentLanguage: AppLanguage,
    onLanguageChanged: (AppLanguage) -> Unit
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {
        composable("splash") {
            TempSplashScreen(
                onNavigateToLogin = {
                    if (authStateManager.isLoggedIn.value) {
                        navController.navigate("main_app") {
                            popUpTo("splash") { inclusive = true }
                        }
                    } else {
                        navController.navigate("login") {
                            popUpTo("splash") { inclusive = true }
                        }
                    }
                }
            )
        }

        composable("login") {
            LoginScreen(
                authStateManager = authStateManager,
                onLoginSuccess = {
                    navController.navigate("main_app") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }

        composable("main_app") {
            MainAppScreen(
                authStateManager = authStateManager,
                onLogout = {
                    authStateManager.logout()
                    navController.navigate("login") {
                        popUpTo("main_app") { inclusive = true }
                    }
                },
                themePreferences = themePreferences,
                currentThemeMode = currentThemeMode,
                onThemeModeChanged = onThemeModeChanged,
                languagePreferences = languagePreferences,
                currentLanguage = currentLanguage,
                onLanguageChanged = onLanguageChanged
            )
        }
    }
}

@Composable
fun TempSplashScreen(onNavigateToLogin: () -> Unit) {
    LaunchedEffect(key1 = true) {
        delay(2000)
        onNavigateToLogin()
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(id = R.string.splash_text),
            fontSize = 18.sp
        )
    }
}