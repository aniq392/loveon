

// feature/auth/login/LoginScreen.kt
package xyz.fsg123.loveon.feature.auth.login

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import xyz.fsg123.loveon.auth.AuthStateManager
import xyz.fsg123.loveon.auth.SocialLoginManager
import xyz.fsg123.loveon.data.SocialLoginRequest
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts

@Composable
fun LoginScreen(
    authStateManager: AuthStateManager,
    onLoginSuccess: () -> Unit,
    viewModel: LoginViewModel = viewModel(
        factory = LoginViewModelFactory(authStateManager)
    )
) {
    val context = LocalContext.current
    val activity = LocalActivity.current as? androidx.activity.ComponentActivity
    val uiState by viewModel.uiState.collectAsState()

    var email by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    val isEmailValid = remember(email) {
        android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    // 소셜 로그인 매니저
    var socialLoginManager: SocialLoginManager? by remember { mutableStateOf(null) }

    val googleLoginLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        socialLoginManager?.handleGoogleResult(result)
    }

    // 로그인 성공 처리
    val handleLoginSuccess: (String, String, String, String) -> Unit = { socialType, socialId, email, nickname ->
        val request = SocialLoginRequest(
            social_type = socialType,
            social_id = socialId,
            email = email,
            nickname = nickname
        )
        viewModel.socialLogin(request)
    }

    // SocialLoginManager 초기화
    if (activity != null && socialLoginManager == null) {
        socialLoginManager = SocialLoginManager(
            activity = activity,
            googleLoginLauncher = googleLoginLauncher,
            onSuccess = { provider ->
                // 실제 구현 시 provider에 따라 사용자 정보 수집 필요
                // 여기는 예시로 더미 데이터 사용
                handleLoginSuccess(
                    provider,
                    "${provider}_user_12345",
                    "user@$provider.com",
                    "${provider}사용자"
                )
            },
            onError = { message ->
                errorMessage = message
            }
        )
    }

    // 로그인 성공 시 콜백
    LaunchedEffect(uiState) {
        if (uiState is LoginUiState.Success) {
            onLoginSuccess()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "LoveOn",
            fontSize = 36.sp,
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // 상태 표시
        when (uiState) {
            is LoginUiState.Loading -> {
                CircularProgressIndicator()
                Text("로그인 중...")
            }
            is LoginUiState.Error -> {
                Text(
                    text = (uiState as LoginUiState.Error).message,
                    color = MaterialTheme.colorScheme.error
                )
            }
            is LoginUiState.UpdateRequired -> {
                val state = uiState as LoginUiState.UpdateRequired
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = state.message,
                        color = MaterialTheme.colorScheme.error
                    )
                    Text(
                        text = "필수 버전: ${state.minVersion}",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Button(
                        onClick = {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(state.updateUrl))
                            context.startActivity(intent)
                        }
                    ) {
                        Text("업데이트 하기")
                    }
                }
            }
            else -> {}
        }

        if (errorMessage.isNotEmpty()) {
            Text(text = errorMessage, color = MaterialTheme.colorScheme.error, fontSize = 14.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 이메일 로그인
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

        Button(
            onClick = {
                if (isEmailValid) {
                    handleLoginSuccess("email", email, email, email.split("@")[0])
                } else {
                    errorMessage = "올바른 이메일 형식이 아닙니다."
                }
            },
            enabled = email.isNotEmpty(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("이메일로 계속하기")
        }

        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
        )

        // 카카오 로그인
        Button(
            onClick = { socialLoginManager?.startKakaoLogin() },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFEE500)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("카카오톡으로 시작하기", color = Color(0xFF191919))
        }

        // 네이버 로그인
        Button(
            onClick = { socialLoginManager?.startNaverLogin() },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF03C75A)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("네이버로 시작하기", color = Color.White)
        }

        // 구글 로그인
        OutlinedButton(
            onClick = { socialLoginManager?.startGoogleLogin() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Google 계정으로 시작하기", color = Color.Black)
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 게스트 로그인
        TextButton(
            onClick = {
                handleLoginSuccess("guest", "guest_12345", "guest@loveon.com", "게스트")
            }
        ) {
            Text("로그인 없이 둘러보기", color = MaterialTheme.colorScheme.secondary)
        }
    }
}