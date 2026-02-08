package org.jgsilveira.todolist.android.features.todo.data.remote.client

import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.test.runTest
import org.jgsilveira.todolist.android.coroutines.rules.MainDispatcherRule
import org.jgsilveira.todolist.android.features.todo.data.remote.client.TodoListClientStubs.addItemBody
import org.jgsilveira.todolist.android.features.todo.data.remote.client.TodoListClientStubs.addItemResponse
import org.jgsilveira.todolist.android.features.todo.data.remote.client.TodoListClientStubs.jsonSerializer
import org.jgsilveira.todolist.android.features.todo.data.remote.client.TodoListClientStubs.updateItemBody
import org.jgsilveira.todolist.android.features.todo.data.remote.client.TodoListClientStubs.updateItemResponse
import org.jgsilveira.todolist.android.features.todo.data.remote.model.RemoteTodoListItem
import org.jgsilveira.todolist.android.ktor.mock.mockEngineRespondingServerErrorJson
import org.jgsilveira.todolist.android.ktor.mock.mockEngineRespondingSuccessJson
import org.jgsilveira.todolist.android.ktor.mock.mockEngineRespondingWith
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

internal class KtorTodoListClientTest {
    @get:Rule
    private val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `addItem Should return OK When body is correct`() = runTest {
        // Given
        val engine = mockEngineRespondingSuccessJson(
            content = addItemResponse,
            json = jsonSerializer
        )
        val client = KtorTodoListClient(
            httpClient = TodoListClientStubs.createHttpClient(engine)
        )

        // When
        val response = client.addItem(addItemBody)

        // Then
        assertEquals(response, addItemResponse)
    }

    @Test
    fun `addItem Should return server error When client returns InternalServerError`() = runTest {
        // Given
        val engine = mockEngineRespondingServerErrorJson(
            content = "",
            json = jsonSerializer
        )
        val client = KtorTodoListClient(
            httpClient = TodoListClientStubs.createHttpClient(engine)
        )

        // When
        val exception = assertFailsWith<ServerResponseException> { client.addItem(addItemBody) }

        // Then
        assert(exception.response.status == HttpStatusCode.InternalServerError)
    }

    @Test
    fun `updateItem Should return updated item When client succeeds`() = runTest {
        // Given
        val engine = mockEngineRespondingSuccessJson(
            content = updateItemResponse,
            json = jsonSerializer
        )
        val client = KtorTodoListClient(
            httpClient = TodoListClientStubs.createHttpClient(engine)
        )

        // When
        val response = client.updateItem(updateItemBody)

        // Then
        assertEquals(response, updateItemResponse)
    }

    @Test
    fun `updateItem Should return server error When client returns InternalServerError`() = runTest {
        // Given
        val engine = mockEngineRespondingServerErrorJson(
            content = "",
            json = jsonSerializer
        )
        val client = KtorTodoListClient(
            httpClient = TodoListClientStubs.createHttpClient(engine)
        )

        // When
        val exception = assertFailsWith<ServerResponseException> {
            client.updateItem(updateItemBody)
        }

        // Then
        assert(exception.response.status == HttpStatusCode.InternalServerError)
    }

    @Test
    fun `removeItemByUuid Should return no content When client succeeds`() = runTest {
        // Given
        val engine = mockEngineRespondingWith(
            content = "",
            status = HttpStatusCode.NoContent
        )
        val client = KtorTodoListClient(
            httpClient = TodoListClientStubs.createHttpClient(engine)
        )

        // When
        client.removeItemByUuid("uuid")

        // Then
    }

    @Test
    fun `removeItemByUuid Should return not found When client fails`() = runTest {
        // Given
        val engine = mockEngineRespondingWith(
            content = "",
            status = HttpStatusCode.NotFound
        )
        val client = KtorTodoListClient(
            httpClient = TodoListClientStubs.createHttpClient(engine)
        )

        // When
        val exception = assertFailsWith<ClientRequestException> {
            client.removeItemByUuid("uuid")
        }

        // Then
        assert(exception.response.status == HttpStatusCode.NotFound)
    }

    @Test
    fun `getActiveItems Should return active items When client succeeds`() = runTest {
        // Given
        val engine = mockEngineRespondingSuccessJson(
            content = listOf(addItemResponse, updateItemResponse),
            json = jsonSerializer
        )
        val client = KtorTodoListClient(
            httpClient = TodoListClientStubs.createHttpClient(engine)
        )

        // When
        val response = client.getActiveItems()

        // Then
        assertEquals(response, listOf(addItemResponse, updateItemResponse))
    }

    @Test
    fun `getActiveItems Should return empty list When server returns no content`() = runTest {
        // Given
        val engine = mockEngineRespondingSuccessJson(
            content = listOf<RemoteTodoListItem>(),
            json = jsonSerializer
        )
        val client = KtorTodoListClient(
            httpClient = TodoListClientStubs.createHttpClient(engine)
        )

        // When
        val response = client.getActiveItems()

        // Then
        assertTrue(response.isEmpty())
    }

    @Test
    fun `getActiveItems Should return server error When client returns InternalServerError`() = runTest {
        // Given
        val engine = mockEngineRespondingServerErrorJson(
            content = "",
            json = jsonSerializer
        )
        val client = KtorTodoListClient(
            httpClient = TodoListClientStubs.createHttpClient(engine)
        )

        // When
        val exception = assertFailsWith<ServerResponseException> {
            client.getActiveItems()
        }

        // Then
        assert(exception.response.status == HttpStatusCode.InternalServerError)
    }
}