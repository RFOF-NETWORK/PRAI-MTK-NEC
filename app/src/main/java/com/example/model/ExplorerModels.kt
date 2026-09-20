package com.example.model

enum class TransferPolicy(val label: String, val badgeDesc: String) {
  COPY_ONLY_TRANSFERRABLE("Nur beglaubigte Kopie transferierbar", "Original verbleibt verankert; Nachweis-Kopie wird übertragen"),
  ORIGINAL_TRANSFERRABLE("Original-Urkunde transferierbar", "Vollständige Übertragung des Eigentumsrechts"),
  SOULBOUND_IMMUTABLE("Unveräußerlich (Soulbound)", "Dauerhaft an die Identität gebunden (§ 36 BeurkG)")
}

data class NecCertificate(
  val id: String,
  val title: String,
  val categoryId: Int,
  val categoryName: String,
  val nftTokenId: String,
  val databaseRecordId: String,
  val isNftAndCertSingularity: Boolean = true, // Coexists in Blockchain & Database
  val isCurrency: Boolean = false, // Strictly false: NEC is a Certificate/NFT, NOT a currency
  val transferPolicy: TransferPolicy,
  val unlockRequirement: String,
  val isUnlockedByDefault: Boolean,
  val notarialSealHash: String,
  val description: String,
  val extensionLicenseHash: String = SovereignLicenseData.LICENSE_HASH,
  val issueDate: String = "2026-09-01",
  val issuer: String = "RFOF-NETWORK (Erfinder-Stammvater & Protocol Admin)"
)

data class BlockchainBlock(
  val height: Long,
  val hash: String,
  val chain: CryptoChain,
  val timestamp: Long,
  val validatorOrMiner: String,
  val txCount: Int,
  val sizeKb: Double,
  val reward: String,
  val extensionLicenseHash: String = SovereignLicenseData.LICENSE_HASH
)
