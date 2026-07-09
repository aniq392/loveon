package xyz.fsg123.loveon.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

import xyz.fsg123.loveon.R

sealed class BottomBarScreen(
    val route: String,
    val titleRes: Int,
    val icon: ImageVector
) {
    object Home : BottomBarScreen("home", R.string.home, Icons.Filled.Home)
    object Community : BottomBarScreen("community", R.string.community, Icons.Filled.Group)
    object Create : BottomBarScreen("create", R.string.create, Icons.Filled.Add)
    object Notifications : BottomBarScreen("notifications", R.string.notifications, Icons.Filled.Notifications)
    object Profile : BottomBarScreen("profile", R.string.profile, Icons.Filled.Person)

    companion object {
        val items = listOf(Home, Community, Create, Notifications, Profile)
    }
}