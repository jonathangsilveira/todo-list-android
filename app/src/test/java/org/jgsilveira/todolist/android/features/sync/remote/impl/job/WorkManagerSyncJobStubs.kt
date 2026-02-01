package org.jgsilveira.todolist.android.features.sync.remote.impl.job

import org.jgsilveira.todolist.android.core.database.room.entity.RemoteSyncRequestEntity
import org.jgsilveira.todolist.android.features.sync.remote.api.model.RemoteSyncFrequency

internal object WorkManagerSyncJobStubs {

    const val SYNC_REQUEST_ID = 1L

    const val ENTITY_ID = "uuid"

    const val STRATEGY_NAME = "fake_strategy"

    val syncRequestEntity = RemoteSyncRequestEntity(
        id = SYNC_REQUEST_ID,
        entityId = ENTITY_ID,
        attempts = 0,
        strategyName = STRATEGY_NAME,
        frequency = RemoteSyncFrequency.UNIQUE.name,
        payload = null
    )

    val exception = kotlinx.io.IOException("error")
}