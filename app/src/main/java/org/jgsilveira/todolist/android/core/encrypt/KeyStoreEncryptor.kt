package org.jgsilveira.todolist.android.core.encrypt

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

internal const val KEY_ALIAS = "app_encryptor_key"

private const val ANDROID_KEYSTORE = "AndroidKeyStore"

internal class KeyStoreEncryptor(
    private val keyStore: KeyStore = KeyStore.getInstance(ANDROID_KEYSTORE),
    private val keyAlias: String = KEY_ALIAS
): Encryptor {

    init {
        keyStore.load(null)
        generateKeyAliasIfNotExists()
    }

    override fun encrypt(plainText: String): String {
        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.ENCRYPT_MODE, getSecretKey())

        val iv = cipher.iv
        val encryptedBytes = cipher.doFinal(plainText.toByteArray(Charsets.UTF_8))
        return (Base64.encodeToString(iv, Base64.NO_WRAP)
                + IV_SEPARATOR
                + Base64.encodeToString(encryptedBytes, Base64.NO_WRAP))
    }

    override fun decrypt(encryptedText: String): String {
        val parts = encryptedText.split(IV_SEPARATOR)
        if (parts.size != 2) {
            throw IllegalArgumentException("Invalid encrypted data format")
        }

        val iv = Base64.decode(parts[0], Base64.NO_WRAP)
        val encryptedBytes = Base64.decode(parts[1], Base64.NO_WRAP)

        val cipher = Cipher.getInstance(TRANSFORMATION)
        val spec = GCMParameterSpec(GCM_TAG_LENGTH, iv)
        cipher.init(Cipher.DECRYPT_MODE, getSecretKey(), spec)

        val decryptedBytes = cipher.doFinal(encryptedBytes)
        return String(decryptedBytes, Charsets.UTF_8)
    }

    override fun isEncrypted(text: String): Boolean {
        val parts = text.split(IV_SEPARATOR)
        if (parts.size != 2) {
            return false
        }

        return try {
            Base64.decode(parts[0], Base64.NO_WRAP)
            Base64.decode(parts[1], Base64.NO_WRAP)
            true
        } catch (_: IllegalArgumentException) {
            false
        }
    }

    private fun getSecretKey(): SecretKey {
        return keyStore.getKey(keyAlias, null) as SecretKey
    }

    private fun generateKeyAliasIfNotExists() {
        if (keyStore.containsAlias(keyAlias)) {
            return
        }
        val keyGenerator = KeyGenerator.getInstance(
            KeyProperties.KEY_ALGORITHM_AES,
            ANDROID_KEYSTORE
        )

        val keyGenParameterSpec = buildKeyGenParameterSpec()
        keyGenerator.init(keyGenParameterSpec)
        keyGenerator.generateKey()
    }

    private fun buildKeyGenParameterSpec(): KeyGenParameterSpec {
        val keyGenParameterSpecBuilder = KeyGenParameterSpec.Builder(
            keyAlias,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        )
        return keyGenParameterSpecBuilder
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .setUserAuthenticationRequired(false)
            .setRandomizedEncryptionRequired(true)
            .build()
    }

    companion object {
        private const val TRANSFORMATION = "AES/GCM/NoPadding"
        private const val GCM_TAG_LENGTH = 128
        private const val IV_SEPARATOR = "]"
    }
}