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

  // Dual-Identity Creator Profile: Satoramy (Admin & User with linked password & phrases)
  val SATORAMY_PROFILE = UserProfile(
    username = "Satoramy",
    role = UserRole.ADMIN, // Dual Admin & User
    userType = UserType.ERFINDER,
    email = "satoramy@rfof-network.org",
    authProvider = "Satoramy Dual-Creator Auth (eGbR / Admin & User)",
    walletAddress = "0x89A3B04E5F931aC2388C89284De15f458B43a123",
    isEscrowAuthorized = true,
    isMtkHolderAllowed = true,
    avatarUrl = "",
    bio = "Satoramy – Dualer Creator-Account (Admin & Nutzer, geteiltes Passwort mit RFOF-NETWORK & Phrasen-Zugang).",
    organization = "eGbR (Eingetragene GbR)",
    unlockedCertificateIds = setOf("NEC-001", "NEC-002", "NEC-003", "NEC-004", "NEC-005")
  )

  // Master Secret Phrases for Creator Accounts
  const val MASTER_CREATOR_PHRASE = "vault alpha omega genesis 2026 rfof sovereign guardian"

  // Linked manual user password shared between Satoramy & RFOF-NETWORK
  private var _satoramyManualPassword: String = "SatoramyAdmin2026!"
  val satoramyManualPassword: String get() = _satoramyManualPassword

  // Custom registered user storage: username -> pair of (password, UserProfile)
  private val _registeredUsers = mutableMapOf<String, Pair<String, UserProfile>>(
    "rfof-network" to Pair(_satoramyManualPassword, RFOF_ADMIN_PROFILE),
    "satoramy" to Pair(_satoramyManualPassword, SATORAMY_PROFILE)
  )

  private val _currentUser = MutableStateFlow(RFOF_ADMIN_PROFILE)
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
        accountIdentifier = "rfof236286@gmail.com",
        activeToken = null,
        proxy1ClientValidatorStatus = "STANDBY",
        proxy2ServerlessValidatorStatus = "STANDBY"
      ),
      AuthProviderType.MICROSOFT to ParallelProviderState(
        provider = AuthProviderType.MICROSOFT,
        isEnabled = false,
        mode = AuthExecutionMode.MAIN_REAL,
        accountIdentifier = "rfof-network@azure.com",
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
    get() = _currentUser.value.role == UserRole.ADMIN && _currentUser.value.username == "RFOF-NETWORK"

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

  fun loginAsRfofNetwork() {
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

  fun loginWithFirebase(projectId: String = "gen-lang-client-0256777474") {
    val currentMap = _parallelProviders.value.toMutableMap()
    currentMap[AuthProviderType.FIREBASE] = ParallelProviderState(
      provider = AuthProviderType.FIREBASE,
      isEnabled = true,
      mode = _authExecutionMode.value,
      accountIdentifier = "$projectId (Serverless)",
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

  fun loginWithMicrosoft(email: String = "rfof.network@azure.microsoft.com", displayName: String = "Microsoft Erfinder") {
    _currentUser.value = UserProfile(
      username = if (displayName.contains("@")) displayName.substringBefore("@") else displayName,
      role = UserRole.USER,
      userType = UserType.ERFINDER,
      email = email,
      authProvider = "Microsoft Azure AD (OAuth 2.0)",
      walletAddress = "0x51E281F26aD7B547C9028711AA039401732BC0E1",
      isEscrowAuthorized = false,
      isMtkHolderAllowed = false,
      organization = "eGbR (Eingetragene GbR)",
      unlockedCertificateIds = setOf("NEC-001", "NEC-002")
    )
    val currentMap = _parallelProviders.value.toMutableMap()
    currentMap[AuthProviderType.MICROSOFT] = ParallelProviderState(
      provider = AuthProviderType.MICROSOFT,
      isEnabled = true,
      mode = _authExecutionMode.value,
      accountIdentifier = email,
      activeToken = EntropyDoubleProxyValidator.generateDeterministicToken(
        AuthProviderType.MICROSOFT,
        email,
        _authExecutionMode.value
      ),
      proxy1ClientValidatorStatus = "VALIDATED_INGRESS",
      proxy2ServerlessValidatorStatus = "BOUND_SERVERLESS_FIRESTORE",
      lastValidatedTimestamp = System.currentTimeMillis()
    )
    _parallelProviders.value = currentMap
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
    val currentMap = _parallelProviders.value.toMutableMap()
    currentMap[AuthProviderType.GOOGLE] = ParallelProviderState(
      provider = AuthProviderType.GOOGLE,
      isEnabled = true,
      mode = _authExecutionMode.value,
      accountIdentifier = email,
      activeToken = EntropyDoubleProxyValidator.generateDeterministicToken(
        AuthProviderType.GOOGLE,
        email,
        _authExecutionMode.value
      ),
      proxy1ClientValidatorStatus = "VALIDATED_INGRESS",
      proxy2ServerlessValidatorStatus = "BOUND_SERVERLESS_FIRESTORE",
      lastValidatedTimestamp = System.currentTimeMillis()
    )
    _parallelProviders.value = currentMap
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
    val currentMap = _parallelProviders.value.toMutableMap()
    currentMap[AuthProviderType.W3CONNECT] = ParallelProviderState(
      provider = AuthProviderType.W3CONNECT,
      isEnabled = true,
      mode = _authExecutionMode.value,
      accountIdentifier = address,
      activeToken = EntropyDoubleProxyValidator.generateDeterministicToken(
        AuthProviderType.W3CONNECT,
        address,
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

  fun loginAsSatoramy() {
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

    // Satoramy special dual-account creation logic
    if (key == "satoramy" || key == "sartoramy") {
      _satoramyManualPassword = password
      _registeredUsers["satoramy"] = Pair(password, SATORAMY_PROFILE)
      _registeredUsers["rfof-network"] = Pair(password, RFOF_ADMIN_PROFILE)
      loginAsSatoramy()
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
      email = "$key@rfof-network.org",
      authProvider = "Eigenes System (Passwort / Entropie)",
      walletAddress = "0x" + cleanUsername.hashCode().toUInt().toString(16).padStart(40, 'a').take(42),
      isEscrowAuthorized = false,
      isMtkHolderAllowed = false,
      organization = organization,
      unlockedCertificateIds = setOf("NEC-001")
    )

    _registeredUsers[key] = Pair(password, newProfile)
    _currentUser.value = newProfile
    return ValidationResult(
      success = true,
      message = "Account '$cleanUsername' erfolgreich erstellt und angemeldet!"
    )
  }

  fun loginWithCredentials(username: String, passwordOrPhrase: String): ValidationResult {
    val cleanUsername = username.trim()
    val cleanSecret = passwordOrPhrase.trim()
    val key = cleanUsername.lowercase()

    // 1. Check Master Creator Phrase
    if (cleanSecret.equals(MASTER_CREATOR_PHRASE, ignoreCase = true)) {
      if (key == "rfof-network" || cleanUsername.isBlank()) {
        loginAsRfofNetwork()
        return ValidationResult(success = true, message = "Master-Admin Autorisierung über geheime Phrasen erfolgreich!")
      } else if (key == "satoramy" || key == "sartoramy") {
        loginAsSatoramy()
        return ValidationResult(success = true, message = "Satoramy Creator-Autorisierung über geheime Phrasen erfolgreich!")
      }
    }

    // 2. Check Satoramy or RFOF-NETWORK using linked password
    if (key == "satoramy" || key == "sartoramy") {
      if (cleanSecret == _satoramyManualPassword || cleanSecret == "SatoramyAdmin2026!" || cleanSecret == "rfof2026") {
        loginAsSatoramy()
        return ValidationResult(success = true, message = "Erfolgreich als Satoramy angemeldet (Dual-Admin & Nutzer)!")
      } else {
        return ValidationResult(success = false, message = "Ungültiges Passwort für Satoramy.")
      }
    }

    if (key == "rfof-network") {
      if (cleanSecret == _satoramyManualPassword || cleanSecret == "rfof2026" || cleanSecret == "SatoramyAdmin2026!") {
        loginAsRfofNetwork()
        return ValidationResult(success = true, message = "Erfolgreich als RFOF-NETWORK angemeldet!")
      } else {
        return ValidationResult(success = false, message = "Ungültiges Passwort oder Phrasen für RFOF-NETWORK.")
      }
    }

    // 3. Registered accounts
    val registered = _registeredUsers[key]
    if (registered != null) {
      if (registered.first == cleanSecret) {
        _currentUser.value = registered.second
        return ValidationResult(success = true, message = "Willkommen zurück, ${registered.second.username}!")
      } else {
        return ValidationResult(success = false, message = "Falsches Passwort für $cleanUsername.")
      }
    }

    return ValidationResult(success = false, message = "Benutzer '$cleanUsername' nicht gefunden. Bitte erstelle einen Account!")
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
