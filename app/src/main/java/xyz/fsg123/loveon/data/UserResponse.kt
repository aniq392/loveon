package xyz.fsg123.loveon.data

import xyz.fsg123.loveon.domain.UserProfile

data class UserResponse(
    val userId: String,
    val userNickname: String,
    val profileImage: String
) {
    fun toDomain() = UserProfile(
        id = userId,
        nickname = userNickname,
        imageUrl = profileImage
    )
}