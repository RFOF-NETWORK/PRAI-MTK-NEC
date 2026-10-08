package com.example.auth

import androidx.compose.ui.graphics.Color
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

enum class AuthProviderType(
  val displayName: String,
  val protocol: String,
  val iconEmoji: String,
  val brandColorHex: Long
) {
  FIREBASE("Firebase", "Serverless Cloud Auth", "🔥", 0xFFFF9100),
  GITHUB("GitHub", "OAuth 2.0 / Git Token", "🐙", 0xFF24292F),
  GOOGLE("Google", "Identity / Credential Manager", "🌐", 0xFF4285F4),
  MICROSOFT("Microsoft", "Azure AD / Live OAuth", "💻", 0xFF00A4EF),
  W3CONNECT("W3Connect", "Web3 EIP-4361 / EVM", "⚡", 0xFF627EEA);

  val brandColor: Color
    get() = Color(brandColorHex)
}

enum class AuthExecutionMode(val label: String, val badge: String, val description: String) {
  TEST_DEMO(
    "Test (Demo)",
    "DEMO / TESTNET",
    "Nur Testnet / Sepolia / Sandbox Entropy. Reale Keys gesperrt."
  ),
  MAIN_REAL(
    "Main (Real)",
    "PROD / MAINNET",
    "Nur Produktion / Mainnet / Cryptographic Real Keys. Demo gesperrt."
  )
}

data class DeterministicProxyToken(
  val tokenId: String,
  val provider: AuthProviderType,
  val mode: AuthExecutionMode,
  val tokenValue: String,
  val entropyProof: String,
  val ingressValidated: Boolean,
  val egressServerlessBound: Boolean,
  val createdAt: Long = System.currentTimeMillis()
)

data class ParallelProviderState(
  val provider: AuthProviderType,
  val isEnabled: Boolean = false,
  val mode: AuthExecutionMode = AuthExecutionMode.MAIN_REAL,
  val accountIdentifier: String = "",
  val activeToken: DeterministicProxyToken? = null,
  val entropyBits: Int = 256,
  val proxy1ClientValidatorStatus: String = "STANDBY",
  val proxy2ServerlessValidatorStatus: String = "STANDBY",
  val lastValidatedTimestamp: Long = 0L
)
