package org.jgsilveira.todolist.android.features.sync.remote.impl.enqueuer

import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.Operation
import androidx.work.WorkRequest
import org.jgsilveira.todolist.android.core.database.room.dao.RemoteSyncRequestDao
import org.jgsilveira.todolist.android.core.database.room.entity.RemoteSyncRequestEntity
import org.jgsilveira.todolist.android.core.workmanager.WorkManagerProvider
import org.jgsilveira.todolist.android.features.sync.remote.api.enqueuer.RemoteSyncRequestEnqueuer
import org.jgsilveira.todolist.android.features.sync.remote.api.model.RemoteSyncRequest
import org.jgsilveira.todolist.android.features.sync.remote.impl.job.syncJobParamsOf
import org.jgsilveira.todolist.android.features.sync.remote.impl.worker.RemoteSyncWorker
import java.util.concurrent.TimeUnit

internal class WorkManagerRemoteSyncRequestEnqueuer(
    private val workManagerProvider: WorkManagerProvider,
    private val dao: RemoteSyncRequestDao
) : RemoteSyncRequestEnqueuer {
    override suspend fun enqueueRemoteSyncRequest(
        remoteSyncRequest: RemoteSyncRequest
    ) {
        val remoteSyncRequestId = addRemoteSyncRequest(remoteSyncRequest)
        enqueueUniqueWork(remoteSyncRequestId)
    }

    private suspend fun addRemoteSyncRequest(remoteSyncRequest: RemoteSyncRequest): Long {
        val entity = RemoteSyncRequestEntity(
            frequency = remoteSyncRequest.frequency.name,
            strategyName = remoteSyncRequest.strategyName,
            entityId = remoteSyncRequest.entityId,
            payload = remoteSyncRequest.payload
        )
        val remoteSyncRequestId = dao.add(entity)
        return remoteSyncRequestId
    }

    private fun enqueueUniqueWork(remoteSyncRequestId: Long): Operation {
        val inputData = syncJobParamsOf(remoteSyncRequestId)
        val constraints = Constraints.Builder()
            .setRequiresBatteryNotLow(true)
            .setRequiredNetworkRequest(
                networkRequest = NetworkRequest.Builder()
                    .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                    .build(),
                networkType = NetworkType.CONNECTED
            )
            .build()
        val workRequest = OneTimeWorkRequestBuilder<RemoteSyncWorker>()
            .setInputData(inputData)
            .setConstraints(constraints = constraints)
            .setBackoffCriteria(
                backoffPolicy = BackoffPolicy.EXPONENTIAL,
                backoffDelay = WorkRequest.DEFAULT_BACKOFF_DELAY_MILLIS,
                timeUnit = TimeUnit.MILLISECONDS
            ).build()
        val workManager = workManagerProvider.get()
        return workManager.enqueueUniqueWork(
            uniqueWorkName = "REMOTE_SYNC_REQUEST_WORKER",
            existingWorkPolicy = ExistingWorkPolicy.APPEND_OR_REPLACE,
            request = workRequest
        )
    }
}