package com.example.model

enum class ReleaseChannel(
  val label: String,
  val badgeColor: Long,
  val isTestnet: Boolean = false,
  val isMainnet: Boolean = false,
  val isDual: Boolean = false
) {
  SOVEREIGN_TESTNET("Souverän Testnet (Autonom)", 0xFF10B981, isTestnet = true),
  SOVEREIGN_MAINNET("Souverän Mainnet (Autonom Real)", 0xFF059669, isMainnet = true),
  ETHEREUM_TESTNET("Ethereum Testnet (Sepolia)", 0xFF3B82F6, isTestnet = true),
  ETHEREUM_MAINNET("Ethereum Mainnet (Alchemy)", 0xFF6366F1, isMainnet = true),
  DUAL_HYBRID("Dual-Parität (Testnet & Mainnet)", 0xFFD97706, isDual = true);

  companion object {
    val PRODUCTION_MAINNET = ETHEREUM_MAINNET
  }
}

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
  val supportedChannels: List<ReleaseChannel> = listOf(releaseChannel),
  val isInstalled: Boolean = true,
  val isFlagship: Boolean = false,
  val autoUpdateEnabled: Boolean = true,
  val notarialCertificate: String = "URK-NR. 1892/2026 (§ 36 BeurkG)",
  val dualParityStatus: String = "VERIFIED · SOUVERÄNE DUAL-PARITÄT",
  val iconEmoji: String = "🛡️",
  val author: String = "RFOF-NETWORK"
) {
  fun matchesChannel(channel: ReleaseChannel?): Boolean {
    if (channel == null) return true
    return releaseChannel == channel || supportedChannels.contains(channel)
  }
}

enum class ExecutionMode(val label: String, val badge: String) {
  TEST_DEMO("Test (Demo)", "DEMO / TESTNET"),
  MAIN_REAL("Main (Real)", "PROD / MAINNET")
}
