
// data/SocialLoginRequest.kt
package xyz.fsg123.loveon.data

import xyz.fsg123.loveon.BuildConfig

// aniq 2026 중요
data class SocialLoginRequest(
    val app_id: String = BuildConfig.APPLICATION_ID,
    val app_version: String = BuildConfig.VERSION_NAME,
    val social_type: String,    // "email", "kakao", "naver", "google"
    val social_id: String,
    val email: String,
    val nickname: String,
    val profile_img: String? = null,
    val device_token: String? = null
)

//data class SocialLoginRequest(
//    val app_id: String = "com.myapp.a",  // ✅ 테스트용으로 임시 변경
//    val app_version: String = BuildConfig.VERSION_NAME,
//    val social_type: String,
//    val social_id: String,
//    val email: String,
//    val nickname: String,
//    val profile_img: String? = null,
//    val device_token: String? = null
//)