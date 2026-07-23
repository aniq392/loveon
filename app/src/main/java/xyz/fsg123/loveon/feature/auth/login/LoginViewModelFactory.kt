

// feature/auth/login/LoginViewModelFactory.kt
package xyz.fsg123.loveon.feature.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import xyz.fsg123.loveon.auth.AuthStateManager
import xyz.fsg123.loveon.feature.auth.social.AuthRepository
class LoginViewModelFactory(
    private val authStateManager: AuthStateManager
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(
                authRepository = AuthRepository(),
                authStateManager = authStateManager
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}