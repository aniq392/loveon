

// network/ApiService.kt
package xyz.fsg123.loveon.core.network
import retrofit2.http.Body
import retrofit2.http.POST
import xyz.fsg123.loveon.data.ApiResponse
import xyz.fsg123.loveon.data.MemberData
import xyz.fsg123.loveon.data.SocialLoginRequest

interface ApiService {
    @POST("api/social_login.php")
    suspend fun socialLogin(
        @Body request: SocialLoginRequest
    ): ApiResponse<MemberData>
}