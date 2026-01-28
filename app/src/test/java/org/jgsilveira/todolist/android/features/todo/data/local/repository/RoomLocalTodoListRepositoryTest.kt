package org.jgsilveira.todolist.android.features.todo.data.local.repository

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.io.IOException
import org.jgsilveira.todolist.android.core.database.room.dao.TodoListItemDao
import org.jgsilveira.todolist.android.features.todo.data.local.mapper.toEntity
import org.jgsilveira.todolist.android.features.todo.domain.TodoListItemStubs
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

@OptIn(ExperimentalCoroutinesApi::class)
internal class RoomLocalTodoListRepositoryTest {
    private val testDispatcher = StandardTestDispatcher(
        scheduler = TestCoroutineScheduler()
    )

    private val daoMock = mockk<TodoListItemDao> {
        coEvery { addItem(any()) } just runs
        coEvery { updateItem(any()) } just runs
        coEvery { deleteItemByUuid(any()) } just runs
    }

    private val repository = RoomLocalTodoListRepository(dao = daoMock)

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `addItem Should run successfully When dao succeeds`() = runTest {
        // Given
        val item = TodoListItemStubs.pendingTodoListItem
        val entity = item.toEntity()

        // When
        repository.addItem(item)

        // Then
        coVerify {
            daoMock.addItem(entity)
        }
    }

    @Test
    fun `addItem Should throw Exception When dao fails`() = runTest {
        // Given
        val item = TodoListItemStubs.pendingTodoListItem
        val entity = item.toEntity()
        coEvery { daoMock.addItem(entity) } throws IOException("Error writing on db")

        // When
        val exception = assertFailsWith<IOException> { repository.addItem(item) }

        // Then
        assertEquals("Error writing on db", exception.message)
        coVerify {
            daoMock.addItem(entity)
        }
    }

    @Test
    fun `updateItem Should run successfully When dao succeeds`() = runTest {
        // Given
        val item = TodoListItemStubs.doneTodoListItem
        val entity = item.toEntity()

        // When
        repository.updateItem(item)

        // Then
        coVerify {
            daoMock.updateItem(entity)
        }
    }

    @Test
    fun `updateItem Should throw Exception When dao fails`() = runTest {
        // Given
        val item = TodoListItemStubs.doneTodoListItem
        val entity = item.toEntity()
        coEvery { daoMock.updateItem(entity) } throws IOException("Error writing on db")

        // When
        val exception = assertFailsWith<IOException> { repository.updateItem(item) }

        // Then
        assertEquals("Error writing on db", exception.message)
        coVerify {
            daoMock.updateItem(entity)
        }
    }

    @Test
    fun `removeItemByUuid Should run successfully When dao succeeds`() = runTest {
        // Given
        val uuid = "valid-uuid"

        // When
        repository.removeItemByUuid(uuid)

        // Then
        coVerify {
            daoMock.deleteItemByUuid(uuid)
        }
    }

    @Test
    fun `removeItemByUuid Should throw Exception When dao fails`() = runTest {
        // Given
        val uuid = "invalid-uuid"
        coEvery { daoMock.deleteItemByUuid(uuid) } throws IOException("Error writing on db")

        // When
        val exception = assertFailsWith<IOException> { repository.removeItemByUuid(uuid) }

        // Then
        assertEquals("Error writing on db", exception.message)
        coVerify {
            daoMock.deleteItemByUuid(uuid)
        }
    }

    @Test
    fun `getActiveItems Should return empty list When there are no active items`() = runTest {
        // Given
        val statuses = listOf("PENDING", "DONE")
        coEvery { daoMock.getItemsByStatus(statuses) } returns emptyList()

        // When
        val activeItems = repository.getActiveItems()

        // Then
        assert(activeItems.isEmpty())
        coVerify {
            daoMock.getItemsByStatus(statuses)
        }
    }

    @Test
    fun `getActiveItems Should return non empty list When there are active items`() = runTest {
        // Given
        val statuses = listOf("PENDING", "DONE")
        val expectedActiveItems = TodoListItemStubs.activeTodoListItems.map {
            it.toEntity()
        }
        coEvery { daoMock.getItemsByStatus(statuses) } returns expectedActiveItems

        // When
        val activeItems = repository.getActiveItems()

        // Then
        assert(activeItems.isNotEmpty())
        coVerify {
            daoMock.getItemsByStatus(statuses)
        }
    }

    @Test
    fun `getActiveItems Should throws an exception When dao fails`() = runTest {
        // Given
        val statuses = listOf("PENDING", "DONE")
        coEvery { daoMock.getItemsByStatus(statuses) } throws IOException("Error fetching on db")

        // When
        val exception = assertFailsWith<IOException> { repository.getActiveItems() }

        // Then
        assertEquals("Error fetching on db", exception.message)
        coVerify {
            daoMock.getItemsByStatus(statuses)
        }
    }

    @Test
    fun `flowActiveItems Should emit empty list When there are no active items`() = runTest {
        // Given
        val statuses = listOf("PENDING", "DONE")
        coEvery { daoMock.flowItemsByStatus(statuses) } returns flowOf(emptyList())

        // When
        val emittedActiveItems = repository.flowActiveItems().toList()

        // Then
        assert(emittedActiveItems.isNotEmpty())
        coVerify {
            @Suppress("UnusedFlow")
            daoMock.flowItemsByStatus(statuses)
        }
    }

    @Test
    fun `flowActiveItems Should non empty list When there are active items`() = runTest {
        // Given
        val statuses = listOf("PENDING", "DONE")
        val entities = TodoListItemStubs.activeTodoListItems.map {
            it.toEntity()
        }
        coEvery { daoMock.flowItemsByStatus(statuses) } returns flowOf(entities)

        // When
        val emittedActiveItems = repository.flowActiveItems().toList()

        // Then
        assert(emittedActiveItems.isNotEmpty())
        assertEquals(TodoListItemStubs.activeTodoListItems, emittedActiveItems.firstOrNull())
        coVerify {
            @Suppress("UnusedFlow")
            daoMock.flowItemsByStatus(statuses)
        }
    }
}