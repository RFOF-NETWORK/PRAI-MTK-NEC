package com.example.crypto

import android.util.Base64
import java.security.MessageDigest
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec

data class EncryptedPayload(
  val cipherTextBase64: String,
  val ivBase64: String,
  val saltBase64: String,
  val algorithm: String = "AES/GCM/NoPadding-256"
)

object AESEncryption {
  private const val KEY_LENGTH_BITS = 256
  private const val ITERATION_COUNT = 10000
  private const val GCM_IV_LENGTH_BYTES = 12
  private const val GCM_TAG_LENGTH_BITS = 128
  private const val SALT_LENGTH_BYTES = 16

  private val BIP39_WORDLIST = listOf(
    "abandon", "ability", "able", "about", "above", "absent", "absorb", "abstract",
    "access", "accident", "account", "accuse", "achieve", "acid", "acoustic", "acquire",
    "across", "act", "action", "actor", "actress", "actual", "adapt", "add",
    "addict", "address", "adjust", "admit", "adult", "advance", "advice", "aerobic",
    "affair", "afford", "afraid", "again", "age", "agent", "agree", "ahead",
    "aim", "air", "airport", "aisle", "alarm", "album", "alcohol", "alert",
    "alien", "all", "alley", "allow", "almost", "alone", "alpha", "already",
    "also", "alter", "always", "amateur", "amazing", "among", "amount", "amused",
    "analyst", "anchor", "ancient", "anger", "angle", "angry", "animal", "ankle"
  )

  fun sha256(input: String): String {
    val md = MessageDigest.getInstance("SHA-256")
    val digest = md.digest(input.toByteArray(Charsets.UTF_8))
    return digest.joinToString("") { "%02x".format(it) }
  }

  fun generateSeedPhrase(wordCount: Int = 12): List<String> {
    val random = SecureRandom()
    return List(wordCount) {
      val index = random.nextInt(BIP39_WORDLIST.size)
      BIP39_WORDLIST[index]
    }
  }

  fun deriveAddresses(seedPhrase: List<String>): Map<String, String> {
    val seedString = seedPhrase.joinToString(" ")
    val btcHash = sha256("BTC:$seedString").take(34)
    val ethHash = sha256("ETH:$seedString").take(40)
    val tonHash = sha256("TON:$seedString").take(48)
    val mtkHash = sha256("MTK:$seedString").take(40)

    return mapOf(
      "BTC" to "bc1q$btcHash",
      "ETH" to "0x$ethHash",
      "TON" to "EQD$tonHash",
      "MTK" to "0xMTK$mtkHash"
    )
  }

  fun encrypt(plainText: String, passphrase: String): EncryptedPayload {
    val random = SecureRandom()
    val salt = ByteArray(SALT_LENGTH_BYTES).also { random.nextBytes(it) }
    val iv = ByteArray(GCM_IV_LENGTH_BYTES).also { random.nextBytes(it) }

    val secretKey = deriveKey(passphrase, salt)
    val cipher = Cipher.getInstance("AES/GCM/NoPadding")
    val spec = GCMParameterSpec(GCM_TAG_LENGTH_BITS, iv)
    cipher.init(Cipher.ENCRYPT_MODE, secretKey, spec)

    val cipherText = cipher.doFinal(plainText.toByteArray(Charsets.UTF_8))

    return EncryptedPayload(
      cipherTextBase64 = Base64.encodeToString(cipherText, Base64.NO_WRAP),
      ivBase64 = Base64.encodeToString(iv, Base64.NO_WRAP),
      saltBase64 = Base64.encodeToString(salt, Base64.NO_WRAP)
    )
  }

  fun decrypt(payload: EncryptedPayload, passphrase: String): String {
    val salt = Base64.decode(payload.saltBase64, Base64.NO_WRAP)
    val iv = Base64.decode(payload.ivBase64, Base64.NO_WRAP)
    val cipherText = Base64.decode(payload.cipherTextBase64, Base64.NO_WRAP)

    val secretKey = deriveKey(passphrase, salt)
    val cipher = Cipher.getInstance("AES/GCM/NoPadding")
    val spec = GCMParameterSpec(GCM_TAG_LENGTH_BITS, iv)
    cipher.init(Cipher.DECRYPT_MODE, secretKey, spec)

    val plainBytes = cipher.doFinal(cipherText)
    return String(plainBytes, Charsets.UTF_8)
  }

  fun signDocumentSeal(documentId: String, contentHash: String, signer: String): String {
    val timestamp = System.currentTimeMillis()
    val rawToSign = "DOC:$documentId|HASH:$contentHash|SIGNER:$signer|TIME:$timestamp"
    return "0x" + sha256(rawToSign)
  }

  private fun deriveKey(passphrase: String, salt: ByteArray): SecretKey {
    val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
    val keySpec = PBEKeySpec(passphrase.toCharArray(), salt, ITERATION_COUNT, KEY_LENGTH_BITS)
    val secretKeyBytes = factory.generateSecret(keySpec).encoded
    return SecretKeySpec(secretKeyBytes, "AES")
  }
}
