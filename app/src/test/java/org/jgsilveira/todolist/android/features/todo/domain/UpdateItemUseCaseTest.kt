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
import org.jgsilveira.todolist.android.features.todo.domain.repository.LocalTodoListRepository
import org.jgsilveira.todolist.android.features.todo.domain.usecase.UpdateItemUseCase
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
internal class UpdateItemUseCaseTest {
    private val testDispatcher = StandardTestDispatcher(
        scheduler = TestCoroutineScheduler()
    )

    private val localTodoListRepositoryMock = mockk<LocalTodoListRepository>()

    private val updateItemUseCase = UpdateItemUseCase(
        localTodoListRepository = localTodoListRepositoryMock,
        coroutineDispatcher = UnconfinedTestDispatcher()
    )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `invoke Should return Success When repository succeeds`() = runTest {
        // Given
        val item = TodoListItemStubs.doneTodoListItem
        coEvery {
            localTodoListRepositoryMock.updateItem(item)
        } just runs

        // When
        val result = updateItemUseCase(item)

        // Then
        assert(result.isSuccess)
        coVerify {
            localTodoListRepositoryMock.updateItem(item)
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
        assertEquals(result.exceptionOrNull()?.message, "something went wrong")
        coVerify {
            localTodoListRepositoryMock.updateItem(item)
        }
    }
}