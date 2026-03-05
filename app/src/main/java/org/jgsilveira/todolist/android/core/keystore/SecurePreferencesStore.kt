package org.jgsilveira.todolist.android.core.keystore

import org.jgsilveira.todolist.android.core.encrypt.Encryptor

internal class SecurePreferencesStore(
    private val preferences: PreferencesStore,
    private val encryptor: Encryptor
): KeyValueStore by preferences {

    override suspend fun put(key: String, value: String?) {
        val encryptedValue = if (!value.isNullOrEmpty()) {
            encryptor.encrypt(plainText = value)
        } else {
            value
        }
        preferences.put(key, encryptedValue)
    }

    override suspend fun get(key: String): String? {
        val value = preferences.get(key)
        if (value != null && encryptor.isEncrypted(value)) {
            return encryptor.decrypt(value)
        }
        return value
    }
}