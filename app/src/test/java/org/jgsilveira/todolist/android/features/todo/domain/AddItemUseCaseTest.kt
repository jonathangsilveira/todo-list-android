package org.jgsilveira.todolist.android.features.todo.domain

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.jgsilveira.todolist.android.coroutines.rules.MainDispatcherRule
import org.jgsilveira.todolist.android.features.todo.domain.repository.LocalTodoListRepository
import org.jgsilveira.todolist.android.features.todo.domain.usecase.AddItemUseCase
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
internal class AddItemUseCaseTest {
    @get:Rule
    private val mainDispatcherRule = MainDispatcherRule()

    private val localTodoListRepositoryMock = mockk<LocalTodoListRepository>()

    private val addItemUseCase = AddItemUseCase(
        localTodoListRepository = localTodoListRepositoryMock,
        coroutineDispatcher = UnconfinedTestDispatcher()
    )

    @Test
    fun `invoke Should return Success When repository succeeds`() = runTest {
        // Given
        val item = TodoListItemStubs.pendingTodoListItem
        coEvery {
            localTodoListRepositoryMock.addItem(item)
        } just runs

        // When
        val result = addItemUseCase(item)

        // Then
        assert(result.isSuccess)
        coVerify {
            localTodoListRepositoryMock.addItem(item)
        }
    }

    @Test
    fun `invoke Should return Failure When repository fails`() = runTest {
        // Given
        val item = TodoListItemStubs.pendingTodoListItem
        coEvery {
            localTodoListRepositoryMock.addItem(item)
        } throws Exception("something went wrong")

        // When
        val result = addItemUseCase(item)

        // Then
        assert(result.isFailure)
        assertEquals("something went wrong", result.exceptionOrNull()?.message)
        coVerify {
            localTodoListRepositoryMock.addItem(item)
        }
    }
}