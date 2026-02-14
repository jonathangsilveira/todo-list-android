package org.jgsilveira.todolist.android.features.todo.domain

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.jgsilveira.todolist.android.coroutines.rules.MainDispatcherRule
import org.jgsilveira.todolist.android.features.todo.domain.model.TodoListRemoteSyncType
import org.jgsilveira.todolist.android.features.todo.domain.repository.LocalTodoListRepository
import org.jgsilveira.todolist.android.features.todo.domain.usecase.EnqueueItemChangesRemoteSyncRequestUseCase
import org.jgsilveira.todolist.android.features.todo.domain.usecase.UpdateItemUseCase
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
internal class UpdateItemUseCaseTest {
    @get:Rule
    private val mainDispatcherRule = MainDispatcherRule()

    private val localTodoListRepositoryMock = mockk<LocalTodoListRepository>()

    private val enqueueItemChangesRequestMock = mockk<EnqueueItemChangesRemoteSyncRequestUseCase>()

    private val updateItemUseCase = UpdateItemUseCase(
        localTodoListRepository = localTodoListRepositoryMock,
        enqueueItemChangesRequest = enqueueItemChangesRequestMock,
        coroutineDispatcher = UnconfinedTestDispatcher()
    )

    @Test
    fun `invoke Should return Success When repository succeeds`() = runTest {
        // Given
        val item = TodoListItemStubs.doneTodoListItem
        val remoteSyncType = TodoListRemoteSyncType.UPDATE_ITEM
        coEvery {
            localTodoListRepositoryMock.updateItem(item)
        } just runs
        coEvery {
            enqueueItemChangesRequestMock.invoke(item, remoteSyncType)
        } just runs

        // When
        val result = updateItemUseCase(item)

        // Then
        assert(result.isSuccess)
        coVerify {
            localTodoListRepositoryMock.updateItem(item)
            enqueueItemChangesRequestMock.invoke(item, remoteSyncType)
        }
    }

    @Test
    fun `invoke Should return Failure When repository fails`() = runTest {
        // Given
        val item = TodoListItemStubs.doneTodoListItem
        coEvery {
            localTodoListRepositoryMock.updateItem(item)
        } throws Exception("something went wrong")

        // When
        val result = updateItemUseCase(item)

        // Then
        assert(result.isFailure)
        assertEquals("something went wrong", result.exceptionOrNull()?.message)
        coVerify {
            localTodoListRepositoryMock.updateItem(item)
        }
    }
}