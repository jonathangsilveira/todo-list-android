package org.jgsilveira.todolist.android.features.sync.pending.api.model

data class PendingSyncRequest(
    val id: Long,
    val actionType: SyncRequestType,
    val entityId: String,
    val retryCount: Int,
    val createdAt: Long,
    val updatedAt: Long?,
    val payload: String?
)
