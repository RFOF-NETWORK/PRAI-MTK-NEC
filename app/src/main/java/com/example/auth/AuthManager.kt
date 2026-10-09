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
    email = null, // Strictly private; NEVER exposed publicly
    authProvider = "RFOF-NETWORK (Sovereign OAuth)",
    walletAddress = "0xRFOF9842A7b2F366c8B01C5D19E77F32e2A8321",
    isEscrowAuthorized = true,
    isMtkHolderAllowed = true, // Admin exclusively holds MTK along with BTC, ETH, TON
    avatarUrl = "https://avatars.githubusercontent.com/u/rfof-network",
    bio = "Urheber, Erfinder & System-Architekt von PRAI / MTK / NEC. Alleinherrschaft über MTK Treasury & Notariats-Escrow.",
    organization = "© (Urheber & Erfinder)",
    sshPublicKey = "",
    gpgKeyId = "",
    personalAccessToken = "",
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
    email = null, // Strictly private
    authProvider = "OAuth 2.0 Identifier",
    walletAddress = "0x71C2B04E5F931aC2388C89284De15f458B43a890",
    isEscrowAuthorized = false,
    isMtkHolderAllowed = false,
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

  // Dual-Identity Creator Profile: Satoramy (Admin & User with linked password & phrases)
  val SATORAMY_PROFILE = UserProfile(
    username = "Satoramy",
    role = UserRole.ADMIN, // Dual Admin & User
    userType = UserType.ERFINDER,
    email = null, // Strictly private
    authProvider = "Satoramy Dual-Creator Auth (eGbR / Admin & User)",
    walletAddress = "0x89A3B04E5F931aC2388C89284De15f458B43a123",
    isEscrowAuthorized = true,
    isMtkHolderAllowed = true,
    avatarUrl = "",
    bio = "Satoramy – Dualer Creator-Account (Admin & Nutzer, geteiltes Passwort mit RFOF-NETWORK & Phrasen-Zugang).",
    organization = "eGbR (Eingetragene GbR)",
    unlockedCertificateIds = setOf("NEC-001", "NEC-002", "NEC-003", "NEC-004", "NEC-005")
  )

  // Password & Phrase Hashing (SHA-256 + Deterministic Salt)
  fun hashSecret(input: String): String {
    val md = java.security.MessageDigest.getInstance("SHA-256")
    val salt = "PRAI_MTK_NEC_SOVEREIGN_AUTH_SALT_2026_DETERMINISTIC"
    val bytes = md.digest((input + salt).toByteArray(Charsets.UTF_8))
    return bytes.joinToString("") { "%02x".format(it) }
  }

  // Master Secret Phrases Hash for Creator Accounts
  val MASTER_CREATOR_PHRASE_HASH = hashSecret("vault alpha omega genesis 2026 rfof sovereign guardian")

  fun getMasterCreatorPhraseIfAuthorized(user: UserProfile): String? {
    return if (user.role == UserRole.ADMIN || user.username == "Satoramy") {
      "vault alpha omega genesis 2026 rfof sovereign guardian"
    } else {
      null
    }
  }

  // Linked manual user password hash shared between Satoramy & RFOF-NETWORK
  private var _satoramyPasswordHash: String = hashSecret("SatoramyAdmin2026!")

  // Rate Limiting Protection (5 failed attempts -> 30s lockout)
  private var _failedAttemptsCount: Int = 0
  private var _lockoutUntilTimestamp: Long = 0L

  fun getRemainingLockoutSeconds(): Long {
    val remaining = _lockoutUntilTimestamp - System.currentTimeMillis()
    return if (remaining > 0) (remaining / 1000) + 1 else 0
  }

  // Custom registered user storage: username -> pair of (passwordHash, UserProfile)
  private val _registeredUsers = mutableMapOf<String, Pair<String, UserProfile>>(
    "rfof-network" to Pair(_satoramyPasswordHash, RFOF_ADMIN_PROFILE),
    "satoramy" to Pair(_satoramyPasswordHash, SATORAMY_PROFILE)
  )

  // Initial state is strictly GUEST_PROFILE - NO AUTO-LOGIN ON APP START
  private val _currentUser = MutableStateFlow(GUEST_PROFILE)
  val currentUser: StateFlow<UserProfile> = _currentUser.asStateFlow()

  // Deterministic Execution Mode (Test/Demo vs Main/Real)
  private val _authExecutionMode = MutableStateFlow(AuthExecutionMode.MAIN_REAL)
  val authExecutionMode: StateFlow<AuthExecutionMode> = _authExecutionMode.asStateFlow()

  // Parallel Provider Session States (Firebase, GitHub, Google, Microsoft, W3Connect)
  private val _parallelProviders = MutableStateFlow<Map<AuthProviderType, ParallelProviderState>>(
    mapOf(
      AuthProviderType.FIREBASE to ParallelProviderState(
        provider = AuthProviderType.FIREBASE,
        isEnabled = true,
        mode = AuthExecutionMode.MAIN_REAL,
        accountIdentifier = "gen-lang-client-0256777474 (europe-west2)",
        activeToken = EntropyDoubleProxyValidator.generateDeterministicToken(
          AuthProviderType.FIREBASE,
          "gen-lang-client-0256777474",
          AuthExecutionMode.MAIN_REAL
        ),
        proxy1ClientValidatorStatus = "VALIDATED_INGRESS",
        proxy2ServerlessValidatorStatus = "BOUND_SERVERLESS_FIRESTORE",
        lastValidatedTimestamp = System.currentTimeMillis()
      ),
      AuthProviderType.GITHUB to ParallelProviderState(
        provider = AuthProviderType.GITHUB,
        isEnabled = true,
        mode = AuthExecutionMode.MAIN_REAL,
        accountIdentifier = "RFOF-NETWORK",
        activeToken = EntropyDoubleProxyValidator.generateDeterministicToken(
          AuthProviderType.GITHUB,
          "RFOF-NETWORK",
          AuthExecutionMode.MAIN_REAL
        ),
        proxy1ClientValidatorStatus = "VALIDATED_INGRESS",
        proxy2ServerlessValidatorStatus = "BOUND_SERVERLESS_FIRESTORE",
        lastValidatedTimestamp = System.currentTimeMillis()
      ),
      AuthProviderType.GOOGLE to ParallelProviderState(
        provider = AuthProviderType.GOOGLE,
        isEnabled = false,
        mode = AuthExecutionMode.MAIN_REAL,
        accountIdentifier = "Google OAuth 2.0 Client (Privat)",
        activeToken = null,
        proxy1ClientValidatorStatus = "STANDBY",
        proxy2ServerlessValidatorStatus = "STANDBY"
      ),
      AuthProviderType.MICROSOFT to ParallelProviderState(
        provider = AuthProviderType.MICROSOFT,
        isEnabled = false,
        mode = AuthExecutionMode.MAIN_REAL,
        accountIdentifier = "Microsoft Azure AD Client (Privat)",
        activeToken = null,
        proxy1ClientValidatorStatus = "STANDBY",
        proxy2ServerlessValidatorStatus = "STANDBY"
      ),
      AuthProviderType.W3CONNECT to ParallelProviderState(
        provider = AuthProviderType.W3CONNECT,
        isEnabled = true,
        mode = AuthExecutionMode.MAIN_REAL,
        accountIdentifier = "0xRFOF9842A7b2F366c8B01C5D19E77F32e2A8321",
        activeToken = EntropyDoubleProxyValidator.generateDeterministicToken(
          AuthProviderType.W3CONNECT,
          "0xRFOF9842A7b2F366c8B01C5D19E77F32e2A8321",
          AuthExecutionMode.MAIN_REAL
        ),
        proxy1ClientValidatorStatus = "VALIDATED_INGRESS",
        proxy2ServerlessValidatorStatus = "BOUND_SERVERLESS_FIRESTORE",
        lastValidatedTimestamp = System.currentTimeMillis()
      )
    )
  )
  val parallelProviders: StateFlow<Map<AuthProviderType, ParallelProviderState>> = _parallelProviders.asStateFlow()

  val isAdmin: Boolean
    get() = _currentUser.value.role == UserRole.ADMIN && (_currentUser.value.username == "RFOF-NETWORK" || _currentUser.value.username == "Satoramy")

  fun isLockedOut(): Boolean {
    return System.currentTimeMillis() < _lockoutUntilTimestamp
  }

  fun setExecutionMode(mode: AuthExecutionMode) {
    _authExecutionMode.value = mode
    val currentMap = _parallelProviders.value.toMutableMap()
    currentMap.forEach { (type, state) ->
      if (state.isEnabled && state.accountIdentifier.isNotBlank()) {
        val newToken = EntropyDoubleProxyValidator.generateDeterministicToken(type, state.accountIdentifier, mode)
        currentMap[type] = state.copy(
          mode = mode,
          activeToken = newToken,
          lastValidatedTimestamp = System.currentTimeMillis()
        )
      } else {
        currentMap[type] = state.copy(mode = mode)
      }
    }
    _parallelProviders.value = currentMap
  }

  suspend fun authenticateProvider(
    provider: AuthProviderType,
    identifier: String,
    mode: AuthExecutionMode = _authExecutionMode.value
  ): ValidationResult {
    val result = EntropyDoubleProxyValidator.validateAndBindServerless(provider, identifier, mode)
    if (result.success && result.token != null) {
      val currentMap = _parallelProviders.value.toMutableMap()
      currentMap[provider] = ParallelProviderState(
        provider = provider,
        isEnabled = true,
        mode = mode,
        accountIdentifier = identifier,
        activeToken = result.token,
        proxy1ClientValidatorStatus = "VALIDATED_INGRESS",
        proxy2ServerlessValidatorStatus = if (result.token.egressServerlessBound) "BOUND_SERVERLESS_FIRESTORE" else "LOCAL_PARITY",
        lastValidatedTimestamp = System.currentTimeMillis()
      )
      _parallelProviders.value = currentMap
    }
    return result
  }

  fun disconnectProvider(provider: AuthProviderType) {
    val currentMap = _parallelProviders.value.toMutableMap()
    val existing = currentMap[provider] ?: return
    currentMap[provider] = existing.copy(
      isEnabled = false,
      activeToken = null,
      proxy1ClientValidatorStatus = "DISCONNECTED",
      proxy2ServerlessValidatorStatus = "DISCONNECTED",
      lastValidatedTimestamp = System.currentTimeMillis()
    )
    _parallelProviders.value = currentMap
  }

  private fun executeLoginAsRfofNetwork() {
    _currentUser.value = RFOF_ADMIN_PROFILE
    val currentMap = _parallelProviders.value.toMutableMap()
    currentMap[AuthProviderType.GITHUB] = ParallelProviderState(
      provider = AuthProviderType.GITHUB,
      isEnabled = true,
      mode = _authExecutionMode.value,
      accountIdentifier = "RFOF-NETWORK (Admin)",
      activeToken = EntropyDoubleProxyValidator.generateDeterministicToken(
        AuthProviderType.GITHUB,
        "RFOF-NETWORK",
        _authExecutionMode.value
      ),
      proxy1ClientValidatorStatus = "VALIDATED_INGRESS",
      proxy2ServerlessValidatorStatus = "BOUND_SERVERLESS_FIRESTORE",
      lastValidatedTimestamp = System.currentTimeMillis()
    )
    _parallelProviders.value = currentMap
  }

  fun linkFirebaseProvider(projectId: String = "gen-lang-client-0256777474") {
    val currentMap = _parallelProviders.value.toMutableMap()
    currentMap[AuthProviderType.FIREBASE] = ParallelProviderState(
      provider = AuthProviderType.FIREBASE,
      isEnabled = true,
      mode = _authExecutionMode.value,
      accountIdentifier = "Firebase Serverless (europe-west2)",
      activeToken = EntropyDoubleProxyValidator.generateDeterministicToken(
        AuthProviderType.FIREBASE,
        projectId,
        _authExecutionMode.value
      ),
      proxy1ClientValidatorStatus = "VALIDATED_INGRESS",
      proxy2ServerlessValidatorStatus = "BOUND_SERVERLESS_FIRESTORE",
      lastValidatedTimestamp = System.currentTimeMillis()
    )
    _parallelProviders.value = currentMap
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

  private fun executeLoginAsSatoramy() {
    _currentUser.value = SATORAMY_PROFILE
    val currentMap = _parallelProviders.value.toMutableMap()
    currentMap[AuthProviderType.GITHUB] = ParallelProviderState(
      provider = AuthProviderType.GITHUB,
      isEnabled = true,
      mode = _authExecutionMode.value,
      accountIdentifier = "Satoramy (Dual-Creator)",
      activeToken = EntropyDoubleProxyValidator.generateDeterministicToken(
        AuthProviderType.GITHUB,
        "Satoramy",
        _authExecutionMode.value
      ),
      proxy1ClientValidatorStatus = "VALIDATED_INGRESS",
      proxy2ServerlessValidatorStatus = "BOUND_SERVERLESS_FIRESTORE",
      lastValidatedTimestamp = System.currentTimeMillis()
    )
    _parallelProviders.value = currentMap
  }

  fun registerAccount(
    username: String,
    password: String,
    userType: UserType = UserType.ERFINDER,
    organization: String = "eGbR (Eingetragene GbR)"
  ): ValidationResult {
    val cleanUsername = username.trim()
    if (cleanUsername.isBlank()) {
      return ValidationResult(success = false, message = "Benutzername darf nicht leer sein.")
    }
    if (password.length < 4) {
      return ValidationResult(success = false, message = "Das Passwort muss mindestens 4 Zeichen lang sein.")
    }
    val key = cleanUsername.lowercase()
    val passwordHash = hashSecret(password)

    // Satoramy special dual-account creation logic
    if (key == "satoramy" || key == "sartoramy") {
      _satoramyPasswordHash = passwordHash
      _registeredUsers["satoramy"] = Pair(passwordHash, SATORAMY_PROFILE)
      _registeredUsers["rfof-network"] = Pair(passwordHash, RFOF_ADMIN_PROFILE)
      executeLoginAsSatoramy()
      _failedAttemptsCount = 0
      return ValidationResult(
        success = true,
        message = "Creator-Account 'Satoramy' erfolgreich erstellt! Dual-Admin Status aktiv. Passwort wurde mit RFOF-NETWORK synchronisiert."
      )
    }

    if (key == "rfof-network") {
      return ValidationResult(success = false, message = "Der Benutzername 'RFOF-NETWORK' ist als Master-Admin reserviert.")
    }

    if (_registeredUsers.containsKey(key)) {
      return ValidationResult(success = false, message = "Benutzername '$cleanUsername' ist bereits vergeben!")
    }

    val newProfile = UserProfile(
      username = cleanUsername,
      role = UserRole.USER,
      userType = userType,
      email = null, // Strictly private; NEVER exposed publicly
      authProvider = "Eigenes System (Passwort / Entropie)",
      walletAddress = "0x" + cleanUsername.hashCode().toUInt().toString(16).padStart(40, 'a').take(42),
      isEscrowAuthorized = false,
      isMtkHolderAllowed = false,
      organization = organization,
      unlockedCertificateIds = setOf("NEC-001")
    )

    _registeredUsers[key] = Pair(passwordHash, newProfile)
    _currentUser.value = newProfile
    _failedAttemptsCount = 0
    return ValidationResult(
      success = true,
      message = "Account '$cleanUsername' erfolgreich erstellt und angemeldet!"
    )
  }

  fun loginWithCredentials(username: String, passwordOrPhrase: String): ValidationResult {
    if (getRemainingLockoutSeconds() > 0) {
      return ValidationResult(
        success = false,
        message = "Sicherheits-Lockout aktiv: Zu viele Fehlversuche. Bitte warte ${getRemainingLockoutSeconds()} Sekunden."
      )
    }

    val cleanUsername = username.trim()
    val cleanSecret = passwordOrPhrase.trim()

    if (cleanSecret.isBlank()) {
      return ValidationResult(success = false, message = "Passwortprüfung ist Pflicht. Auto-Login ohne Passwort ist untersagt.")
    }

    val hashedSecret = hashSecret(cleanSecret)
    val key = cleanUsername.lowercase()

    // 1. Check Master Creator Phrase Hash
    if (hashedSecret == MASTER_CREATOR_PHRASE_HASH) {
      _failedAttemptsCount = 0
      if (key == "rfof-network" || cleanUsername.isBlank()) {
        executeLoginAsRfofNetwork()
        return ValidationResult(success = true, message = "Master-Admin Autorisierung über geheime Phrasen erfolgreich!")
      } else if (key == "satoramy" || key == "sartoramy") {
        executeLoginAsSatoramy()
        return ValidationResult(success = true, message = "Satoramy Creator-Autorisierung über geheime Phrasen erfolgreich!")
      }
    }

    // 2. Check Satoramy or RFOF-NETWORK using linked password hash
    if (key == "satoramy" || key == "sartoramy") {
      if (hashedSecret == _satoramyPasswordHash) {
        _failedAttemptsCount = 0
        executeLoginAsSatoramy()
        return ValidationResult(success = true, message = "Erfolgreich als Satoramy angemeldet (Dual-Admin & Nutzer)!")
      } else {
        _failedAttemptsCount++
        if (_failedAttemptsCount >= 5) {
          _lockoutUntilTimestamp = System.currentTimeMillis() + 30_000L
        }
        return ValidationResult(success = false, message = "Ungültiges Passwort für Satoramy.")
      }
    }

    if (key == "rfof-network") {
      if (hashedSecret == _satoramyPasswordHash) {
        _failedAttemptsCount = 0
        executeLoginAsRfofNetwork()
        return ValidationResult(success = true, message = "Erfolgreich als RFOF-NETWORK angemeldet!")
      } else {
        _failedAttemptsCount++
        if (_failedAttemptsCount >= 5) {
          _lockoutUntilTimestamp = System.currentTimeMillis() + 30_000L
        }
        return ValidationResult(success = false, message = "Ungültiges Passwort oder Phrasen für RFOF-NETWORK.")
      }
    }

    // 3. Registered accounts
    val registered = _registeredUsers[key]
    if (registered != null) {
      if (registered.first == hashedSecret) {
        _failedAttemptsCount = 0
        _currentUser.value = registered.second
        return ValidationResult(success = true, message = "Willkommen zurück, ${registered.second.username}!")
      } else {
        _failedAttemptsCount++
        if (_failedAttemptsCount >= 5) {
          _lockoutUntilTimestamp = System.currentTimeMillis() + 30_000L
        }
        return ValidationResult(success = false, message = "Falsches Passwort für $cleanUsername.")
      }
    }

    _failedAttemptsCount++
    if (_failedAttemptsCount >= 5) {
      _lockoutUntilTimestamp = System.currentTimeMillis() + 30_000L
    }
    return ValidationResult(success = false, message = "Benutzer '$cleanUsername' nicht gefunden oder Passwort falsch.")
  }

  fun logout() {
    _currentUser.value = GUEST_PROFILE
    _failedAttemptsCount = 0
    _lockoutUntilTimestamp = 0L
  }

  fun unlockCertificateForCurrentUser(certId: String) {
    val current = _currentUser.value
    _currentUser.value = current.copy(
      unlockedCertificateIds = current.unlockedCertificateIds + certId
    )
  }
}
