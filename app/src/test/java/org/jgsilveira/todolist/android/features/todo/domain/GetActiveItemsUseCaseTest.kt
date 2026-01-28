package org.jgsilveira.todolist.android.features.todo.domain

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.jgsilveira.todolist.android.features.todo.domain.repository.LocalTodoListRepository
import org.jgsilveira.todolist.android.features.todo.domain.usecase.GetActiveItemsUseCase
import org.junit.After
import org.junit.Before
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class GetActiveItemsUseCaseTest {
    private val testDispatcher = StandardTestDispatcher(
        scheduler = TestCoroutineScheduler()
    )

    private val localTodoListRepositoryMock = mockk<LocalTodoListRepository>()

    private val getActiveItemsUseCase = GetActiveItemsUseCase(
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
    fun `invoke Should return non empty list When repository succeeds`() = runTest {
        // Given
        coEvery {
            localTodoListRepositoryMock.getActiveItems()
        } returns TodoListItemStubs.activeTodoListItems

        // When
        val result = getActiveItemsUseCase()

        // Then
        assert(result.isSuccess)
        assertEquals(false, result.getOrNull()?.isEmpty())
        coVerify {
            localTodoListRepositoryMock.getActiveItems()
        }
    }

    @Test
    fun `invoke Should return empty list When repository there are no active items`() = runTest {
        // Given
        coEvery {
            localTodoListRepositoryMock.getActiveItems()
        } returns emptyList()

        // When
        val result = getActiveItemsUseCase()

        // Then
        assert(result.isSuccess)
        assertEquals(true, result.getOrNull()?.isEmpty())
        coVerify {
            localTodoListRepositoryMock.getActiveItems()
        }
    }

    @Test
    fun `invoke Should return Failure When repository fails`() = runTest {
        // Given
        coEvery {
            localTodoListRepositoryMock.getActiveItems()
        } throws Exception("There are no active items")

        // When
        val result = getActiveItemsUseCase()

        // Then
        assert(result.isFailure)
        assertEquals(
            expected = "There are no active items",
            actual = result.exceptionOrNull()?.message
        )
        coVerify {
            localTodoListRepositoryMock.getActiveItems()
        }
    }
}