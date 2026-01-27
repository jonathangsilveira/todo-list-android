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
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.jgsilveira.todolist.android.features.todo.domain.repository.LocalTodoListRepository
import org.jgsilveira.todolist.android.features.todo.domain.usecase.RemoveItemByUuidUseCase
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
internal class RemoveItemByUuidUseCaseTest {
    private val testDispatcher = StandardTestDispatcher(
        scheduler = TestCoroutineScheduler()
    )

    private val localTodoListRepositoryMock = mockk<LocalTodoListRepository>()

    private val removeItemByUuidUseCase = RemoveItemByUuidUseCase(
        localTodoListRepository = localTodoListRepositoryMock,
        coroutineDispatcher = Dispatchers.Unconfined
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
        val uuid = "valid-uuid"
        coEvery {
            localTodoListRepositoryMock.removeItemByUuid(uuid)
        } just runs

        // When
        val result = removeItemByUuidUseCase(uuid)

        // Then
        assert(result.isSuccess)
        coVerify {
            localTodoListRepositoryMock.removeItemByUuid(uuid)
        }
    }

    @Test
    fun `invoke Should return Failure When repository fails`() = runTest {
        // Given
        val uuid = "invalid-uuid"
        coEvery {
            localTodoListRepositoryMock.removeItemByUuid(uuid)
        } throws Exception("something went wrong")

        // When
        val result = removeItemByUuidUseCase(uuid)

        // Then
        assert(result.isFailure)
        assertEquals(result.exceptionOrNull()?.message, "something went wrong")
        coVerify {
            localTodoListRepositoryMock.removeItemByUuid(uuid)
        }
    }
}