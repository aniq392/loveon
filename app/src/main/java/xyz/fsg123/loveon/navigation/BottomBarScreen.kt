package xyz.fsg123.loveon.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomBarScreen(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Home : BottomBarScreen("home", "Home", Icons.Filled.Home)
    object Community : BottomBarScreen("community", "Community", Icons.Filled.Group)
    object Create : BottomBarScreen("create", "Create", Icons.Filled.Add)
    object Notifications : BottomBarScreen("notifications", "Notifications", Icons.Filled.Notifications)
    object Profile : BottomBarScreen("profile", "Profile", Icons.Filled.Person)

    companion object {
        // by lazy를 사용하여 초기화 시점을 뒤로 미룹니다.
        val items by lazy {
            listOf(Home, Community, Create, Notifications, Profile)
        }
    }
}