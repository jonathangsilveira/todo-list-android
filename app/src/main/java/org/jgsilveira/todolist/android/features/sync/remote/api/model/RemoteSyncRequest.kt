package org.jgsilveira.todolist.android.features.sync.remote.api.model

data class RemoteSyncRequest(
    val frequency: RemoteSyncFrequency,
    val strategyName: String,
    val entityId: String,
    val payload: String?
)