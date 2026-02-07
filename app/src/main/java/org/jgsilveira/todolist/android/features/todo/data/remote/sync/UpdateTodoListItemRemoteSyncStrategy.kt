package org.jgsilveira.todolist.android.features.todo.data.remote.sync

import io.ktor.client.plugins.ServerResponseException
import org.jgsilveira.todolist.android.features.sync.remote.api.sync.RemoteSyncStrategy
import org.jgsilveira.todolist.android.features.todo.data.remote.client.TodoListClient
import org.jgsilveira.todolist.android.features.todo.data.remote.mapper.toDomain
import org.jgsilveira.todolist.android.features.todo.domain.repository.LocalTodoListRepository

class UpdateTodoListItemRemoteSyncStrategy internal constructor(
    private val todoListClient: TodoListClient,
    private val localTodoListRepository: LocalTodoListRepository,
    private val decoder: TodoListItemDecoder
): RemoteSyncStrategy {
    override suspend fun sync(entityId: String, payload: String?) {
        val payload = payload
            ?: throw Exception("Payload to $entityId must not be null to add TODO list items remotely")
        val requestBody = decoder.decode(payload)
        val response = todoListClient.updateItem(requestBody)
        val updatedItem = response.toDomain()
        localTodoListRepository.updateItem(updatedItem)
    }

    override suspend fun shouldRetryOnError(
        attempts: Int,
        error: Throwable
    ): Boolean = attempts < 4 && error is ServerResponseException
}