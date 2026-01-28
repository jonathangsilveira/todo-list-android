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
import org.jgsilveira.todolist.android.features.todo.domain.repository.LocalTodoListRepository
import org.jgsilveira.todolist.android.features.todo.domain.usecase.RemoveItemByUuidUseCase
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
internal class RemoveItemByUuidUseCaseTest {
    @get:Rule
    private val mainDispatcherRule = MainDispatcherRule()

    private val localTodoListRepositoryMock = mockk<LocalTodoListRepository>()

    private val removeItemByUuidUseCase = RemoveItemByUuidUseCase(
        localTodoListRepository = localTodoListRepositoryMock,
        coroutineDispatcher = UnconfinedTestDispatcher()
    )

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
        assertEquals("something went wrong", result.exceptionOrNull()?.message)
        coVerify {
            localTodoListRepositoryMock.removeItemByUuid(uuid)
        }
    }
}