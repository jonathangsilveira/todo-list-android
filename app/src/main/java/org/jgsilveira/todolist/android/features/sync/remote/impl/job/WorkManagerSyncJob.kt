package org.jgsilveira.todolist.android.features.sync.remote.impl.job

import androidx.work.Data
import androidx.work.ListenableWorker
import org.jgsilveira.todolist.android.core.database.room.dao.RemoteSyncRequestDao
import org.jgsilveira.todolist.android.core.database.room.entity.RemoteSyncRequestEntity
import org.jgsilveira.todolist.android.features.sync.remote.api.provider.RemoteSyncProvider

internal class WorkManagerSyncJob(
    private val dao: RemoteSyncRequestDao
) {

    suspend fun sync(input: Data): ListenableWorker.Result {
        val syncRequestId = input.getSyncRequestId()
        val syncRequest = dao.findById(syncRequestId)
            ?: return ListenableWorker.Result.success()
        val syncer = RemoteSyncProvider.get(syncRequest.strategyName)
            ?: return ListenableWorker.Result.success()
        val newAttemptRequest = syncRequest.copy(
            attempts = syncRequest.attempts.inc()
        )
        try {
            syncer.sync(syncRequest.entityId, syncRequest.payload)
            deleteSyncRequest(syncRequestId)
        } catch (error: Exception) {
            val shouldRetryOnError = syncer.shouldRetryOnError(
                attempts = newAttemptRequest.attempts,
                error = error
            )
            if (shouldRetryOnError) {
                updateRetryCount(newAttemptRequest)
                return ListenableWorker.Result.retry()
            }
            deleteSyncRequest(syncRequestId)
        }
        return ListenableWorker.Result.success()
    }

    private suspend fun updateRetryCount(syncRequest: RemoteSyncRequestEntity) {
        dao.update(syncRequest)
    }

    private suspend fun deleteSyncRequest(id: Long) {
        dao.deleteById(id)
    }
}