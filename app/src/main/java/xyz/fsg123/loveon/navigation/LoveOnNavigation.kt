package xyz.fsg123.loveon.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.delay
import androidx.compose.material3.Text
import xyz.fsg123.loveon.feature.auth.LoginScreen
import xyz.fsg123.loveon.feature.home.HomeScreen


@Composable
fun LoveOnNavigation() {
    // 1. 화면 전환을 제어할 컨트롤러 생성
    val navController = rememberNavController()

    // 2. NavHost를 통해 라우팅 경로(화면 지도) 설정
    NavHost(
        navController = navController,
        startDestination = "splash" // 시작 화면 지정
    ) {
        // 스플래시 화면 경로
        composable("splash") {
            TempSplashScreen(
                onNavigateToLogin = { navController.navigate("login") }
            )
        }

        // 로그인 화면 경로
        composable("login") {
            TempLoginScreen()
        }
    }
}

// 테스트용 임시 스플래시 화면
@Composable
fun TempSplashScreen(onNavigateToLogin: () -> Unit) {
    // 2초 후 로그인 화면으로 이동하는 로직 (기존 유지)
    LaunchedEffect(key1 = true) {
        delay(2000)
        onNavigateToLogin()
    }

    // 화면 가로/세로 정중앙 정렬 적용
    Box(
        modifier = Modifier.fillMaxSize(), // 화면 전체를 채움
        contentAlignment = Alignment.Center // 자식 컴포넌트(Text)를 정중앙에 배치
    ) {
        Text(
            text = "여기는 스플래시 화면입니다. (2초 후 이동)",
            fontSize = 18.sp // 글자 크기 조절 (선택사항)
        )
    }
}

// 테스트용 임시 로그인 화면
@Composable
fun TempLoginScreen() {
    Box(
        modifier = Modifier.fillMaxSize(), // 화면 전체를 채움
        contentAlignment = Alignment.Center // 자식 컴포넌트를 정중앙에 배치
    ) {
        Text(
            text = "여기는 로그인 화면입니다. (임시)",
            fontSize = 18.sp // 글자 크기 조절 (선택사항)
        )
    }
}