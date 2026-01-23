package org.jgsilveira.todolist.android.core.keystore

interface KeyValueStore {
    suspend fun put(key: String, value: String?)
    suspend fun get(key: String): String?
    suspend fun remove(key: String)
    suspend fun keys(): Set<String>
    suspend fun values(): List<String?>
}