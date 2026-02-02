package org.jgsilveira.todolist.android.features.sync.remote.api.sync

interface RemoteSyncStrategy {
    suspend fun sync(entityId: String, payload: String?)
    suspend fun shouldRetryOnError(attempts: Int, error: Throwable): Boolean
}