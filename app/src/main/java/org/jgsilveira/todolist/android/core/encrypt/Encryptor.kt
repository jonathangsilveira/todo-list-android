package org.jgsilveira.todolist.android.core.encrypt

interface Encryptor {
    fun encrypt(plainText: String): String
    fun decrypt(encryptedText: String): String
    fun isEncrypted(text: String): Boolean
}