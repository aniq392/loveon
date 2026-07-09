package xyz.fsg123.loveon.auth

import android.content.SharedPreferences

interface AuthStateStore {
    fun isLoggedIn(): Boolean
    fun setLoggedIn(provider: String)
    fun getProvider(): String?
    fun clearLogin()
}

class SharedPreferencesAuthStateStore(
    private val sharedPreferences: SharedPreferences
) : AuthStateStore {
    override fun isLoggedIn(): Boolean = sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false)

    override fun setLoggedIn(provider: String) {
        sharedPreferences.edit()
            .putBoolean(KEY_IS_LOGGED_IN, true)
            .putString(KEY_PROVIDER, provider)
            .apply()
    }

    override fun getProvider(): String? = sharedPreferences.getString(KEY_PROVIDER, null)

    override fun clearLogin() {
        sharedPreferences.edit()
            .remove(KEY_PROVIDER)
            .putBoolean(KEY_IS_LOGGED_IN, false)
            .apply()
    }

    companion object {
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
        private const val KEY_PROVIDER = "login_provider"
    }
}

class InMemoryAuthStateStore : AuthStateStore {
    private var isLoggedInValue = false
    private var providerValue: String? = null

    override fun isLoggedIn(): Boolean = isLoggedInValue

    override fun setLoggedIn(provider: String) {
        isLoggedInValue = true
        providerValue = provider
    }

    override fun getProvider(): String? = providerValue

    override fun clearLogin() {
        isLoggedInValue = false
        providerValue = null
    }
}

class AuthStateManager(
    private val store: AuthStateStore
) {
    fun login(provider: String) = store.setLoggedIn(provider)

    fun logout() = store.clearLogin()

    fun isLoggedIn(): Boolean = store.isLoggedIn()

    fun getProvider(): String? = store.getProvider()
}
