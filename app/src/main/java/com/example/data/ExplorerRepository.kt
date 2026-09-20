package com.example.data

import com.example.auth.AuthManager
import com.example.crypto.AESEncryption
import com.example.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

object ExplorerRepository {
  private val _certificates = MutableStateFlow<List<NecCertificate>>(emptyList())
  val certificates: StateFlow<List<NecCertificate>> = _certificates.asStateFlow()

  private val _recentBlocks = MutableStateFlow<List<BlockchainBlock>>(emptyList())
  val recentBlocks: StateFlow<List<BlockchainBlock>> = _recentBlocks.asStateFlow()

  private val _transferredCopies = MutableStateFlow<List<CopyTransferRecord>>(emptyList())
  val transferredCopies: StateFlow<List<CopyTransferRecord>> = _transferredCopies.asStateFlow()

  data class CopyTransferRecord(
    val certId: String,
    val recipientEmailOrAddress: String,
    val timestamp: Long,
    val sealVerificationHash: String,
    val isCopyOnly: Boolean
  )

  init {
    initCertificates()
    initBlocks()
  }

  private fun initCertificates() {
    _certificates.value = listOf(
      NecCertificate(
        id = "NEC-001",
        title = "Urheber-Stammurkunde & Primäres Erfindungs-Zertifikat",
        categoryId = 1,
        categoryName = "Urheber-, Erfinder- & Titelschutz",
        nftTokenId = "0xMTK_NFT_001_STAMM_URKUNDE",
        databaseRecordId = "XJUSTIZ-DOC-2026-CAT01-001",
        isNftAndCertSingularity = true,
        isCurrency = false,
        transferPolicy = TransferPolicy.COPY_ONLY_TRANSFERRABLE,
        unlockRequirement = "Erlangt: Primärer System-Start & RFOF-NETWORK Autorisation",
        isUnlockedByDefault = true,
        notarialSealHash = "0x89ab10f2c842398402948209384029384092834092834092834092834092834a",
        description = "Beglaubigt die unanfechtbare Priorität der 28 Kategorien, 4 Layer und 8 Perspektiven nach § 36 BeurkG. Das Original verbleibt unverrückbar beim Erfinder; zur Vorlage bei Behörden wird stets eine verifizierte beglaubigte Abschrift generiert."
      ),
      NecCertificate(
        id = "NEC-002",
        title = "Notarielle Tatsachenfeststellung & Escrow-Deckungsnachweis",
        categoryId = 2,
        categoryName = "Notariat, Justiz & Vertragsregister",
        nftTokenId = "0xMTK_NFT_002_ESCROW_BACKING",
        databaseRecordId = "XJUSTIZ-DOC-2026-CAT02-004",
        isNftAndCertSingularity = true,
        isCurrency = false,
        transferPolicy = TransferPolicy.COPY_ONLY_TRANSFERRABLE,
        unlockRequirement = "Erlangt bei Freigabe des Treuhand-Vertrags (URK-NR. 1892/2026)",
        isUnlockedByDefault = true,
        notarialSealHash = "0x4a92c8172039842019482039482039482039482039482039482039482039482b",
        description = "Bestätigt die notarielle Hinterlegung und 100% Deckung der MTK-Rücklagen im Treuhand-Tresor. Dient als Nachweis für Aufsichtsbehörden."
      ),
      NecCertificate(
        id = "NEC-003",
        title = "Permakultur-Autarkie & Rohstoff-Schutzbrief",
        categoryId = 5,
        categoryName = "Agrarwirtschaft, Permakultur & Ernährung",
        nftTokenId = "0xMTK_NFT_003_PERMA_AUTARKY",
        databaseRecordId = "XJUSTIZ-DOC-2026-CAT05-012",
        isNftAndCertSingularity = true,
        isCurrency = false,
        transferPolicy = TransferPolicy.ORIGINAL_TRANSFERRABLE,
        unlockRequirement = "Erlangt bei Erreichen von Level 2 in Arena Wohnzentren",
        isUnlockedByDefault = false,
        notarialSealHash = "0x7b12c8901928304918230948120394812039481203948120394812039481203c",
        description = "Sichert die biologische Saatgut-Autarkie und Rohstoff-Eigenständigkeit innerhalb der Wohnzentren (Kreis 1 & Kreis 2)."
      ),
      NecCertificate(
        id = "NEC-004",
        title = "Post-Quanten-Kryptographie & Algorithmen-Zertifikat",
        categoryId = 13,
        categoryName = "Informatik, KI, Kybernetik & Krypto",
        nftTokenId = "0xMTK_NFT_004_QUANTUM_SHIELD",
        databaseRecordId = "XJUSTIZ-DOC-2026-CAT13-099",
        isNftAndCertSingularity = true,
        isCurrency = false,
        transferPolicy = TransferPolicy.SOULBOUND_IMMUTABLE,
        unlockRequirement = "Erlangt: Kybernetik-Audit & AES-256 Validierung",
        isUnlockedByDefault = false,
        notarialSealHash = "0x1f9284729104820194820194820194820194820194820194820194820194820d",
        description = "Unveräußerlicher kryptographischer Nachweis der mathematischen Absicherung gegen Quantencomputer-Entschlüsselungsangriffe."
      ),
      NecCertificate(
        id = "NEC-005",
        title = "Wohnzentrum-Autarkie & Allokations-Urkunde",
        categoryId = 28,
        categoryName = "Wohnzentren, Bau, Geodäsie & Raumordnung",
        nftTokenId = "0xMTK_NFT_005_WOHNZENTRUM_TITEL",
        databaseRecordId = "XJUSTIZ-DOC-2026-CAT28-028",
        isNftAndCertSingularity = true,
        isCurrency = false,
        transferPolicy = TransferPolicy.ORIGINAL_TRANSFERRABLE,
        unlockRequirement = "Erlangt bei Allokation eines Parzellen-Wohnrechts",
        isUnlockedByDefault = false,
        notarialSealHash = "0x6e9102948201948201948201948201948201948201948201948201948201948e",
        description = "Grundbuch-äquivalente Berechtigung zur Nutzung der autarken Infrastruktur (Energie, Wasser, Nahrung) im Verbund der Wohnzentren."
      ),
      NecCertificate(
        id = "NEC-006",
        title = "Treuhand-Bürgschaft & Reserve-Depot-Urkunde",
        categoryId = 8,
        categoryName = "Finanzmarkt, Bankwesen & Krypto-Assets",
        nftTokenId = "0xMTK_NFT_006_TREUHAND_DEPOT",
        databaseRecordId = "XJUSTIZ-DOC-2026-CAT08-015",
        isNftAndCertSingularity = true,
        isCurrency = false,
        transferPolicy = TransferPolicy.COPY_ONLY_TRANSFERRABLE,
        unlockRequirement = "Erlangt: Escrow-Treuhand Prüfsiegel durch Admin",
        isUnlockedByDefault = false,
        notarialSealHash = "0x3d8204918204918204918204918204918204918204918204918204918204918f",
        description = "Amtliche Urkunde zur Bestätigung der Mehrwährungs-Deckung (BTC, ETH, TON) im Treuhand-Speicher."
      )
    )
  }

  private fun initBlocks() {
    val now = System.currentTimeMillis()
    _recentBlocks.value = listOf(
      BlockchainBlock(
        height = 4_102_918L,
        hash = "0xMTK_984f1a209b7c8e14d2a56c0b91e84a2f8c149e21",
        chain = CryptoChain.MTK,
        timestamp = now - 12_000,
        validatorOrMiner = "Notariat-Node #1 (RFOF-NETWORK Core)",
        txCount = 42,
        sizeKb = 124.8,
        reward = "Sovereign PoS (0% Inflation)"
      ),
      BlockchainBlock(
        height = 840_195L,
        hash = "0000000000000000000194b8e219084c7a1590483c1824792c81",
        chain = CryptoChain.BTC,
        timestamp = now - 360_000,
        validatorOrMiner = "PRAI Autarkie Mining Pool #1",
        txCount = 2841,
        sizeKb = 1420.5,
        reward = "3.125 BTC"
      ),
      BlockchainBlock(
        height = 20_891_452L,
        hash = "0xETH_c814b7e9124089a4218b7c4192084c7a529e81b6",
        chain = CryptoChain.ETH,
        timestamp = now - 24_000,
        validatorOrMiner = "Lido / PRAI Validator Cluster #3",
        txCount = 186,
        sizeKb = 210.2,
        reward = "0.084 ETH (PoS)"
      ),
      BlockchainBlock(
        height = 41_902_384L,
        hash = "EQD_f192847c190823b471a9823b1092847c190823b4",
        chain = CryptoChain.TON,
        timestamp = now - 8_000,
        validatorOrMiner = "TonWhales / RFOF Master Validator",
        txCount = 512,
        sizeKb = 88.4,
        reward = "1.8 TON"
      )
    )
  }

  fun isCertificateUnlocked(cert: NecCertificate): Boolean {
    // Admin has all unlocked
    if (AuthManager.isAdmin) return true
    val user = AuthManager.currentUser.value
    return cert.isUnlockedByDefault || user.unlockedCertificateIds.contains(cert.id)
  }

  fun transferDocumentCopy(certId: String, recipient: String): String {
    val cert = _certificates.value.find { it.id == certId } ?: return "FEHLER: Zertifikat nicht gefunden"
    val sealHash = AESEncryption.signDocumentSeal(cert.id, cert.notarialSealHash, recipient)

    val record = CopyTransferRecord(
      certId = certId,
      recipientEmailOrAddress = recipient,
      timestamp = System.currentTimeMillis(),
      sealVerificationHash = sealHash,
      isCopyOnly = true
    )
    _transferredCopies.value = listOf(record) + _transferredCopies.value
    return sealHash
  }

  fun transferDocumentOriginal(certId: String, recipient: String): Boolean {
    val cert = _certificates.value.find { it.id == certId } ?: return false
    if (cert.transferPolicy != TransferPolicy.ORIGINAL_TRANSFERRABLE) return false

    val record = CopyTransferRecord(
      certId = certId,
      recipientEmailOrAddress = recipient,
      timestamp = System.currentTimeMillis(),
      sealVerificationHash = "0xORIGINAL_TRANSFER_${System.currentTimeMillis()}",
      isCopyOnly = false
    )
    _transferredCopies.value = listOf(record) + _transferredCopies.value
    return true
  }

  fun unlockCertificate(certId: String) {
    AuthManager.unlockCertificateForCurrentUser(certId)
  }

  fun initializeDatabase(context: android.content.Context) {
    val db = com.example.db.AppDatabase.getDatabase(context)
    kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.IO).launch {
      val entities = _certificates.value.map { cert ->
        com.example.db.NecCertificateEntity(
          id = cert.id,
          title = cert.title,
          categoryId = cert.categoryId,
          categoryName = cert.categoryName,
          nftTokenId = cert.nftTokenId,
          databaseRecordId = cert.databaseRecordId,
          transferPolicy = cert.transferPolicy.name,
          unlockRequirement = cert.unlockRequirement,
          isUnlocked = cert.isUnlockedByDefault,
          notarialSealHash = cert.notarialSealHash,
          description = cert.description,
          licenseHash = cert.extensionLicenseHash
        )
      }
      db.necCertificateDao().insertAll(entities)
    }
  }
}
