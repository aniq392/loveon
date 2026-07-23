// feature/auth/login/LoginViewModel.kt
package xyz.fsg123.loveon.feature.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import xyz.fsg123.loveon.auth.AuthStateManager
import xyz.fsg123.loveon.data.MemberData
import xyz.fsg123.loveon.data.SocialLoginRequest
import xyz.fsg123.loveon.feature.auth.social.AuthRepository

class LoginViewModel(
    private val authRepository: AuthRepository,
    private val authStateManager: AuthStateManager
) : ViewModel() {

    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun socialLogin(request: SocialLoginRequest) {
        viewModelScope.launch {
            _uiState.value = LoginUiState.Loading

            val response = authRepository.socialLogin(request)

            if (response.result) {
                val member = response.data
                if (member != null) {
                    authStateManager.loginWithMember(member)
                    _uiState.value = LoginUiState.Success(
                        member = member,
                        isBlacklisted = member.is_blacklisted
                    )
                } else {
                    _uiState.value = LoginUiState.Error("회원 정보를 불러올 수 없습니다.")
                }
            } else {
                when (response.code) {
                    "UPDATE_REQUIRED" -> {
                        _uiState.value = LoginUiState.UpdateRequired(
                            minVersion = response.min_version ?: "",
                            updateUrl = response.update_url ?: "",
                            message = response.message
                        )
                    }
                    "INVALID_APP" -> {
                        _uiState.value = LoginUiState.Error("유효하지 않은 앱입니다.")
                    }
                    "VERSION_NOT_SUPPORTED" -> {
                        _uiState.value = LoginUiState.Error("현재 버전이 지원되지 않습니다.")
                    }
                    else -> {
                        _uiState.value = LoginUiState.Error(response.message)
                    }
                }
            }
        }
    }

    fun resetState() {
        _uiState.value = LoginUiState.Idle
    }
}

sealed class LoginUiState {
    object Idle : LoginUiState()
    object Loading : LoginUiState()
    data class Success(
        val member: MemberData,
        val isBlacklisted: Boolean = false
    ) : LoginUiState()
    data class Error(val message: String) : LoginUiState()
    data class UpdateRequired(
        val minVersion: String,
        val updateUrl: String,
        val message: String
    ) : LoginUiState()
}