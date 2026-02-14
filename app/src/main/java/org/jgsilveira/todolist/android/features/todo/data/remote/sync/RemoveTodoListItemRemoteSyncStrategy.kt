package org.jgsilveira.todolist.android.features.todo.data.remote.sync

import io.ktor.client.plugins.ServerResponseException
import org.jgsilveira.todolist.android.features.sync.remote.api.sync.RemoteSyncStrategy
import org.jgsilveira.todolist.android.features.todo.data.remote.client.TodoListClient
import org.jgsilveira.todolist.android.features.todo.domain.repository.LocalTodoListRepository

class RemoveTodoListItemRemoteSyncStrategy internal constructor(
    private val todoListClient: TodoListClient,
    private val localTodoListRepository: LocalTodoListRepository,
): RemoteSyncStrategy {
    override suspend fun sync(entityId: String, payload: String?) {
        todoListClient.removeItemByUuid(entityId)
        localTodoListRepository.removeItemByUuid(entityId)
    }

    override suspend fun shouldRetryOnError(
        attempts: Int,
        error: Throwable
    ): Boolean = attempts < 4 && error is ServerResponseException
}