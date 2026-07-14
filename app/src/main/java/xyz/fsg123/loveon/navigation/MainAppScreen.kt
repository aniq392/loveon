package xyz.fsg123.loveon.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding // 패딩 추가
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy // hierarchy 추가
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
import xyz.fsg123.loveon.ui.theme.ThemeMode
import xyz.fsg123.loveon.ui.theme.ThemePreferences

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppScreen(
    onLogout: () -> Unit,
    themePreferences: ThemePreferences,
    currentThemeMode: ThemeMode,
    onThemeModeChanged: (ThemeMode) -> Unit
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val currentRoute = currentDestination?.route

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(
                            // 널 안전성을 위해 currentRoute가 null일 때 대처 추가
                            id = BottomBarScreen.getTitleResForRoute(currentRoute)
                        )
                    )
                }
            )
        },
        bottomBar = {
            NavigationBar {
                // 이전 답변대로 BottomBarScreen에 'by lazy'를 적용했다면 호출 시점에 안전하게 가져옴
                BottomBarScreen.items.forEach { screen ->
                    val isSelected = currentDestination?.hierarchy?.any { it.route == screen.route } == true

                    NavigationBarItem(
                        selected = isSelected,
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
                            Icon(
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
            // 중요: innerPadding을 넣어주어야 컨텐츠가 상하단 바에 가려지지 않아!
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            composable(BottomBarScreen.Home.route) { HomeScreen() }
            composable(BottomBarScreen.Community.route) { CommunityScreen() }
            composable(BottomBarScreen.Create.route) { CreateScreen() }
            composable(BottomBarScreen.Notifications.route) { NotificationsScreen() }
            composable(BottomBarScreen.Profile.route) {
                ProfileScreen(
                    onLogout = onLogout,
                    themePreferences = themePreferences,
                    currentThemeMode = currentThemeMode,
                    onThemeModeChanged = onThemeModeChanged
                )
            }
        }
    }
}