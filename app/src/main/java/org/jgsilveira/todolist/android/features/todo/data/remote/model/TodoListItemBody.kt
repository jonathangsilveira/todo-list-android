package org.jgsilveira.todolist.android.features.todo.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class TodoListItemBody(
    @SerialName("uuid") val uuid: String,
    @SerialName("title") val title: String,
    @SerialName("status") val status: String,
    @SerialName("created_at") val createdAt: Long,
    @SerialName("updated_at") val updatedAt: Long? = null,
    @SerialName("last_sync_at") val lastSyncAt: Long? = null
)
