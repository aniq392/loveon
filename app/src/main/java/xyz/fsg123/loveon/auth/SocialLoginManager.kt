package xyz.fsg123.loveon.auth

import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.OAuthLoginCallback
import xyz.fsg123.loveon.BuildConfig

class SocialLoginManager(
    private val activity: ComponentActivity,
    private val googleLoginLauncher: ActivityResultLauncher<Intent>,
    private val onSuccess: (String) -> Unit,
    private val onError: (String) -> Unit,
) {
    private var googleSignInClient: GoogleSignInClient? = null
    private val KAKAO_TAG = "KakaoLogin"
    private val NAVER_TAG = "NAVER"

    init {
        initializeGoogleClient()
        initializeKakaoSdk()
    }

    private fun initializeGoogleClient() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(BuildConfig.GOOGLE_WEB_CLIENT_ID)
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
        Log.d("GOOGLE", "handleGoogleResult 호출")

        if (result.resultCode != Activity.RESULT_OK) {
            Log.d("GOOGLE", "취소됨")
            onError("취소")
            return
        }

        try {
            Log.d("GOOGLE", "RESULT OK")
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            val account = task.getResult(ApiException::class.java)

            Log.d("GOOGLE", "Email = ${account.email}")
            Log.d("GOOGLE", "ID Token = ${account.idToken}")

            onSuccess("google")
        } catch (e: ApiException) {
            Log.e("GOOGLE", "code=${e.statusCode}", e)
            onError(e.message ?: "실패")
        }
    } // 👈 닫는 괄호가 누락되었던 부분 수정

    fun startKakaoLogin() {
        if (BuildConfig.KAKAO_NATIVE_APP_KEY.isBlank() || BuildConfig.KAKAO_NATIVE_APP_KEY.contains("YOUR")) {
            onError("카카오 앱 키를 먼저 설정해 주세요.")
            return
        }

        val webLoginCallback: (OAuthToken?, Throwable?) -> Unit = fun(token, error) {
            if (error != null) {
                if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                    return
                }
                onError(error.message ?: "카카오 로그인 실패")
            } else if (token != null) {
                fetchKakaoUserInfo()
                onSuccess("kakao")
            }
        }

        if (UserApiClient.instance.isKakaoTalkLoginAvailable(activity)) {
            UserApiClient.instance.loginWithKakaoTalk(activity) { token, error ->
                if (error != null) {
                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                        return@loginWithKakaoTalk
                    }
                    UserApiClient.instance.loginWithKakaoAccount(activity, callback = webLoginCallback)
                } else if (token != null) {
                    fetchKakaoUserInfo()
                    onSuccess("kakao")
                }
            }
        } else {
            UserApiClient.instance.loginWithKakaoAccount(activity, callback = webLoginCallback)
        }
    }

    private fun fetchKakaoUserInfo() {
        UserApiClient.instance.me { user, error ->
            if (error != null) {
                Log.e(KAKAO_TAG, "사용자 정보 요청 실패", error)
            } else if (user != null) {
                Log.i(
                    KAKAO_TAG,
                    "사용자 정보 요청 성공" +
                            "\n회원번호: ${user.id}" +
                            "\n이메일: ${user.kakaoAccount?.email}" +
                            "\n닉네임: ${user.kakaoAccount?.profile?.nickname}" +
                            "\n프로필사진: ${user.kakaoAccount?.profile?.thumbnailImageUrl}"
                )
            }
        }
    }

    fun startNaverLogin() {
        Log.d(NAVER_TAG, "Naver Login Start")

        if (BuildConfig.NAVER_CLIENT_ID.isBlank() || BuildConfig.NAVER_CLIENT_SECRET.isBlank()) {
            Log.e(NAVER_TAG, "Client ID 또는 Secret 없음")
            onError("네이버 설정이 없습니다.")
            return
        }

        Log.d(NAVER_TAG, "SDK Initialize")
        NaverIdLoginSDK.initialize(
            activity,
            BuildConfig.NAVER_CLIENT_ID,
            BuildConfig.NAVER_CLIENT_SECRET,
            "LoveOn"
        )

        Log.d(NAVER_TAG, "Authenticate")
        NaverIdLoginSDK.authenticate(activity, object : OAuthLoginCallback {
            override fun onSuccess() {

                val accessToken = NaverIdLoginSDK.getAccessToken()
                val refreshToken = NaverIdLoginSDK.getRefreshToken()
                val expiresAt = NaverIdLoginSDK.getExpiresAt()
                val tokenType = NaverIdLoginSDK.getTokenType()

                Log.d(NAVER_TAG, "===== NAVER LOGIN SUCCESS =====")
                Log.d(NAVER_TAG, "AccessToken : $accessToken")
                Log.d(NAVER_TAG, "RefreshToken: $refreshToken")
                Log.d(NAVER_TAG, "TokenType   : $tokenType")
                Log.d(NAVER_TAG, "ExpiresAt   : $expiresAt")
                Log.d(NAVER_TAG, "================================")

                onSuccess("naver")
            }

            override fun onFailure(httpStatus: Int, message: String) {
                Log.e(NAVER_TAG, "Failure : $httpStatus / $message")
                onError(message)
            }

            override fun onError(errorCode: Int, message: String) {
                Log.e(NAVER_TAG, "Error : $errorCode / $message")
                onError(message)
            }
        })
    }
}