package xyz.fsg123.loveon.auth

import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.user.UserApiClient
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.OAuthLoginCallback
import xyz.fsg123.loveon.BuildConfig
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import android.app.Activity
import com.google.android.gms.common.api.ApiException

class SocialLoginManager(
    private val activity: ComponentActivity,
    private val googleLoginLauncher: ActivityResultLauncher<Intent>,
    private val onSuccess: (String) -> Unit,
    private val onError: (String) -> Unit,
) {
    private var googleSignInClient: GoogleSignInClient? = null

    init {
        initializeGoogleClient()
        initializeKakaoSdk()
    }

    private fun initializeGoogleClient() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(BuildConfig.GOOGLE_WEB_CLIENT_ID) // ⭐ 발급받은 웹 클라이언트 ID를 전달해야 정상 연동됩니다.
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(activity, gso)
    }

    private fun initializeKakaoSdk() {
        if (BuildConfig.KAKAO_NATIVE_APP_KEY.isBlank() ||
            BuildConfig.KAKAO_NATIVE_APP_KEY.contains("YOUR")
        ) {
            return
        }

        KakaoSdk.init(
            activity.applicationContext,
            BuildConfig.KAKAO_NATIVE_APP_KEY
        )
    }





    fun startGoogleLogin() {
        val client = googleSignInClient ?: run {
            onError("Google 로그인 클라이언트를 초기화하지 못했습니다.")
            return
        }
        googleLoginLauncher.launch(client.signInIntent)
    }


    fun handleGoogleResult(result: ActivityResult) {
        if (result.resultCode != Activity.RESULT_OK) {
            onError("Google 로그인이 취소되었습니다.")
            return
        }

        try {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            val account = task.getResult(ApiException::class.java)

            // account.idToken
            // account.email
            // account.displayName

            onSuccess("google")

        } catch (e: ApiException) {
            onError(e.localizedMessage ?: "Google 로그인 실패")
        }
    }

    fun startKakaoLogin() {
        if (BuildConfig.KAKAO_NATIVE_APP_KEY.isBlank() || BuildConfig.KAKAO_NATIVE_APP_KEY.contains("YOUR")) {
            onError("카카오 앱 키를 먼저 설정해 주세요.")
            return
        }

        if (UserApiClient.instance.isKakaoTalkLoginAvailable(activity)) {
            UserApiClient.instance.loginWithKakaoTalk(activity) { token, error ->
                if (error != null) {
                    onError(error.message ?: "카카오 로그인 실패")
                } else if (token != null) {
                    onSuccess("kakao")
                }
            }
        } else {
            UserApiClient.instance.loginWithKakaoAccount(activity) { token, error ->
                if (error != null) {
                    onError(error.message ?: "카카오 로그인 실패")
                } else if (token != null) {
                    onSuccess("kakao")
                }
            }
        }
    }

    fun startNaverLogin() {
        if (BuildConfig.NAVER_CLIENT_ID.isBlank() || BuildConfig.NAVER_CLIENT_SECRET.isBlank()) {
            onError("네이버 클라이언트 ID/Secret을 먼저 설정해 주세요.")
            return
        }

        NaverIdLoginSDK.initialize(activity, BuildConfig.NAVER_CLIENT_ID, BuildConfig.NAVER_CLIENT_SECRET, "LoveOn")
        NaverIdLoginSDK.authenticate(activity, object : OAuthLoginCallback {
            override fun onSuccess() {
                onSuccess("naver")
            }

            override fun onFailure(httpStatus: Int, message: String) {
                onError("네이버 로그인 실패: $message")
            }

            override fun onError(errorCode: Int, message: String) {
                onError("네이버 로그인 오류: $message")
            }
        })
    }
}
