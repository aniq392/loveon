package xyz.fsg123.loveon.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import xyz.fsg123.loveon.R
import xyz.fsg123.loveon.feature.community.CommunityScreen
import xyz.fsg123.loveon.feature.create.CreateScreen
import xyz.fsg123.loveon.feature.home.HomeScreen
import xyz.fsg123.loveon.feature.notifications.NotificationsScreen
import xyz.fsg123.loveon.feature.profile.ProfileScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppScreen() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val currentRoute = currentDestination?.route ?: BottomBarScreen.Home.route

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(
                            id = BottomBarScreen.getTitleResForRoute(currentRoute)
                        )
                    )
                }
            )
        },
        bottomBar = {
            NavigationBar {
                BottomBarScreen.items.forEach { screen ->
                    NavigationBarItem(
                        selected = currentRoute == screen.route,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            androidx.compose.material3.Icon(
                                imageVector = screen.icon,
                                contentDescription = stringResource(id = screen.titleRes)
                            )
                        },
                        label = { Text(text = stringResource(id = screen.titleRes)) }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = BottomBarScreen.Home.route,
            modifier = Modifier.fillMaxSize()
        ) {
            composable(BottomBarScreen.Home.route) {
                HomeScreen()
            }
            composable(BottomBarScreen.Community.route) {
                CommunityScreen()
            }
            composable(BottomBarScreen.Create.route) {
                CreateScreen()
            }
            composable(BottomBarScreen.Notifications.route) {
                NotificationsScreen()
            }
            composable(BottomBarScreen.Profile.route) {
                ProfileScreen()
            }
        }
    }
}
