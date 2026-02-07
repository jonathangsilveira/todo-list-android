package org.jgsilveira.todolist.android.features.todo.domain.usecase

import kotlinx.serialization.json.Json
import org.jgsilveira.todolist.android.features.sync.remote.api.enqueuer.RemoteSyncRequestEnqueuer
import org.jgsilveira.todolist.android.features.sync.remote.api.model.RemoteSyncFrequency
import org.jgsilveira.todolist.android.features.sync.remote.api.model.RemoteSyncRequest
import org.jgsilveira.todolist.android.features.todo.data.remote.mapper.toBody
import org.jgsilveira.todolist.android.features.todo.domain.model.TodoListItem
import org.jgsilveira.todolist.android.features.todo.domain.model.TodoListRemoteSyncType

class EnqueueItemChangesRemoteSyncRequestUseCase internal constructor(
    private val enqueuer: RemoteSyncRequestEnqueuer,
    private val jsonSerializer: Json
){

    suspend operator fun invoke(
        item: TodoListItem,
        remoteSyncType: TodoListRemoteSyncType
    ) {
        val payload = if (remoteSyncType != TodoListRemoteSyncType.REMOVE_ITEM) {
            val requestBody = item.toBody()
            jsonSerializer.encodeToString(requestBody)
        } else null
        val syncRequest = RemoteSyncRequest(
            frequency = RemoteSyncFrequency.UNIQUE,
            strategyName = remoteSyncType.value,
            entityId = item.uuid,
            payload = payload
        )
        enqueuer.enqueueRemoteSyncRequest(syncRequest)
    }
}