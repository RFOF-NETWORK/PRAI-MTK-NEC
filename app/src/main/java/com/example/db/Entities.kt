package com.example.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "nec_certificates")
data class NecCertificateEntity(
  @PrimaryKey val id: String,
  val title: String,
  val categoryId: Int,
  val categoryName: String,
  val nftTokenId: String,
  val databaseRecordId: String,
  val transferPolicy: String,
  val unlockRequirement: String,
  val isUnlocked: Boolean,
  val notarialSealHash: String,
  val description: String,
  val licenseHash: String,
  val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "repositories")
data class RepositoryEntity(
  @PrimaryKey val id: String,
  val name: String,
  val ownerHandle: String,
  val organization: String, // "©", "GbR", "eGbR", "geGbR", "Stiftung"
  val description: String,
  val licenseType: String, // "RFOF-SOVEREIGN-v1.0", "MIT", "Apache-2.0"
  val defaultBranch: String = "main",
  val isPrivate: Boolean = false,
  val linkedNecCertId: String = "",
  val pagesUrl: String = "",
  val latestCommitHash: String = "",
  val collaboratorRole: String = "Erfinder", // "Admin", "Erfinder", "Partner", "Kunde"
  val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "user_profiles")
data class UserProfileEntity(
  @PrimaryKey val userId: String,
  val username: String,
  val role: String, // "ADMIN", "USER"
  val userType: String, // "ADMIN", "ERFINDER", "PARTNER", "KUNDE"
  val authProvider: String, // "RFOF_CREDENTIALS", "GOOGLE_OAUTH", "WEB3_CONNECT", "GUEST"
  val email: String,
  val avatarUrl: String = "",
  val bio: String = "",
  val walletAddress: String = "",
  val sshPublicKey: String = "",
  val gpgKeyId: String = "",
  val personalAccessToken: String = "",
  val editorTheme: String = "VS Code Dark",
  val organization: String = "©",
  val updatedAt: Long = System.currentTimeMillis()
)
