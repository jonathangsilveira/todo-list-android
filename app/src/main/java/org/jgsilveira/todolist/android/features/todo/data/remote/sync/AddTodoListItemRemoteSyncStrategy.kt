package org.jgsilveira.todolist.android.features.todo.data.remote.sync

import io.ktor.client.plugins.ServerResponseException
import org.jgsilveira.todolist.android.features.sync.remote.api.sync.RemoteSyncStrategy
import org.jgsilveira.todolist.android.features.todo.data.remote.client.TodoListClient
import org.jgsilveira.todolist.android.features.todo.data.remote.mapper.toDomain
import org.jgsilveira.todolist.android.features.todo.domain.repository.LocalTodoListRepository
import kotlin.IllegalArgumentException

class AddTodoListItemRemoteSyncStrategy internal constructor(
    private val todoListClient: TodoListClient,
    private val localTodoListRepository: LocalTodoListRepository,
    private val decoder: TodoListItemDecoder
): RemoteSyncStrategy {
    override suspend fun sync(entityId: String, payload: String?) {
        val payload = payload ?: throw IllegalArgumentException(
            "Payload to $entityId must not be null to add TODO list items remotely"
        )
        val requestBody = decoder.decode(payload)
        val response = todoListClient.addItem(requestBody)
        val createdItem = response.toDomain()
        localTodoListRepository.updateItem(createdItem)
    }

    override suspend fun shouldRetryOnError(
        attempts: Int,
        error: Throwable
    ): Boolean = attempts < 4 && error is ServerResponseException
}