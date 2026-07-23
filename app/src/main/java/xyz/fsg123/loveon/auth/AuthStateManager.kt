// auth/AuthStateManager.kt
package xyz.fsg123.loveon.auth

import android.content.SharedPreferences
import androidx.compose.runtime.mutableStateOf
import xyz.fsg123.loveon.data.MemberData

interface AuthStateStore {
    fun isLoggedIn(): Boolean
    fun setLoggedIn(provider: String)
    fun getProvider(): String?
    fun clearLogin()

    // 추가 메서드
    fun saveMember(member: MemberData)
    fun getMember(): MemberData?
    fun getMbLevel(): Int
    fun isBlacklisted(): Boolean
    fun hasWritePermission(): Boolean
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

    override fun saveMember(member: MemberData) {
        sharedPreferences.edit().apply {
            putString("mb_id", member.mb_id)
            putString("mb_nick", member.mb_nick)
            putString("mb_name", member.mb_name)
            putString("mb_email", member.mb_email)
            putInt("mb_level", member.mb_level)
            putString("mb_profile_img", member.mb_profile_img)
            putBoolean("is_blacklisted", member.is_blacklisted)
            apply()
        }
    }

    override fun getMember(): MemberData? {
        if (!isLoggedIn()) return null
        return MemberData(
            mb_id = sharedPreferences.getString("mb_id", "") ?: "",
            mb_nick = sharedPreferences.getString("mb_nick", "") ?: "",
            mb_name = sharedPreferences.getString("mb_name", "") ?: "",
            mb_email = sharedPreferences.getString("mb_email", "") ?: "",
            mb_level = sharedPreferences.getInt("mb_level", 2),
            mb_profile_img = sharedPreferences.getString("mb_profile_img", null),
            is_blacklisted = sharedPreferences.getBoolean("is_blacklisted", false)
        )
    }

    override fun getMbLevel(): Int = sharedPreferences.getInt("mb_level", 2)
    override fun isBlacklisted(): Boolean = sharedPreferences.getBoolean("is_blacklisted", false)
    override fun hasWritePermission(): Boolean = getMbLevel() >= 2

    companion object {
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
        private const val KEY_PROVIDER = "login_provider"
    }
}

class InMemoryAuthStateStore : AuthStateStore {
    private var isLoggedInValue = false
    private var providerValue: String? = null
    private var memberValue: MemberData? = null

    override fun isLoggedIn(): Boolean = isLoggedInValue
    override fun setLoggedIn(provider: String) {
        isLoggedInValue = true
        providerValue = provider
    }
    override fun getProvider(): String? = providerValue
    override fun clearLogin() {
        isLoggedInValue = false
        providerValue = null
        memberValue = null
    }
    override fun saveMember(member: MemberData) { memberValue = member }
    override fun getMember(): MemberData? = memberValue
    override fun getMbLevel(): Int = memberValue?.mb_level ?: 2
    override fun isBlacklisted(): Boolean = memberValue?.is_blacklisted ?: false
    override fun hasWritePermission(): Boolean = getMbLevel() >= 2
}

class AuthStateManager(
    private val store: AuthStateStore
) {
    private val _isLoggedIn = mutableStateOf(store.isLoggedIn())
    val isLoggedIn = _isLoggedIn

    private val _member = mutableStateOf(store.getMember())
    val member = _member

    private val _mbLevel = mutableStateOf(store.getMbLevel())
    val mbLevel = _mbLevel

    private val _isBlacklisted = mutableStateOf(store.isBlacklisted())
    val isBlacklisted = _isBlacklisted

    fun login(provider: String) {
        store.setLoggedIn(provider)
        _isLoggedIn.value = true
    }

    fun loginWithMember(member: MemberData) {
        store.saveMember(member)
        store.setLoggedIn(member.mb_id)
        _isLoggedIn.value = true
        _member.value = member
        _mbLevel.value = member.mb_level
        _isBlacklisted.value = member.is_blacklisted
    }

    fun logout() {
        store.clearLogin()
        _isLoggedIn.value = false
        _member.value = null
        _mbLevel.value = 2
        _isBlacklisted.value = false
    }

    fun getProvider(): String? = store.getProvider()
    fun getMember(): MemberData? = store.getMember()
    fun getMbLevel(): Int = store.getMbLevel()
    fun isBlacklisted(): Boolean = store.isBlacklisted()
    fun hasWritePermission(): Boolean = store.hasWritePermission()
}