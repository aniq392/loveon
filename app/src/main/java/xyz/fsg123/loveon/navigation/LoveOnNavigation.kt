package xyz.fsg123.loveon.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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


@Composable
fun LoveOnNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {
        composable("splash") {
            TempSplashScreen(onNavigateToLogin = { navController.navigate("login") })
        }

        composable("login") {
            TempLoginScreen(onLoginSuccess = {
                navController.navigate("main_app") {
                    popUpTo("login") { inclusive = true }
                }
            })
        }

        composable("main_app") {
            MainAppScreen()
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

@Composable
fun TempLoginScreen(onLoginSuccess: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Button(
            onClick = onLoginSuccess,
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = stringResource(id = R.string.go_to_main_app))
        }
    }
}
