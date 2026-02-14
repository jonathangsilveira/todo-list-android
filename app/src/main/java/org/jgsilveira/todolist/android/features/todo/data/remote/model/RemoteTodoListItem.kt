package org.jgsilveira.todolist.android.features.todo.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class RemoteTodoListItem(
    @SerialName("uuid") val uuid: String? = null,
    @SerialName("title") val title: String? = null,
    @SerialName("status") val status: String? = null,
    @SerialName("created_at") val createdAt: Long? = null,
    @SerialName("updated_at") val updatedAt: Long? = null,
    @SerialName("last_sync_at") val lastSyncAt: Long? = null
)
