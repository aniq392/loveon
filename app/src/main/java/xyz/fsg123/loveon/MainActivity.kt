package xyz.fsg123.loveon

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import xyz.fsg123.loveon.auth.AuthStateManager
import xyz.fsg123.loveon.auth.SharedPreferencesAuthStateStore
import xyz.fsg123.loveon.navigation.LoveOnNavigation
import xyz.fsg123.loveon.ui.theme.LoveOnTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val authStateManager = AuthStateManager(
            SharedPreferencesAuthStateStore(
                getSharedPreferences("loveon_auth", Context.MODE_PRIVATE)
            )
        )

        enableEdgeToEdge()
        setContent {
            LoveOnTheme {
                LoveOnNavigation(authStateManager)
            }
        }
    }
}
