package xyz.fsg123.loveon.domain

import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getUserProfile(userId: String): Flow<UserProfile>
}