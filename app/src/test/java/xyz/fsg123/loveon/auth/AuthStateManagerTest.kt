// test/java/xyz/fsg123/loveon/auth/AuthStateManagerTest.kt
package xyz.fsg123.loveon.auth

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import xyz.fsg123.loveon.data.MemberData

class AuthStateManagerTest {

    private lateinit var authStateManager: AuthStateManager
    private lateinit var inMemoryStore: InMemoryAuthStateStore

    @Before
    fun setUp() {
        inMemoryStore = InMemoryAuthStateStore()
        authStateManager = AuthStateManager(inMemoryStore)
    }

    @Test
    fun `initial state should be not logged in`() {
        // ✅ isLoggedIn은 프로퍼티이므로 .value로 접근
        assertFalse(authStateManager.isLoggedIn.value)
        assertNull(authStateManager.getProvider())
    }

    @Test
    fun `login should set logged in state`() {
        // Given
        val provider = "kakao"

        // When
        authStateManager.login(provider)

        // Then
        assertTrue(authStateManager.isLoggedIn.value)  // ✅ .value 사용
        assertEquals(provider, authStateManager.getProvider())
    }

    @Test
    fun `logout should clear logged in state`() {
        // Given
        authStateManager.login("google")

        // When
        authStateManager.logout()

        // Then
        assertFalse(authStateManager.isLoggedIn.value)  // ✅ .value 사용
        assertNull(authStateManager.getProvider())
    }

    @Test
    fun `loginWithMember should save member data`() {
        // Given
        val member = MemberData(
            mb_id = "test123",
            mb_nick = "테스트유저",
            mb_name = "테스트",
            mb_email = "test@example.com",
            mb_level = 2,
            is_blacklisted = false
        )

        // When
        authStateManager.loginWithMember(member)

        // Then
        assertTrue(authStateManager.isLoggedIn.value)  // ✅ .value 사용
        assertEquals(member, authStateManager.getMember())
        assertEquals(2, authStateManager.getMbLevel())
        assertFalse(authStateManager.isBlacklisted())
        assertTrue(authStateManager.hasWritePermission())
    }

    @Test
    fun `isBlacklisted should return true when member is blacklisted`() {
        // Given
        val member = MemberData(
            mb_id = "black123",
            mb_nick = "블랙리스트",
            mb_name = "블랙",
            mb_email = "black@example.com",
            mb_level = 1,
            is_blacklisted = true
        )

        // When
        authStateManager.loginWithMember(member)

        // Then
        assertTrue(authStateManager.isLoggedIn.value)  // ✅ .value 사용
        assertTrue(authStateManager.isBlacklisted())
        assertEquals(1, authStateManager.getMbLevel())
        assertFalse(authStateManager.hasWritePermission())
    }

    @Test
    fun `hasWritePermission should return true for level 2 member`() {
        // Given
        val member = MemberData(
            mb_id = "normal123",
            mb_nick = "일반회원",
            mb_name = "일반",
            mb_email = "normal@example.com",
            mb_level = 2,
            is_blacklisted = false
        )

        // When
        authStateManager.loginWithMember(member)

        // Then
        assertTrue(authStateManager.hasWritePermission())
    }

    @Test
    fun `hasWritePermission should return false for level 1 member`() {
        // Given
        val member = MemberData(
            mb_id = "black123",
            mb_nick = "블랙리스트",
            mb_name = "블랙",
            mb_email = "black@example.com",
            mb_level = 1,
            is_blacklisted = true
        )

        // When
        authStateManager.loginWithMember(member)

        // Then
        assertFalse(authStateManager.hasWritePermission())
    }
}