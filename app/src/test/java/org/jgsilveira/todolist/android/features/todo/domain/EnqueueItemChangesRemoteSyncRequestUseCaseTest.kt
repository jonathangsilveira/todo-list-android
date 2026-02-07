package org.jgsilveira.todolist.android.features.todo.domain

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.test.runTest
import kotlinx.io.IOException
import kotlinx.serialization.json.Json
import org.jgsilveira.todolist.android.coroutines.rules.MainDispatcherRule
import org.jgsilveira.todolist.android.features.sync.remote.api.enqueuer.RemoteSyncRequestEnqueuer
import org.jgsilveira.todolist.android.features.sync.remote.api.model.RemoteSyncFrequency
import org.jgsilveira.todolist.android.features.sync.remote.api.model.RemoteSyncRequest
import org.jgsilveira.todolist.android.features.todo.data.remote.mapper.toBody
import org.jgsilveira.todolist.android.features.todo.domain.model.TodoListRemoteSyncType
import org.jgsilveira.todolist.android.features.todo.domain.usecase.EnqueueItemChangesRemoteSyncRequestUseCase
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

internal class EnqueueItemChangesRemoteSyncRequestUseCaseTest {
    @get:Rule
    private val mainDispatcherRule = MainDispatcherRule()

    private val remoteSyncRequestEnqueuerMock = mockk<RemoteSyncRequestEnqueuer>()

    private val jsonSerializer = Json {
        encodeDefaults = true
        ignoreUnknownKeys = true
    }

    private val useCase = EnqueueItemChangesRemoteSyncRequestUseCase(
        enqueuer = remoteSyncRequestEnqueuerMock,
        jsonSerializer = jsonSerializer
    )

    @Test
    fun `invoke Should enqueue sync request without payload When type is REMOVE_ITEM`() = runTest {
        // Given
        val item = TodoListItemStubs.pendingTodoListItem
        val remoteSyncType = TodoListRemoteSyncType.REMOVE_ITEM
        val remoteSyncRequest = RemoteSyncRequest(
            frequency = RemoteSyncFrequency.UNIQUE,
            strategyName = remoteSyncType.value,
            entityId = "uuid",
            payload = null
        )
        coEvery {
            remoteSyncRequestEnqueuerMock.enqueueRemoteSyncRequest(remoteSyncRequest)
        } just runs

        // When
        useCase.invoke(item, remoteSyncType)

        // Then
        coVerify {
            remoteSyncRequestEnqueuerMock.enqueueRemoteSyncRequest(remoteSyncRequest)
        }
    }

    @Test
    fun `invoke Should enqueue sync request with payload When type is UPDATE_ITEM`() = runTest {
        // Given
        val item = TodoListItemStubs.pendingTodoListItem
        val remoteSyncType = TodoListRemoteSyncType.UPDATE_ITEM
        val remoteSyncRequest = RemoteSyncRequest(
            frequency = RemoteSyncFrequency.UNIQUE,
            strategyName = remoteSyncType.value,
            entityId = "uuid",
            payload = jsonSerializer.encodeToString(item.toBody())
        )
        coEvery {
            remoteSyncRequestEnqueuerMock.enqueueRemoteSyncRequest(remoteSyncRequest)
        } just runs

        // When
        useCase.invoke(item, remoteSyncType)

        // Then
        coVerify {
            remoteSyncRequestEnqueuerMock.enqueueRemoteSyncRequest(remoteSyncRequest)
        }
    }

    @Test
    fun `invoke Should throw Exception When enqueuer fails`() = runTest {
        // Given
        val item = TodoListItemStubs.pendingTodoListItem
        val remoteSyncType = TodoListRemoteSyncType.REMOVE_ITEM
        val remoteSyncRequest = RemoteSyncRequest(
            frequency = RemoteSyncFrequency.UNIQUE,
            strategyName = remoteSyncType.value,
            entityId = "uuid",
            payload = null
        )
        coEvery {
            remoteSyncRequestEnqueuerMock.enqueueRemoteSyncRequest(remoteSyncRequest)
        } throws IOException("Error enqueuing remote sync. request")

        // When
        val exception = assertFailsWith<IOException> { useCase.invoke(item, remoteSyncType) }

        // Then
        assertEquals(expected = "Error enqueuing remote sync. request", actual = exception.message)
        coVerify {
            remoteSyncRequestEnqueuerMock.enqueueRemoteSyncRequest(remoteSyncRequest)
        }
    }
}