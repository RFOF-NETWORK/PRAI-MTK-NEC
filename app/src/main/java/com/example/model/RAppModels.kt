package com.example.model

data class SovereignApp(
  val id: String,
  val name: String,
  val packageName: String,
  val versionName: String,
  val versionCode: Int,
  val description: String,
  val category: String,
  val repositoryUrl: String,
  val releaseChannel: ReleaseChannel,
  val isInstalled: Boolean = true,
  val isFlagship: Boolean = false,
  val autoUpdateEnabled: Boolean = true,
  val notarialCertificate: String = "URK-NR. 1892/2026 (§ 36 BeurkG)",
  val dualParityStatus: String = "VERIFIED · SOUVERÄNE DUAL-PARITÄT",
  val iconEmoji: String = "🛡️",
  val author: String = "RFOF-NETWORK"
)

enum class ReleaseChannel(val label: String, val badgeColor: Long) {
  SOVEREIGN_TESTNET("Sovereign Testnet (Autonom)", 0xFF10B981),
  PRODUCTION_MAINNET("Ethereum Mainnet (Alchemy)", 0xFF6366F1),
  DUAL_HYBRID("Dual-Parität (Testnet & Mainnet)", 0xFFD97706)
}
