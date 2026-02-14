package org.jgsilveira.todolist.android.features.todo.data.remote.client

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.appendPathSegments
import org.jgsilveira.todolist.android.features.todo.data.remote.model.RemoteTodoListItem
import org.jgsilveira.todolist.android.features.todo.data.remote.model.TodoListItemBody

private const val V1_TODO_LIST_PATH = "tasks/v1"

internal class KtorTodoListClient(
    private val httpClient: HttpClient
) : TodoListClient {
    override suspend fun addItem(body: TodoListItemBody): RemoteTodoListItem {
        val response = httpClient.post {
            url.appendPathSegments(V1_TODO_LIST_PATH, "task", "upsert")
            setBody(body)
        }
        return response.body()
    }

    override suspend fun updateItem(body: TodoListItemBody): RemoteTodoListItem {
        return addItem(body)
    }

    override suspend fun removeItemByUuid(uuid: String) {
        httpClient.delete {
            url.appendPathSegments(V1_TODO_LIST_PATH, "task", "remove", uuid)
        }
    }

    override suspend fun getActiveItems(): List<RemoteTodoListItem> {
        val response = httpClient.get {
            url.appendPathSegments(V1_TODO_LIST_PATH, "active")
        }
        return response.body()
    }
}