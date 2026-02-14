package org.jgsilveira.todolist.android.features.todo.data.remote.sync

import io.ktor.client.plugins.ServerResponseException
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import kotlinx.io.IOException
import kotlinx.serialization.SerializationException
import org.jgsilveira.todolist.android.coroutines.rules.MainDispatcherRule
import org.jgsilveira.todolist.android.features.todo.data.remote.client.TodoListClient
import org.jgsilveira.todolist.android.features.todo.data.remote.mapper.toDomain
import org.jgsilveira.todolist.android.features.todo.domain.repository.LocalTodoListRepository
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse

internal class UpdateTodoListItemRemoteSyncStrategyTest {
    @get:Rule
    private val mainDispatcherRule = MainDispatcherRule()

    private val clientMock = mockk<TodoListClient>()

    private val localRepoMock = mockk<LocalTodoListRepository>()

    private val decoderMock = mockk<TodoListItemDecoder>()

    private val remoteSyncStrategy = UpdateTodoListItemRemoteSyncStrategy(
        todoListClient = clientMock,
        localTodoListRepository = localRepoMock,
        decoder = decoderMock
    )

    @Test
    fun `sync Should throw Exception When payload is null`() = runTest {
        // Given
        val entityId = "uuid"
        val payload = null
        val expectedExceptionMessage = "Payload to $entityId must not be null to update TODO list items remotely"

        // When
        val exception = assertFailsWith<IllegalArgumentException> {
            remoteSyncStrategy.sync(entityId, payload)
        }

        // Then
        assertEquals(expectedExceptionMessage, exception.message)
    }

    @Test
    fun `sync Should throw Exception When payload is invalid`() = runTest {
        // Given
        val entityId = "uuid"
        val payload = "invalid-payload"
        every { decoderMock.decode(payload) } throws SerializationException("Error decoding payload")

        // When
        val exception = assertFailsWith<SerializationException> {
            remoteSyncStrategy.sync(entityId, payload)
        }

        // Then
        assertEquals("Error decoding payload", exception.message)
        verify {
            decoderMock.decode(payload)
        }
    }

    @Test
    fun `sync Should throw Exception When client fails`() = runTest {
        // Given
        val entityId = "uuid"
        val payload = "payload"
        val body = TodoListItemRemoteSyncStrategyStubs.updateItemBody
        every { decoderMock.decode(payload) } returns body
        coEvery { clientMock.updateItem(body) } throws IllegalStateException("Client failed")

        // When
        val exception = assertFailsWith<IllegalStateException> {
            remoteSyncStrategy.sync(entityId, payload)
        }

        // Then
        assertEquals("Client failed", exception.message)
        coVerify {
            decoderMock.decode(payload)
            clientMock.updateItem(body)
        }
    }

    @Test
    fun `sync Should throw Exception When local repository fails`() = runTest {
        // Given
        val entityId = "uuid"
        val payload = "payload"
        val body = TodoListItemRemoteSyncStrategyStubs.updateItemBody
        val response = TodoListItemRemoteSyncStrategyStubs.updateItemResponse
        every { decoderMock.decode(payload) } returns body
        coEvery { clientMock.updateItem(body) } returns response
        coEvery { localRepoMock.updateItem(response.toDomain()) } throws IOException("Database failed")

        // When
        val exception = assertFailsWith<IOException> {
            remoteSyncStrategy.sync(entityId, payload)
        }

        // Then
        assertEquals("Database failed", exception.message)
        coVerify {
            decoderMock.decode(payload)
            clientMock.updateItem(body)
            localRepoMock.updateItem(response.toDomain())
        }
    }

    @Test
    fun `sync Should complete without error When client and local repository succeeds`() = runTest {
        // Given
        val entityId = "uuid"
        val payload = "payload"
        val body = TodoListItemRemoteSyncStrategyStubs.updateItemBody
        val response = TodoListItemRemoteSyncStrategyStubs.updateItemResponse
        every { decoderMock.decode(payload) } returns body
        coEvery { clientMock.updateItem(body) } returns response
        coEvery { localRepoMock.updateItem(response.toDomain()) } just runs

        // When
        remoteSyncStrategy.sync(entityId, payload)

        // Then
        coVerify {
            decoderMock.decode(payload)
            clientMock.updateItem(body)
            localRepoMock.updateItem(response.toDomain())
        }
    }

    @Test
    fun `shouldRetryOnError Should return true When error is ServerResponseException and attempts is less than 4`() = runTest {
        // Given
        val attempts = 2
        val error = mockk<ServerResponseException>()

        // When
        val result = remoteSyncStrategy.shouldRetryOnError(attempts, error)

        // Then
        assertTrue(result)
    }

    @Test
    fun `shouldRetryOnError Should return false When error is ServerResponseException and attempts is equal to 4`() = runTest {
        // Given
        val attempts = 4
        val error = mockk<ServerResponseException>()

        // When
        val result = remoteSyncStrategy.shouldRetryOnError(attempts, error)

        // Then
        assertFalse(result)
    }

    @Test
    fun `shouldRetryOnError Should return false When error is not ServerResponseException and attempts is lass than 4`() = runTest {
        // Given
        val attempts = 2
        val error = IOException("error")

        // When
        val result = remoteSyncStrategy.shouldRetryOnError(attempts, error)

        // Then
        assertFalse(result)
    }
}