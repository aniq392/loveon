package xyz.fsg123.loveon.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.delay
import xyz.fsg123.loveon.R
import xyz.fsg123.loveon.auth.AuthStateManager
import xyz.fsg123.loveon.auth.SocialLoginManager
import androidx.activity.compose.LocalActivity
import xyz.fsg123.loveon.ui.theme.ThemeMode
import xyz.fsg123.loveon.ui.theme.ThemePreferences
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.ComponentActivity

@Composable
fun LoveOnNavigation(
    authStateManager: AuthStateManager,
    themePreferences: ThemePreferences,
    currentThemeMode: ThemeMode,
    onThemeModeChanged: (ThemeMode) -> Unit
) {
    val navController = rememberNavController()
    val activity = LocalActivity.current

    // 1. 공통으로 사용할 네비게이션 성공 처리를 람다로 분리
    val handleLoginSuccess: (String) -> Unit = { provider ->
        authStateManager.login(provider)
        navController.navigate("main_app") {
            popUpTo("login") { inclusive = true }
        }
    }

    // 2. 구글 런처 선언 (지연 초기화될 manager의 handleGoogleResult를 호출)
    var socialLoginManager: SocialLoginManager? by remember { mutableStateOf(null) }

    val googleLoginLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        socialLoginManager?.handleGoogleResult(result)
    }

    // 3. 버튼 클릭 시 사용될 소셜로그인 매니저 싱글 인스턴스 기억하기
    // LocalActivity.current를 ComponentActivity로 캐스팅해 줍니다.
    val componentActivity = activity as? ComponentActivity

    if (componentActivity != null && socialLoginManager == null) {
        socialLoginManager = SocialLoginManager(
            activity = componentActivity, // 👈 에러 해결: ComponentActivity로 캐스팅된 변수 대입
            googleLoginLauncher = googleLoginLauncher,
            onSuccess = handleLoginSuccess,
            onError = { message -> println("로그인 에러: $message") }
        )
    }

    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {
        composable("splash") {
            TempSplashScreen(
                onNavigateToLogin = {
                    if (authStateManager.isLoggedIn()) {
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
            TempLoginScreen(
                onEmailLoginClick = { emailAddress ->
                    handleLoginSuccess("email")
                },
                onGoogleLoginClick = {
                    socialLoginManager?.startGoogleLogin()
                },
                onKakaoLoginClick = {
                    socialLoginManager?.startKakaoLogin()
                },
                onNaverLoginClick = {
                    socialLoginManager?.startNaverLogin()
                },
                onGuestLogin = {
                    handleLoginSuccess("guest")
                }
            )
        }

        composable("main_app") {
            MainAppScreen(
                onLogout = {
                    authStateManager.logout()
                    navController.navigate("login") {
                        popUpTo("main_app") { inclusive = true }
                    }
                },
                themePreferences = themePreferences,
                currentThemeMode = currentThemeMode,
                onThemeModeChanged = onThemeModeChanged
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

@Composable
fun TempLoginScreen(
    onEmailLoginClick: (String) -> Unit,
    onGoogleLoginClick: () -> Unit,
    onKakaoLoginClick: () -> Unit,
    onNaverLoginClick: () -> Unit,
    onGuestLogin: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    val isEmailValid = remember(email) {
        android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "LoveOn",
                fontSize = 36.sp,
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    errorMessage = ""
                },
                label = { Text("이메일 주소 입력") },
                placeholder = { Text("example@email.com") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth()
            )

            if (errorMessage.isNotEmpty()) {
                Text(text = errorMessage, color = MaterialTheme.colorScheme.error, fontSize = 14.sp)
            }

            Button(
                onClick = {
                    if (isEmailValid) {
                        onEmailLoginClick(email)
                    } else {
                        errorMessage = "올바른 이메일 형식이 아닙니다."
                    }
                },
                enabled = email.isNotEmpty(),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("이메일로 계속하기")
            }

            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                HorizontalDivider(modifier = Modifier.weight(1f))
                Text(" 또는 ", modifier = Modifier.padding(horizontal = 8.dp), color = Color.Gray, fontSize = 12.sp)
                HorizontalDivider(modifier = Modifier.weight(1f))
            }

            Button(
                onClick = onKakaoLoginClick,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFEE500)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("카카오톡으로 시작하기", color = Color(0xFF191919))
            }

            Button(
                onClick = onNaverLoginClick,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF03C75A)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("네이버로 시작하기", color = Color.White)
            }

            OutlinedButton(
                onClick = onGoogleLoginClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Google 계정으로 시작하기", color = Color.Black)
            }

            Spacer(modifier = Modifier.height(24.dp))

            TextButton(onClick = onGuestLogin) {
                Text("로그인 없이 둘러보기", color = MaterialTheme.colorScheme.secondary)
            }
        }
    }
}

//