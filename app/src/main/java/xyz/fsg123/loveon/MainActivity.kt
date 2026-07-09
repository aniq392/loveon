package xyz.fsg123.loveon

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import xyz.fsg123.loveon.ui.theme.LoveOnTheme
import xyz.fsg123.loveon.navigation.LoveOnNavigation

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LoveOnTheme {
                LoveOnNavigation()
            }
        }
    }
}
