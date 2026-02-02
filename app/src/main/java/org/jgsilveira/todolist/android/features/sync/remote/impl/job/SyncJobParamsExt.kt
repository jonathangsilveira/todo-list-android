package org.jgsilveira.todolist.android.features.sync.remote.impl.job

import androidx.work.Data
import androidx.work.workDataOf

private const val SYNC_REQUEST_ID_KEY = "REMOTE_SYNC_REQUEST_ID"

internal fun syncJobParamsOf(syncRequestId: Long): Data {
    return workDataOf(SYNC_REQUEST_ID_KEY to syncRequestId)
}

internal fun Data.getSyncRequestId(): Long = this.getLong(SYNC_REQUEST_ID_KEY, -1)