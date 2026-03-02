package org.jgsilveira.todolist.android.features.todo.presentation

import app.cash.turbine.test
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.jgsilveira.todolist.android.core.date.GetCurrentTimeInMillisUseCase
import org.jgsilveira.todolist.android.core.logging.AppLogger
import org.jgsilveira.todolist.android.coroutines.rules.MainDispatcherRule
import org.jgsilveira.todolist.android.features.todo.domain.usecase.AddItemUseCase
import org.jgsilveira.todolist.android.features.todo.domain.usecase.CreateItemUseCase
import org.jgsilveira.todolist.android.features.todo.domain.usecase.RemoveItemByUuidUseCase
import org.jgsilveira.todolist.android.features.todo.domain.usecase.StreamActiveItemsUseCase
import org.jgsilveira.todolist.android.features.todo.domain.usecase.UpdateItemUseCase
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

internal class TodoListViewModelTest {

    @get:Rule
    private val mainDispatcherRule = MainDispatcherRule()

    private val createItemMock = mockk<CreateItemUseCase>()

    private val addItemUseCaseMock = mockk<AddItemUseCase>()

    private val updateItemUseCaseMock = mockk<UpdateItemUseCase>()

    private val removeItemByUuidUseCaseMock = mockk<RemoveItemByUuidUseCase>()

    private val flowActiveItemsUseCaseMock = mockk<StreamActiveItemsUseCase> {
        every { this@mockk.invoke() } returns flowOf(listOf())
    }

    private val getCurrentTimeUseCaseMock = mockk<GetCurrentTimeInMillisUseCase> {
        every { this@mockk.invoke() } returns 0L
    }

    private val loggerMock = mockk<AppLogger> {
        coEvery { info(any()) } just runs
        coEvery { warn(any(), throwable = any()) } just runs
        coEvery { error(any(), throwable = any()) } just runs
        coEvery { debug(any(), throwable = any()) } just runs
    }

    @Test
    fun `init Should set state with empty items When flow emits empty list`() = runTest {
        // Given
        coEvery { flowActiveItemsUseCaseMock.invoke() } returns flowOf(emptyList())
        val initialState = TodoListViewState()

        // When
        val viewModel = createViewModel()

        // Then
        assertEquals(
            expected = initialState,
            actual = viewModel.viewState.value
        )
        coVerify { flowActiveItemsUseCaseMock.invoke() }
    }

    @Test
    fun `init Should set state with items When flow emits non empty list`() = runTest {
        // Given
        val items = listOf(TodoListViewModelStubs.pendingItem, TodoListViewModelStubs.doneItem)
        every { flowActiveItemsUseCaseMock.invoke() } returns flow {
            emit(listOf())
            emit(items)
        }
        val initialState = TodoListViewState()
        val nonEmptyState = TodoListViewState(
            todoListItems = items.map { it.toViewData() }
        )

        // When
        val viewModel = createViewModel()

        // Then
        viewModel.viewState.test {
            assertEquals(
                expected = initialState,
                actual = awaitItem()
            )
            assertEquals(
                expected = nonEmptyState,
                actual = awaitItem()
            )
            cancelAndIgnoreRemainingEvents()
        }
        verify { flowActiveItemsUseCaseMock.invoke() }
    }

    @Test
    fun `init Should log error When flow throws an Exception`() = runTest {
        // Given
        val exception = Exception("something went wrong")
        every { flowActiveItemsUseCaseMock.invoke() } returns flow {
            throw exception
        }
        every { loggerMock.warn(message = "Error fetching items", throwable = exception) } just runs

        // When
        val viewModel = createViewModel()

        // Then
        viewModel.viewState.test {
            assertEquals(expected = TodoListViewState(), actual = awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
        verify {
            flowActiveItemsUseCaseMock.invoke()
            loggerMock.warn(message = "Error fetching items", throwable = exception)
        }
    }

    @Test
    fun `updateItem Should update item When use case succeeds`() = runTest {
        // Given
        val viewData = TodoListItemViewData(
            id = "id",
            text = "",
            isDone = false,
            createdAt = 0L,
            updatedAt = null,
            lastSyncAt = null
        )
        val item = TodoListViewModelStubs.pendingItem
        coEvery { updateItemUseCaseMock.invoke(item) } returns Result.success(Unit)
        val viewModel = createViewModel()

        // When
        viewModel.updateItem(viewData = viewData, title = "title")

        // Then
        coVerify {
            getCurrentTimeUseCaseMock.invoke()
            updateItemUseCaseMock.invoke(item)
        }
    }

    @Test
    fun `updateItem Should log error When use case fails`() = runTest {
        // Given
        val viewData = TodoListItemViewData(
            id = "id",
            text = "",
            isDone = false,
            createdAt = 0L,
            updatedAt = null,
            lastSyncAt = null
        )
        val item = TodoListViewModelStubs.pendingItem
        val error = Exception("something went wrong")
        coEvery { updateItemUseCaseMock.invoke(item) } returns Result.failure(error)
        val viewModel = createViewModel()

        // When
        viewModel.updateItem(viewData = viewData, title = "title")

        // Then
        coVerify {
            getCurrentTimeUseCaseMock.invoke()
            updateItemUseCaseMock.invoke(item)
            loggerMock.error(
                message = "Error updating item id",
                throwable = error
            )
        }
    }

    @Test
    fun `checkItem Should update checked item When use case succeeds`() = runTest {
        // Given
        val viewData = TodoListItemViewData(
            id = "id",
            text = "",
            isDone = false,
            createdAt = 0L,
            updatedAt = null,
            lastSyncAt = null
        )
        val item = viewData.copy(
            isDone = true,
            updatedAt = 0L
        ).toDomain()
        coEvery { updateItemUseCaseMock.invoke(item) } returns Result.success(Unit)
        val viewModel = createViewModel()

        // When
        viewModel.checkItem(viewData = viewData, isChecked = true)

        // Then
        coVerify {
            getCurrentTimeUseCaseMock.invoke()
            updateItemUseCaseMock.invoke(item)
        }
    }

    @Test
    fun `checkItem Should log error When use case fails`() = runTest {
        // Given
        val viewData = TodoListItemViewData(
            id = "id",
            text = "",
            isDone = true,
            createdAt = 0L,
            updatedAt = null,
            lastSyncAt = null
        )
        val item = viewData.copy(
            isDone = false,
            updatedAt = 0L
        ).toDomain()
        val error = Exception("something went wrong")
        coEvery { updateItemUseCaseMock.invoke(item) } returns Result.failure(error)
        val viewModel = createViewModel()

        // When
        viewModel.checkItem(viewData = viewData, isChecked = false)

        // Then
        coVerify {
            getCurrentTimeUseCaseMock.invoke()
            updateItemUseCaseMock.invoke(item)
            loggerMock.error(
                message = "Error updating item id",
                throwable = error
            )
        }
    }

    @Test
    fun `removeItem Should remove item by uuid When use case succeeds`() = runTest {
        // Given
        val viewData = TodoListItemViewData(
            id = "id",
            text = "",
            isDone = false,
            createdAt = 0L,
            updatedAt = null,
            lastSyncAt = null
        )
        val item = viewData.toDomain()
        coEvery { removeItemByUuidUseCaseMock.invoke(item) } returns Result.success(Unit)
        val viewModel = createViewModel()

        // When
        viewModel.removeItem(viewData = viewData)

        // Then
        coVerify {
            removeItemByUuidUseCaseMock.invoke(item)
        }
    }

    @Test
    fun `removeItem Should log error When use case fails`() = runTest {
        // Given
        val viewData = TodoListItemViewData(
            id = "id",
            text = "",
            isDone = false,
            createdAt = 0L,
            updatedAt = null,
            lastSyncAt = null
        )
        val item = viewData.toDomain()
        val error = Exception("something went wrong")
        coEvery { removeItemByUuidUseCaseMock.invoke(item) } returns Result.failure(error)
        val viewModel = createViewModel()

        // When
        viewModel.removeItem(viewData = viewData)

        // Then
        coVerify {
            removeItemByUuidUseCaseMock.invoke(item)
            loggerMock.error(
                message = "Error removing item id",
                throwable = error
            )
        }
    }

    @Test
    fun `addItem Should add item When use case succeeds`() = runTest {
        val item = TodoListViewModelStubs.addedItem
        every { createItemMock.invoke() } returns item
        coEvery { addItemUseCaseMock.invoke(item) } returns Result.success(Unit)
        val viewModel = createViewModel()

        viewModel.addItem()

        coVerify {
            createItemMock.invoke()
            addItemUseCaseMock.invoke(item)
        }
    }

    @Test
    fun `addItem Should log error When use case fails`() = runTest {
        val item = TodoListViewModelStubs.addedItem
        val error = Exception("something went wrong")
        every { createItemMock.invoke() } returns item
        coEvery { addItemUseCaseMock.invoke(item) } returns Result.failure(error)
        val viewModel = createViewModel()

        viewModel.addItem()

        coVerify {
            createItemMock.invoke()
            addItemUseCaseMock.invoke(item)
            loggerMock.error(
                message = "Error adding item",
                throwable = error
            )
        }
    }

    private fun createViewModel(): TodoListViewModel {
        return TodoListViewModel(
            createItem = createItemMock,
            addItem = addItemUseCaseMock,
            updateItem = updateItemUseCaseMock,
            removeItemByUuid = removeItemByUuidUseCaseMock,
            flowActiveItems = flowActiveItemsUseCaseMock,
            getCurrentTimeInMillis = getCurrentTimeUseCaseMock,
            logger = loggerMock
        )
    }
}