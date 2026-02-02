package org.jgsilveira.todolist.android.features.sync.remote.api.enqueuer

import org.jgsilveira.todolist.android.features.sync.remote.api.model.RemoteSyncRequest

interface RemoteSyncRequestEnqueuer {
    suspend fun enqueueRemoteSyncRequest(remoteSyncRequest: RemoteSyncRequest)
}