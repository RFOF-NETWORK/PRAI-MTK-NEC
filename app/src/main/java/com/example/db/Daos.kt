package com.example.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface NecCertificateDao {
  @Query("SELECT * FROM nec_certificates ORDER BY id ASC")
  fun getAllCertificates(): Flow<List<NecCertificateEntity>>

  @Query("SELECT * FROM nec_certificates WHERE id = :id")
  suspend fun getCertificateById(id: String): NecCertificateEntity?

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertCertificate(cert: NecCertificateEntity)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertAll(certs: List<NecCertificateEntity>)

  @Update
  suspend fun updateCertificate(cert: NecCertificateEntity)
}

@Dao
interface RepositoryDao {
  @Query("SELECT * FROM repositories ORDER BY createdAt DESC")
  fun getAllRepositories(): Flow<List<RepositoryEntity>>

  @Query("SELECT * FROM repositories WHERE organization = :org ORDER BY createdAt DESC")
  fun getRepositoriesByOrg(org: String): Flow<List<RepositoryEntity>>

  @Query("SELECT * FROM repositories WHERE ownerHandle = :owner ORDER BY createdAt DESC")
  fun getRepositoriesByOwner(owner: String): Flow<List<RepositoryEntity>>

  @Query("SELECT * FROM repositories WHERE id = :id")
  suspend fun getRepositoryById(id: String): RepositoryEntity?

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertRepository(repo: RepositoryEntity)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertAll(repos: List<RepositoryEntity>)

  @Delete
  suspend fun deleteRepository(repo: RepositoryEntity)
}

@Dao
interface UserProfileDao {
  @Query("SELECT * FROM user_profiles WHERE userId = :userId")
  suspend fun getProfile(userId: String): UserProfileEntity?

  @Query("SELECT * FROM user_profiles LIMIT 1")
  fun getCurrentProfileFlow(): Flow<UserProfileEntity?>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun saveProfile(profile: UserProfileEntity)
}
