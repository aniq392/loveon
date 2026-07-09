package xyz.fsg123.loveon.data

import retrofit2.http.GET
import retrofit2.http.Path

interface LoveOnService {
    @GET("users/{userId}")
    suspend fun getUserProfile(@Path("userId") userId: String): UserResponse
}