package org.jgsilveira.todolist.android.features.todo.domain.usecase

import io.mockk.every
import io.mockk.mockk
import org.jgsilveira.todolist.android.core.date.GetCurrentTimeInMillisUseCase
import org.jgsilveira.todolist.android.features.todo.domain.model.TodoListItem
import org.jgsilveira.todolist.android.features.todo.domain.model.TodoListItemStatus
import org.junit.Test
import kotlin.test.assertEquals

internal class CreateItemUseCaseTest {

    private val generateItemUuidMock = mockk<GenerateItemUuidUseCase>()

    private val getCurrentTimeInMillisMock = mockk<GetCurrentTimeInMillisUseCase>()

    private val useCase = CreateItemUseCase(generateItemUuidMock, getCurrentTimeInMillisMock)

    @Test
    fun `invoke Should return a TodoListItem When called with default values`() {
        // Given
        every { generateItemUuidMock.invoke() } returns "uuid"
        every { getCurrentTimeInMillisMock.invoke() } returns 1L
        val expected = TodoListItem(
            uuid = "uuid",
            title = "",
            status = TodoListItemStatus.PENDING,
            isSynced = false,
            createdAt = 1L,
            updatedAt = null,
            lastSyncAt = null
        )

        // When
        val actual = useCase.invoke()

        // Then
        assertEquals(expected, actual)
    }

    @Test
    fun `invoke Should return a TodoListItem When called with values passed as parameters`() {
        // Given
        every { generateItemUuidMock.invoke() } returns "uuid"
        every { getCurrentTimeInMillisMock.invoke() } returns 1L
        val expected = TodoListItem(
            uuid = "hardcoded-uuid",
            title = "chablau",
            status = TodoListItemStatus.DONE,
            isSynced = false,
            createdAt = 1L,
            updatedAt = null,
            lastSyncAt = null
        )

        // When
        val actual = useCase.invoke(
            uuid = "hardcoded-uuid",
            title = "chablau",
            status = TodoListItemStatus.DONE
        )

        // Then
        assertEquals(expected, actual)
    }
}