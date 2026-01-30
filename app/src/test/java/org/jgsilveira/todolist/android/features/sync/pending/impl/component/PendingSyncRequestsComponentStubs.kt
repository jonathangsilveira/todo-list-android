package org.jgsilveira.todolist.android.features.sync.pending.impl.component

import org.jgsilveira.todolist.android.features.sync.pending.api.model.PendingSyncRequest
import org.jgsilveira.todolist.android.features.sync.pending.api.model.PendingSyncRequestCreation
import org.jgsilveira.todolist.android.features.sync.pending.api.model.SyncRequestType

internal object PendingSyncRequestsComponentStubs {

    const val GENERATED_ID = 1L

    const val INVALID_ID = -1L

    const val ENTITY_ID = "entity_id"

    const val EMPTY_PAYLOAD = ""

    val createSyncRequest = PendingSyncRequestCreation(
        actionType = SyncRequestType.CREATE,
        entityId = ENTITY_ID,
        payload = EMPTY_PAYLOAD
    )

    val createPendingSyncRequest = PendingSyncRequest(
        id = GENERATED_ID,
        actionType = SyncRequestType.CREATE,
        entityId = ENTITY_ID,
        payload = EMPTY_PAYLOAD,
        retryCount = 0,
        createdAt = 1L,
        updatedAt = null
    )

    val updatedPendingSyncRequest = createPendingSyncRequest.copy(
        retryCount = 1,
        updatedAt = 1L
    )

    val databaseException = kotlinx.io.IOException("database error")
}