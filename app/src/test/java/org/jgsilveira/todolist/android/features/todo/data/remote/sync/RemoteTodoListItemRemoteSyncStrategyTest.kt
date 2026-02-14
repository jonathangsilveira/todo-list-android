package org.jgsilveira.todolist.android.features.todo.data.remote.sync

import io.ktor.client.plugins.ServerResponseException
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import okio.IOException
import org.jgsilveira.todolist.android.coroutines.rules.MainDispatcherRule
import org.jgsilveira.todolist.android.features.todo.data.remote.client.TodoListClient
import org.jgsilveira.todolist.android.features.todo.domain.repository.LocalTodoListRepository
import org.junit.Rule
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

internal class RemoteTodoListItemRemoteSyncStrategyTest {
    @get:Rule
    private val mainDispatcherRule = MainDispatcherRule()

    private val clientMock = mockk<TodoListClient>()

    private val localRepoMock = mockk<LocalTodoListRepository>()

    private val remoteSyncStrategy = RemoveTodoListItemRemoteSyncStrategy(
        todoListClient = clientMock,
        localTodoListRepository = localRepoMock
    )

    @Test
    fun `sync Should throw Exception When client fails`() = runTest {
        // Given
        val entityId = "uuid"
        val payload = null
        coEvery { clientMock.removeItemByUuid(entityId) } throws Exception("Client failed")

        // When
        val exception = assertFailsWith<Exception> {
            remoteSyncStrategy.sync(entityId, payload)
        }

        // Then
        assertEquals("Client failed", exception.message)
        coVerify {
            clientMock.removeItemByUuid(entityId)
        }
    }

    @Test
    fun `sync Should throw Exception When local repository fails`() = runTest {
        // Given
        val entityId = "uuid"
        val payload = null
        coEvery { clientMock.removeItemByUuid(entityId) } just runs
        coEvery {
            localRepoMock.removeItemByUuid(entityId)
        } throws IOException("Database failed")

        // When
        val exception = assertFailsWith<IOException> {
            remoteSyncStrategy.sync(entityId, payload)
        }

        // Then
        assertEquals("Database failed", exception.message)
        coVerify {
            clientMock.removeItemByUuid(entityId)
            localRepoMock.removeItemByUuid(entityId)
        }
    }

    @Test
    fun `sync Should complete without error When client and local repository succeeds`() = runTest {
        // Given
        val entityId = "uuid"
        val payload = null
        coEvery { clientMock.removeItemByUuid(entityId) } just runs
        coEvery {
            localRepoMock.removeItemByUuid(entityId)
        }  just runs

        // When
        remoteSyncStrategy.sync(entityId, payload)

        // Then
        coVerify {
            clientMock.removeItemByUuid(entityId)
            localRepoMock.removeItemByUuid(entityId)
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