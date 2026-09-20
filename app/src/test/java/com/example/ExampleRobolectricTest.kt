package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.CategoriesRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("PRAI / MTK / NEC", appName)
  }

  @Test
  fun `verify all 28 categories exist with complete perspectives and urkunden`() {
    val report = CategoriesRepository.validateHealth()
    assertEquals(28, report.totalCategories)
    assertTrue("All categories must have 8 perspectives", report.hasAll8Perspectives)
    assertTrue("All categories must have Urkundenmuster", report.hasAllUrkunden)
    assertTrue("All categories must have Organigramm", report.hasAllOrganigramme)
    assertTrue("All categories must have Bauplan", report.hasAllBauplaene)
    assertEquals("GREEN", report.status)
  }

  @Test
  fun `verify sovereign license and copyright parameters`() {
    assertEquals("RFOF-NETWORK", com.example.model.SovereignLicenseData.GITHUB_ACCOUNT)
    assertEquals("PRAI / MTK / NEC App", com.example.model.SovereignLicenseData.REPO_NAME)
    assertTrue(com.example.model.SovereignLicenseData.LICENSE_HASH.isNotBlank())
    assertTrue(com.example.model.SovereignLicenseData.FULL_LICENSE_TEXT.contains("SINGLE-CURRENCY", ignoreCase = true))
    assertTrue(com.example.model.SovereignLicenseData.FULL_LICENSE_TEXT.contains("XJustiz"))
  }

  @Test
  fun `verify admin role exclusivity and user role segregation`() {
    com.example.auth.AuthManager.loginAsRfofNetwork()
    assertTrue(com.example.auth.AuthManager.isAdmin)
    assertEquals(com.example.auth.UserRole.ADMIN, com.example.auth.AuthManager.currentUser.value.role)
    assertTrue(com.example.auth.AuthManager.currentUser.value.isMtkHolderAllowed)

    // Regular sign-in must strictly produce USER role
    com.example.auth.AuthManager.loginWithGoogle("partner@network.org", "Partner Dev")
    org.junit.Assert.assertFalse(com.example.auth.AuthManager.isAdmin)
    assertEquals(com.example.auth.UserRole.USER, com.example.auth.AuthManager.currentUser.value.role)
    org.junit.Assert.assertFalse(com.example.auth.AuthManager.currentUser.value.isMtkHolderAllowed)
  }

  @Test
  fun `verify repository publishing with organizations and nec certificate`() {
    val repo = com.example.data.RepositoryManager.publishRepository(
      name = "my-test-module",
      organization = "eGbR (Eingetragene GbR)",
      description = "Test module implementation",
      licenseType = com.example.model.SovereignLicenseData.LICENSE_NAME,
      isPrivate = false,
      linkedNecCertId = "NEC-001"
    )
    assertEquals("my-test-module", repo.name)
    assertEquals("NEC-001", repo.linkedNecCertId)
    assertTrue(repo.pagesUrl.contains("my-test-module"))
    assertTrue(repo.latestCommitHash.isNotBlank())
  }
}
