package org.jgsilveira.todolist.android.features.sync.pending.api.model

data class PendingSyncRequestCreation(
    val actionType: SyncRequestType,
    val entityId: String,
    val payload: String?,
)
