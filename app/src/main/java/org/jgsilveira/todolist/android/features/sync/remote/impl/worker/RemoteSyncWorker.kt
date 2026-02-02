package org.jgsilveira.todolist.android.features.sync.remote.impl.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import org.jgsilveira.todolist.android.features.sync.remote.impl.job.WorkManagerSyncJob

internal class RemoteSyncWorker(
    appContext: Context,
    params: WorkerParameters,
    private val syncJob: WorkManagerSyncJob
): CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        return syncJob.sync(input = inputData)
    }

    companion object {
        fun create(
            appContext: Context,
            params: WorkerParameters,
            syncJob: WorkManagerSyncJob
        ): RemoteSyncWorker = RemoteSyncWorker(
            appContext = appContext,
            params = params,
            syncJob = syncJob
        )
    }
}