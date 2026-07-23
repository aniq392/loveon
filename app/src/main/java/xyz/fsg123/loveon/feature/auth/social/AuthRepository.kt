package xyz.fsg123.loveon.feature.auth.social


import xyz.fsg123.loveon.data.ApiResponse
import xyz.fsg123.loveon.data.MemberData
import xyz.fsg123.loveon.data.SocialLoginRequest
import xyz.fsg123.loveon.core.network.RetrofitClient

class AuthRepository {
    private val apiService = RetrofitClient.apiService

    suspend fun socialLogin(request: SocialLoginRequest): ApiResponse<MemberData> {
        return try {
            apiService.socialLogin(request)
        } catch (e: Exception) {
            ApiResponse(
                result = false,
                message = "네트워크 오류가 발생했습니다: ${e.message}",
                data = null,
                code = "NETWORK_ERROR"
            )
        }
    }
}