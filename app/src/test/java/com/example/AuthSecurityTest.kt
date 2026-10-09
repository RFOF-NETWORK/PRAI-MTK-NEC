package com.example

import com.example.auth.AuthManager
import com.example.auth.UserRole
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class AuthSecurityTest {

  @Before
  fun setUp() {
    AuthManager.logout()
  }

  @Test
  fun testNoAutoLoginWithoutPassword() {
    val resultEmpty = AuthManager.loginWithCredentials("RFOF-NETWORK", "")
    assertFalse(resultEmpty.success)
    assertEquals(UserRole.GUEST, AuthManager.currentUser.value.role)

    val resultBlank = AuthManager.loginWithCredentials("Satoramy", "   ")
    assertFalse(resultBlank.success)
    assertEquals(UserRole.GUEST, AuthManager.currentUser.value.role)
  }

  @Test
  fun testFailedPasswordRejectsAndDoesNotCreateSession() {
    val resultWrong = AuthManager.loginWithCredentials("Satoramy", "WrongPassword123")
    assertFalse(resultWrong.success)
    assertEquals(UserRole.GUEST, AuthManager.currentUser.value.role)
  }

  @Test
  fun testPublicProfilesDoNotExposeEmail() {
    assertNull(AuthManager.currentUser.value.email)
    assertNull(AuthManager.RFOF_ADMIN_PROFILE.email)
    assertNull(AuthManager.SATORAMY_PROFILE.email)
    assertNull(AuthManager.GUEST_PROFILE.email)
    assertNull(AuthManager.DEFAULT_USER_PROFILE.email)
  }

  @Test
  fun testSuccessfulLoginWithValidPasswordOnly() {
    val result = AuthManager.loginWithCredentials("Satoramy", "SatoramyAdmin2026!")
    assertTrue(result.success)
    assertEquals("Satoramy", AuthManager.currentUser.value.username)
    assertEquals(UserRole.ADMIN, AuthManager.currentUser.value.role)
    assertNull(AuthManager.currentUser.value.email)
  }
}
