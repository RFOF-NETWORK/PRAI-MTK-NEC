package com.example.data

import android.content.Context
import com.example.auth.AuthManager
import com.example.crypto.AESEncryption
import com.example.db.AppDatabase
import com.example.db.RepositoryEntity
import com.example.model.SovereignLicenseData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class CodeRepository(
  val id: String,
  val name: String,
  val ownerHandle: String,
  val organization: String, // "©", "GbR", "eGbR", "geGbR", "Stiftung"
  val description: String,
  val licenseType: String,
  val defaultBranch: String = "main",
  val isPrivate: Boolean = false,
  val linkedNecCertId: String = "",
  val pagesUrl: String = "",
  val latestCommitHash: String = "",
  val collaboratorRole: String = "Erfinder",
  val commitsCount: Int = 12,
  val starsCount: Int = 8,
  val forksCount: Int = 3,
  val createdAt: Long = System.currentTimeMillis()
)

object RepositoryManager {
  val ORGANIZATIONS = listOf(
    "© (Urheber & Erfinder)",
    "GbR (BGB-Gesellschaft)",
    "eGbR (Eingetragene GbR)",
    "geGbR (Gemeinnützige eGbR)",
    "Stiftung (Autarke Treuhand)"
  )

  private val _repositories = MutableStateFlow<List<CodeRepository>>(emptyList())
  val repositories: StateFlow<List<CodeRepository>> = _repositories.asStateFlow()

  private val coroutineScope = CoroutineScope(Dispatchers.IO)
  private var appDatabase: AppDatabase? = null

  init {
    initDefaultRepositories()
  }

  fun initializeDatabase(context: Context) {
    if (appDatabase == null) {
      appDatabase = AppDatabase.getDatabase(context)
      // Populate or sync from DB
      coroutineScope.launch {
        appDatabase?.repositoryDao()?.getAllRepositories()?.collect { entities ->
          if (entities.isNotEmpty()) {
            _repositories.value = entities.map { it.toCodeRepository() }
          } else {
            // Seed DB with defaults
            val defaults = _repositories.value.map { it.toEntity() }
            appDatabase?.repositoryDao()?.insertAll(defaults)
          }
        }
      }
    }
  }

  private fun initDefaultRepositories() {
    _repositories.value = listOf(
      CodeRepository(
        id = "repo-001",
        name = "prai-core-engine",
        ownerHandle = "RFOF-NETWORK",
        organization = "© (Urheber & Erfinder)",
        description = "Der mathematische und kybernetische Kern von PRAI / MTK / NEC mit 4 Layern und 28 Kategorien.",
        licenseType = SovereignLicenseData.LICENSE_NAME,
        linkedNecCertId = "NEC-001",
        pagesUrl = "https://rfof-network.github.io/prai-core-engine",
        latestCommitHash = AESEncryption.sha256("COMMIT_001_PRAI_CORE_INIT").take(16),
        collaboratorRole = "Admin",
        commitsCount = 142,
        starsCount = 64,
        forksCount = 18
      ),
      CodeRepository(
        id = "repo-002",
        name = "mtk-escrow-contracts",
        ownerHandle = "RFOF-NETWORK",
        organization = "Stiftung (Autarke Treuhand)",
        description = "Treuhand- und Pfandrechtsverträge nach §§ 1274, 1280 BGB mit notarieller Scheckverbriefung (§ 36 BeurkG).",
        licenseType = SovereignLicenseData.LICENSE_NAME,
        linkedNecCertId = "NEC-002",
        pagesUrl = "https://rfof-network.github.io/mtk-escrow-contracts",
        latestCommitHash = AESEncryption.sha256("COMMIT_002_ESCROW_RELEASE").take(16),
        collaboratorRole = "Admin",
        commitsCount = 89,
        starsCount = 42,
        forksCount = 9
      ),
      CodeRepository(
        id = "repo-003",
        name = "nec-notary-dual-parity",
        ownerHandle = "RFOF-NETWORK",
        organization = "eGbR (Eingetragene GbR)",
        description = "Schnittstelle zur synchronen Co-Existenz von MTK-NFTs und XJustiz-Notariatsakten.",
        licenseType = SovereignLicenseData.LICENSE_NAME,
        linkedNecCertId = "NEC-004",
        pagesUrl = "https://rfof-network.github.io/nec-notary-dual-parity",
        latestCommitHash = AESEncryption.sha256("COMMIT_003_NOTARIAL_XJUSTIZ").take(16),
        collaboratorRole = "Erfinder",
        commitsCount = 61,
        starsCount = 37,
        forksCount = 6
      ),
      CodeRepository(
        id = "repo-004",
        name = "wohnzentren-cad-blueprints",
        ownerHandle = "RFOF-NETWORK",
        organization = "geGbR (Gemeinnützige eGbR)",
        description = "Autarke 2-Kreise-Siedlungsmodelle mit Permakultur-Gürteln und dezentraler Energieversorgung.",
        licenseType = "MIT / RFOF-Open-Architecture",
        linkedNecCertId = "NEC-005",
        pagesUrl = "https://rfof-network.github.io/wohnzentren-cad-blueprints",
        latestCommitHash = AESEncryption.sha256("COMMIT_004_WOHNZENTREN_V2").take(16),
        collaboratorRole = "Partner",
        commitsCount = 34,
        starsCount = 29,
        forksCount = 4
      )
    )
  }

  fun publishRepository(
    name: String,
    organization: String,
    description: String,
    licenseType: String,
    isPrivate: Boolean,
    linkedNecCertId: String
  ): CodeRepository {
    val currentUser = AuthManager.currentUser.value
    val repoId = "repo-${System.currentTimeMillis() % 100000}"
    val commitHash = AESEncryption.sha256("${currentUser.username}:$name:$repoId:${System.currentTimeMillis()}").take(16)
    val pagesUrl = "https://rfof-network.github.io/${name.lowercase().replace(" ", "-")}"

    val newRepo = CodeRepository(
      id = repoId,
      name = name,
      ownerHandle = currentUser.username,
      organization = organization,
      description = description,
      licenseType = licenseType,
      defaultBranch = "main",
      isPrivate = isPrivate,
      linkedNecCertId = linkedNecCertId,
      pagesUrl = pagesUrl,
      latestCommitHash = commitHash,
      collaboratorRole = if (currentUser.role.name == "ADMIN") "Admin" else "Erfinder",
      commitsCount = 1,
      starsCount = 1,
      forksCount = 0
    )

    _repositories.value = listOf(newRepo) + _repositories.value

    coroutineScope.launch {
      appDatabase?.repositoryDao()?.insertRepository(newRepo.toEntity())
    }

    return newRepo
  }

  private fun RepositoryEntity.toCodeRepository(): CodeRepository {
    return CodeRepository(
      id = this.id,
      name = this.name,
      ownerHandle = this.ownerHandle,
      organization = this.organization,
      description = this.description,
      licenseType = this.licenseType,
      defaultBranch = this.defaultBranch,
      isPrivate = this.isPrivate,
      linkedNecCertId = this.linkedNecCertId,
      pagesUrl = this.pagesUrl,
      latestCommitHash = this.latestCommitHash,
      collaboratorRole = this.collaboratorRole,
      createdAt = this.createdAt
    )
  }

  private fun CodeRepository.toEntity(): RepositoryEntity {
    return RepositoryEntity(
      id = this.id,
      name = this.name,
      ownerHandle = this.ownerHandle,
      organization = this.organization,
      description = this.description,
      licenseType = this.licenseType,
      defaultBranch = this.defaultBranch,
      isPrivate = this.isPrivate,
      linkedNecCertId = this.linkedNecCertId,
      pagesUrl = this.pagesUrl,
      latestCommitHash = this.latestCommitHash,
      collaboratorRole = this.collaboratorRole,
      createdAt = this.createdAt
    )
  }
}
