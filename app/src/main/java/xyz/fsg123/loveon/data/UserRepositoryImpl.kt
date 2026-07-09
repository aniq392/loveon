package xyz.fsg123.loveon.data

import xyz.fsg123.loveon.domain.UserProfile
import xyz.fsg123.loveon.domain.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val loveOnService: LoveOnService
) : UserRepository {

    override fun getUserProfile(userId: String): Flow<UserProfile> = flow {
        val response = loveOnService.getUserProfile(userId)
        emit(response.toDomain())
    }
}