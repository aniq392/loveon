package xyz.fsg123.loveon.auth

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class AuthStateManagerTest {
    @Test
    fun `login state is saved and cleared`() {
        val store = InMemoryAuthStateStore()
        val manager = AuthStateManager(store)

        assertFalse(manager.isLoggedIn())

        manager.login("kakao")

        assertTrue(manager.isLoggedIn())
        assertEquals("kakao", manager.getProvider())

        manager.logout()

        assertFalse(manager.isLoggedIn())
        assertNull(manager.getProvider())
    }
}
