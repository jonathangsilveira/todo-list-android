package org.jgsilveira.todolist.android.features.sync.pending.impl.mapper

import org.jgsilveira.todolist.android.core.database.room.entity.PendingSyncRequestEntity
import org.jgsilveira.todolist.android.features.sync.pending.api.model.PendingSyncRequest
import org.jgsilveira.todolist.android.features.sync.pending.api.model.PendingSyncRequestCreation
import org.jgsilveira.todolist.android.features.sync.pending.api.model.SyncRequestType

internal fun PendingSyncRequestCreation.toEntity(): PendingSyncRequestEntity {
    return PendingSyncRequestEntity(
        actionType = actionType.name,
        entityId = entityId,
        payload = payload,
        retryCount = 0,
        createdAt = System.currentTimeMillis(),
        updatedAt = null
    )
}

internal fun PendingSyncRequest.toEntity(): PendingSyncRequestEntity {
    return PendingSyncRequestEntity(
        id = id,
        actionType = actionType.name,
        entityId = entityId,
        payload = payload,
        retryCount = retryCount,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

internal fun PendingSyncRequestEntity.toDomain(): PendingSyncRequest {
    return PendingSyncRequest(
        id = id!!,
        actionType = SyncRequestType.valueOf(actionType),
        entityId = entityId,
        payload = payload,
        retryCount = retryCount,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}