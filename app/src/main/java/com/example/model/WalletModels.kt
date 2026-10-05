package com.example.model

import androidx.compose.ui.graphics.Color

enum class CryptoChain(
  val symbol: String,
  val fullName: String,
  val isSovereign: Boolean = false,
  val supportsMining: Boolean = false,
  val supportsStaking: Boolean = false,
  val primaryColorHex: Long = 0xFF2563EB
) {
  BTC("BTC", "Bitcoin Network", false, supportsMining = true, supportsStaking = false, primaryColorHex = 0xFFF7931A),
  ETH("ETH", "Ethereum (EVM / W3C)", false, supportsMining = false, supportsStaking = true, primaryColorHex = 0xFF627EEA),
  TON("TON", "The Open Network", false, supportsMining = false, supportsStaking = true, primaryColorHex = 0xFF0098EA),
  MTK("MTK", "Montalkanio Sovereign Chain", true, supportsMining = false, supportsStaking = true, primaryColorHex = 0xFFB45309),
  ZON("ZON", "ZON Universal Currency (ETH/TON Bridged)", true, supportsMining = true, supportsStaking = true, primaryColorHex = 0xFF10B981),
  CUSTOM("OWN", "Eigene Währung (Custom Token)", false, supportsMining = true, supportsStaking = true, primaryColorHex = 0xFF8B5CF6);

  val color: Color
    get() = Color(primaryColorHex)
}

data class LaunchedToken(
  val id: String,
  val name: String,
  val symbol: String,
  val creatorAddress: String,
  val totalSupply: Double,
  val circulatingSupply: Double,
  val priceEur: Double,
  val liquidityEur: Double,
  val isMiningSupported: Boolean,
  val isStakingSupported: Boolean,
  val iconEmoji: String = "⚡",
  val creationDate: String,
  val extensionOf: String = "ZON"
)

data class CommunityStakingPool(
  val poolId: String,
  val tokenSymbol: String,
  val tokenName: String,
  val totalStaked: Double,
  val apyPercent: Double,
  val minStake: Double,
  val lockPeriodDays: Int,
  val participantsCount: Int,
  val liquidityBackedEur: Double
)

data class CommunityMiningPool(
  val poolId: String,
  val tokenSymbol: String,
  val algorithm: String = "SHA-256 / JK-Automaton",
  val hashrateGh: Double,
  val activeMiners: Int,
  val blockReward: Double,
  val difficulty: Double,
  val liquidityBackedEur: Double
)

data class NftCertificate(
  val tokenId: String,
  val title: String,
  val urkundenNummer: String,
  val notarialSealDate: String,
  val ownerAddress: String,
  val priceZon: Double,
  val ipfsCid: String,
  val categoryId: Int,
  val rechtsform: String,
  val isVerifiedByNotar: Boolean = true
)

data class CryptoAsset(
  val chain: CryptoChain,
  val balance: Double,
  val usdRate: Double,
  val address: String,
  val change24h: Double,
  val isRestrictedToAdmin: Boolean = false
) {
  val usdValue: Double
    get() = balance * usdRate
}

enum class EscrowStatus(val label: String) {
  LOCKED_TREUHAND("Treuhand-Sperre Aktiv"),
  RESERVE_BACKED("100% Notariell Gedeckt"),
  RELEASE_PENDING("Freigabe Ausstehend"),
  PARTIALLY_RELEASED("Teilweise Freigegeben")
}

data class EscrowContractState(
  val contractId: String = "ESCROW-PRAI-MTK-001",
  val notarialActNumber: String = "URK-NR. 1892/2026",
  val treuhaender: String = "RFOF-NETWORK (Admin & Erfinder-Stammvater)",
  val totalLockedMtk: Double = 10_000_000.0,
  val backingBtc: Double = 250.0,
  val backingEth: Double = 3500.0,
  val backingTon: Double = 150_000.0,
  val status: EscrowStatus = EscrowStatus.LOCKED_TREUHAND,
  val legalBase: String = "§§ 675, 667 BGB i.V.m. § 36 BeurkG",
  val lastAuditTimestamp: Long = System.currentTimeMillis()
)

data class StakingState(
  val chain: CryptoChain,
  val stakedAmount: Double,
  val aprPercent: Double,
  val rewardsAccumulated: Double,
  val validatorPool: String
)

data class MiningState(
  val chain: CryptoChain = CryptoChain.BTC,
  val isActive: Boolean = true,
  val hashrateTh: Double = 142.5,
  val workersCount: Int = 4,
  val dailyRewardBtc: Double = 0.00045,
  val poolName: String = "PRAI Autarkie Mining Pool #1"
)

enum class TxType(val label: String) {
  DEPOSIT("Einzahlung (Deposit)"),
  WITHDRAW("Auszahlung (Withdraw)"),
  SWAP("Krypto-Tausch (Swap)"),
  STAKE("Staking Allokation"),
  ESCROW_LOCK("Treuhand-Hinterlegung"),
  CERTIFICATE_MINT("NEC-Zertifikat Prägung")
}

enum class TxStatus {
  CONFIRMED,
  PENDING,
  FAILED
}

data class WalletTransaction(
  val id: String,
  val txHash: String,
  val chain: CryptoChain,
  val type: TxType,
  val amount: Double,
  val fromAddress: String,
  val toAddress: String,
  val timestamp: Long,
  val status: TxStatus = TxStatus.CONFIRMED,
  val fee: Double = 0.0002,
  val note: String = ""
)
