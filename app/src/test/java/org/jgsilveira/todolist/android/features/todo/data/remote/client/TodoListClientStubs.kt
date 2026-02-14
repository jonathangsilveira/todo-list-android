package org.jgsilveira.todolist.android.features.todo.data.remote.client

import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.jgsilveira.todolist.android.features.todo.data.remote.model.RemoteTodoListItem
import org.jgsilveira.todolist.android.features.todo.data.remote.model.TodoListItemBody
import org.jgsilveira.todolist.android.features.todo.domain.model.TodoListItemStatus

internal object TodoListClientStubs {

    val addItemBody = TodoListItemBody(
        uuid = "uuid",
        title = "",
        status = TodoListItemStatus.PENDING.name,
        createdAt = 0L,
        updatedAt = null,
        lastSyncAt = null
    )

    val updateItemBody = addItemBody.copy(
        title = "Task",
        updatedAt = 2L,
        lastSyncAt = 1L
    )

    val addItemResponse = RemoteTodoListItem(
        uuid = "uuid",
        title = "",
        status = TodoListItemStatus.PENDING.name,
        createdAt = 0L,
        updatedAt = null,
        lastSyncAt = 1L
    )

    val updateItemResponse = addItemResponse.copy(
        title = "Task",
        updatedAt = 2L,
        lastSyncAt = 3L
    )

    val jsonSerializer = Json {
        encodeDefaults = true
        isLenient = true
        coerceInputValues = true
        ignoreUnknownKeys = true
    }

    fun createHttpClient(engine: HttpClientEngine): HttpClient {
        return org.jgsilveira.todolist.android.ktor.mock.createHttpClient(engine) {
            expectSuccess = true
            defaultRequest {
                headers.append(
                    HttpHeaders.ContentType,
                    ContentType.Application.Json.toString()
                )
            }
            install(plugin = ContentNegotiation) {
                json(jsonSerializer)
            }
        }
    }
}