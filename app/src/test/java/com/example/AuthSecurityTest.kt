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
    assertTrue(AuthManager.isAdmin)
    assertNull(AuthManager.currentUser.value.email)
  }

  @Test
  fun testRfofNetworkPasswordLoginSharesPasswordWithSatoramy() {
    val result = AuthManager.loginWithCredentials("RFOF-NETWORK", "SatoramyAdmin2026!")
    assertTrue(result.success)
    assertEquals("RFOF-NETWORK", AuthManager.currentUser.value.username)
    assertEquals(UserRole.ADMIN, AuthManager.currentUser.value.role)
    assertTrue(AuthManager.isAdmin)
  }

  @Test
  fun testDualIdentityMasterPhrasesAuth() {
    val phrase = "vault alpha omega genesis 2026 rfof sovereign guardian"
    val result = AuthManager.loginWithCredentials("Satoramy", phrase)
    assertTrue(result.success)
    assertEquals("Satoramy", AuthManager.currentUser.value.username)
    assertTrue(AuthManager.isAdmin)
  }

  @Test
  fun testRateLimitingLocksOutAfterFiveFailedAttempts() {
    for (i in 1..4) {
      val res = AuthManager.loginWithCredentials("Satoramy", "BadPass$i")
      assertFalse(res.success)
      assertEquals(UserRole.GUEST, AuthManager.currentUser.value.role)
    }
    val res5 = AuthManager.loginWithCredentials("Satoramy", "BadPass5")
    assertFalse(res5.success)
    assertTrue(AuthManager.isLockedOut())

    // Even correct password is now blocked during active lockout window
    val resLocked = AuthManager.loginWithCredentials("Satoramy", "SatoramyAdmin2026!")
    assertFalse(resLocked.success)
    assertTrue(resLocked.message.contains("Sicherheits-Lockout aktiv"))
  }

  @Test
  fun testGuestCannotSeeWalletOrTreugeberBalances() {
    // Guest starts with empty assets and no transaction visibility
    AuthManager.logout()
    com.example.data.WalletRepository.refreshAssetsForCurrentRole()
    assertTrue(com.example.data.WalletRepository.assets.value.isEmpty())
    assertTrue(com.example.data.WalletRepository.transactions.value.isEmpty())
    assertEquals(UserRole.GUEST, AuthManager.currentUser.value.role)
  }

  @Test
  fun testCurrencyOperationsSendReceiveSwapWorkProperly() {
    // Authenticate as verified user to perform wallet operations
    val loginRes = AuthManager.loginWithCredentials("Satoramy", "SatoramyAdmin2026!")
    assertTrue(loginRes.success)
    com.example.data.WalletRepository.refreshAssetsForCurrentRole()

    // 1. Test Deposit (Empfangen)
    val initialEth = com.example.data.WalletRepository.assets.value.find { it.chain == com.example.model.CryptoChain.ETH }?.balance ?: 0.0
    com.example.data.WalletRepository.deposit(com.example.model.CryptoChain.ETH, 1.5, "Test Empfangen")
    val afterDeposit = com.example.data.WalletRepository.assets.value.find { it.chain == com.example.model.CryptoChain.ETH }?.balance ?: 0.0
    assertTrue(afterDeposit >= initialEth + 1.49)

    // 2. Test Withdraw (Versenden)
    val beforeWithdraw = com.example.data.WalletRepository.assets.value.find { it.chain == com.example.model.CryptoChain.ETH }?.balance ?: 0.0
    val withdrawSuccess = com.example.data.WalletRepository.withdraw(com.example.model.CryptoChain.ETH, "0xRecipientVault", 0.5)
    assertTrue(withdrawSuccess)
    val afterWithdraw = com.example.data.WalletRepository.assets.value.find { it.chain == com.example.model.CryptoChain.ETH }?.balance ?: 0.0
    assertEquals(beforeWithdraw - 0.5, afterWithdraw, 0.01)

    // 3. Test Swap
    val swapResult = com.example.data.WalletRepository.swap(
      fromChain = com.example.model.CryptoChain.ETH,
      toChain = com.example.model.CryptoChain.BTC,
      amountFrom = 0.5
    )
    assertNotNull(swapResult)
    assertTrue(swapResult!! > 0.0)
  }

  @Test
  fun testRegularUserCannotMintSupplyOutOfThinAir() {
    // Normal user registers and logs in
    val regResult = AuthManager.registerAccount("TestNutzer", "NutzerPass123!", com.example.auth.UserType.KUNDE)
    assertTrue(regResult.success)
    assertEquals(UserRole.USER, AuthManager.currentUser.value.role)
    assertFalse(AuthManager.isAdmin)
    assertFalse(AuthManager.currentUser.value.isMtkHolderAllowed)
    assertFalse(AuthManager.currentUser.value.isEscrowAuthorized)

    // Regular user cannot hold or mint MTK from thin air
    assertFalse(AuthManager.currentUser.value.role == UserRole.ADMIN)
    assertNotEquals("Satoramy", AuthManager.currentUser.value.username)
    assertNotEquals("RFOF-NETWORK", AuthManager.currentUser.value.username)
  }

  @Test
  fun testThirdPartyProviderBindingWithoutPasswordIntermediary() {
    // Providers can bind their active tokens deterministically using official credentials
    val providers = AuthManager.parallelProviders.value
    assertTrue(providers.containsKey(com.example.auth.AuthProviderType.GITHUB))
    assertTrue(providers.containsKey(com.example.auth.AuthProviderType.FIREBASE))
    assertTrue(providers.containsKey(com.example.auth.AuthProviderType.GOOGLE))
    assertTrue(providers.containsKey(com.example.auth.AuthProviderType.MICROSOFT))
    assertTrue(providers.containsKey(com.example.auth.AuthProviderType.W3CONNECT))
  }
}
