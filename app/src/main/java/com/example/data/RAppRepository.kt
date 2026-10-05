package com.example.data

import com.example.model.ReleaseChannel
import com.example.model.SovereignApp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object RAppRepository {

  private val initialApps = listOf(
    SovereignApp(
      id = "rapp-001",
      name = "PRAI / MTK / NEC",
      packageName = "com.aistudio.praimtknec.qvzkxp",
      versionName = "7.0",
      versionCode = 7,
      description = "Das souveräne Ökosystem: 28 Fachkategorien, 8-Sichten-Matrix, Multi-Chain Wallet & Treuhand-Register (§ 36 BeurkG).",
      category = "Sovereign Framework",
      repositoryUrl = "https://github.com/rfof-network/PRAI-MTK-NEC",
      releaseChannel = ReleaseChannel.DUAL_HYBRID,
      isInstalled = true,
      isFlagship = true,
      iconEmoji = "🏛️",
      author = "RFOF-NETWORK"
    ),
    SovereignApp(
      id = "rapp-002",
      name = "ZON Universal DEX & Launchpad",
      packageName = "com.rfof.zon.dex",
      versionName = "1.0-RC",
      versionCode = 1,
      description = "Autonome Bonding-Curve Börse, Token-Launchpad, Staking & Mining Pools für alle Community-Währungen.",
      category = "DeFi & DEX",
      repositoryUrl = "https://github.com/rfof-network/zon-dex-engine",
      releaseChannel = ReleaseChannel.SOVEREIGN_TESTNET,
      isInstalled = true,
      isFlagship = false,
      iconEmoji = "⚡",
      author = "PRAI Autonomous Agent"
    ),
    SovereignApp(
      id = "rapp-003",
      name = "XJustiz Notar & Grundbuch Archiv",
      packageName = "com.rfof.xjustiz.notariat",
      versionName = "2.4",
      versionCode = 24,
      description = "Vollautomatische Beurkundungsprüfung, XJustiz-XML Export und 100% notarielle Beweissicherung.",
      category = "Legal & Notariat",
      repositoryUrl = "https://github.com/rfof-network/xjustiz-archive",
      releaseChannel = ReleaseChannel.PRODUCTION_MAINNET,
      isInstalled = true,
      isFlagship = false,
      iconEmoji = "📜",
      author = "RFOF-NETWORK Notar-Parität"
    )
  )

  private val _apps = MutableStateFlow<List<SovereignApp>>(initialApps)
  val apps: StateFlow<List<SovereignApp>> = _apps.asStateFlow()

  fun deployAppFromRepository(
    name: String,
    packageName: String,
    category: String,
    repositoryUrl: String,
    versionName: String,
    channel: ReleaseChannel,
    description: String,
    iconEmoji: String
  ): Boolean {
    val newApp = SovereignApp(
      id = "rapp-${System.currentTimeMillis()}",
      name = name,
      packageName = if (packageName.startsWith("com.")) packageName else "com.rfof.$packageName",
      versionName = versionName.ifBlank { "1.0.0" },
      versionCode = 1,
      description = description.ifBlank { "Dezentral bereitgestellte rApp über RFOF-NETWORK Autonomie." },
      category = category,
      repositoryUrl = repositoryUrl,
      releaseChannel = channel,
      isInstalled = true,
      isFlagship = false,
      iconEmoji = iconEmoji.ifBlank { "📦" },
      author = "Community Sovereign Developer"
    )
    _apps.value = listOf(newApp) + _apps.value
    return true
  }

  fun toggleAutoUpdate(appId: String) {
    _apps.value = _apps.value.map {
      if (it.id == appId) it.copy(autoUpdateEnabled = !it.autoUpdateEnabled) else it
    }
  }

  fun checkForUpdates(appId: String): String {
    val app = _apps.value.find { it.id == appId } ?: return "App nicht gefunden"
    return "Souveräne Integrität für ${app.name} (v${app.versionName}) bestätigt! Keine Konflikte auf ${app.releaseChannel.label}."
  }
}
