package com.example.auth

import com.example.crypto.AESEncryption
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class UserRole(val displayName: String, val badge: String) {
  ADMIN("Admin (RFOF-NETWORK)", "ADMIN"),
  USER("Standard-Nutzer", "USER"),
  GUEST("Gast", "GUEST")
}

enum class UserType(val label: String, val description: String) {
  ADMIN("Sovereign Admin", "Vollständige System-Souveränität, MTK-Treasury & Escrow Master"),
  ERFINDER("Erfinder (Inventor)", "Code-Entwicklung, Repo-Publishing & NEC-Urkunden-Einreichung"),
  PARTNER("Partner (Co-Operator)", "Organisations-Mitverwaltung (GbR/eGbR/Stiftung) & Escrow-Prüfung"),
  KUNDE("Kunde / Abnehmer (Client)", "Nutzung dezentraler Services, BTC/ETH/TON & Beglaubigte Kopien");

  companion object {
    fun fromRole(role: UserRole): UserType {
      return when (role) {
        UserRole.ADMIN -> ADMIN
        UserRole.USER -> ERFINDER
        UserRole.GUEST -> KUNDE
      }
    }
  }
}

data class UserProfile(
  val username: String,
  val role: UserRole,
  val userType: UserType = UserType.fromRole(role),
  val email: String? = null,
  val authProvider: String, // "RFOF-NETWORK", "Google", "Web3", "Guest"
  val walletAddress: String,
  val isEscrowAuthorized: Boolean,
  val isMtkHolderAllowed: Boolean,
  val avatarUrl: String = "",
  val bio: String = "PRAI / MTK / NEC Ökosystem-Teilnehmer",
  val organization: String = "© (Urheber & Erfinder)",
  val sshPublicKey: String = "ssh-ed25519 AAAAC3NzaC1lZDI1NTE5AAAAIPRAIMTKNEC... sovereign-key",
  val gpgKeyId: String = "0x89AB10F2C8423984",
  val personalAccessToken: String = "rfof_pat_7a8b9c0d1e2f3a4b5c6d7e8f",
  val editorTheme: String = "VS Code Modern Dark",
  val unlockedCertificateIds: Set<String> = emptySet()
)

object AuthManager {
  // RFOF-NETWORK is the SOLE Admin and creator of PRAI / MTK / NEC
  val RFOF_ADMIN_PROFILE = UserProfile(
    username = "RFOF-NETWORK",
    role = UserRole.ADMIN,
    userType = UserType.ADMIN,
    email = "admin@rfof-network.org",
    authProvider = "RFOF-NETWORK (Sovereign OAuth)",
    walletAddress = "0xRFOF9842A7b2F366c8B01C5D19E77F32e2A8321",
    isEscrowAuthorized = true,
    isMtkHolderAllowed = true, // Admin exclusively holds MTK along with BTC, ETH, TON
    avatarUrl = "https://avatars.githubusercontent.com/u/rfof-network",
    bio = "Urheber, Erfinder & System-Architekt von PRAI / MTK / NEC. Alleinherrschaft über MTK Treasury & Notariats-Escrow.",
    organization = "© (Urheber & Erfinder)",
    sshPublicKey = "ssh-ed25519 AAAAC3NzaC1lZDI1NTE5AAAAI_RFOF_MASTER_SOVEREIGN_KEY_2026",
    gpgKeyId = "0xRFOF2026_MASTER_GPG",
    personalAccessToken = "rfof_pat_master_99887766554433221100",
    editorTheme = "VS Code High-Contrast Navy",
    unlockedCertificateIds = setOf(
      "NEC-001", "NEC-002", "NEC-003", "NEC-004",
      "NEC-005", "NEC-006", "NEC-007", "NEC-008"
    )
  )

  val DEFAULT_USER_PROFILE = UserProfile(
    username = "Erfinder-Entwickler",
    role = UserRole.USER,
    userType = UserType.ERFINDER,
    email = "developer@rfof-network.org",
    authProvider = "Google Account (OAuth 2.0)",
    walletAddress = "0x71C2B04E5F931aC2388C89284De15f458B43a890",
    isEscrowAuthorized = false,
    isMtkHolderAllowed = false, // Users hold BTC, ETH, TON, but NOT raw MTK
    avatarUrl = "",
    bio = "Entwickler & Erfinder im dezentralen Wirtschaftsnetzwerk.",
    organization = "eGbR (Eingetragene GbR)",
    unlockedCertificateIds = setOf("NEC-001", "NEC-003", "NEC-005")
  )

  val GUEST_PROFILE = UserProfile(
    username = "Gast-Besucher",
    role = UserRole.GUEST,
    userType = UserType.KUNDE,
    email = null,
    authProvider = "Gast",
    walletAddress = "0x0000000000000000000000000000000000000000",
    isEscrowAuthorized = false,
    isMtkHolderAllowed = false,
    organization = "Kunde (Einzelperson)",
    unlockedCertificateIds = setOf("NEC-001")
  )

  private val _currentUser = MutableStateFlow(RFOF_ADMIN_PROFILE)
  val currentUser: StateFlow<UserProfile> = _currentUser.asStateFlow()

  val isAdmin: Boolean
    get() = _currentUser.value.role == UserRole.ADMIN && _currentUser.value.username == "RFOF-NETWORK"

  fun loginAsRfofNetwork() {
    _currentUser.value = RFOF_ADMIN_PROFILE
  }

  fun loginWithGoogle(email: String = "rfof236286@gmail.com", name: String = "Google Nutzer") {
    _currentUser.value = UserProfile(
      username = if (name.contains("@")) name.substringBefore("@") else name,
      role = UserRole.USER, // All others are strictly USER
      userType = UserType.ERFINDER,
      email = email,
      authProvider = "Google Account (OAuth 2.0)",
      walletAddress = "0x3Fa2919E5D491EAcC182479B2912DDE34892E1C9",
      isEscrowAuthorized = false,
      isMtkHolderAllowed = false,
      organization = "eGbR (Eingetragene GbR)",
      unlockedCertificateIds = setOf("NEC-001", "NEC-003")
    )
  }

  fun loginWithWeb3(address: String, networkName: String = "Ethereum", userType: UserType = UserType.PARTNER) {
    _currentUser.value = UserProfile(
      username = "W3-" + address.take(6) + "..." + address.takeLast(4),
      role = UserRole.USER, // All others are strictly USER
      userType = userType,
      email = null,
      authProvider = "W3Connect ($networkName)",
      walletAddress = address,
      isEscrowAuthorized = false,
      isMtkHolderAllowed = false,
      organization = "GbR (BGB-Gesellschaft)",
      unlockedCertificateIds = setOf("NEC-001", "NEC-003", "NEC-005")
    )
  }

  fun updateProfileSettings(
    newBio: String,
    newOrganization: String,
    newSshKey: String,
    newGpgKey: String,
    newEditorTheme: String,
    userType: UserType
  ) {
    val current = _currentUser.value
    _currentUser.value = current.copy(
      bio = newBio,
      organization = newOrganization,
      sshPublicKey = newSshKey,
      gpgKeyId = newGpgKey,
      editorTheme = newEditorTheme,
      userType = if (current.role == UserRole.ADMIN) UserType.ADMIN else userType
    )
  }

  fun logout() {
    _currentUser.value = GUEST_PROFILE
  }

  fun unlockCertificateForCurrentUser(certId: String) {
    val current = _currentUser.value
    _currentUser.value = current.copy(
      unlockedCertificateIds = current.unlockedCertificateIds + certId
    )
  }
}
