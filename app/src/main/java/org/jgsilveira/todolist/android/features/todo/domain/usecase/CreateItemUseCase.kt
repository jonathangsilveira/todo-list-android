package org.jgsilveira.todolist.android.features.todo.domain.usecase

import org.jgsilveira.todolist.android.core.date.GetCurrentTimeInMillisUseCase
import org.jgsilveira.todolist.android.features.todo.domain.model.TodoListItem
import org.jgsilveira.todolist.android.features.todo.domain.model.TodoListItemStatus

class CreateItemUseCase(
    private val generateItemUuid: GenerateItemUuidUseCase,
    private val getCurrentTimeInMillis: GetCurrentTimeInMillisUseCase
) {

    operator fun invoke(
        title: String = "",
        status: TodoListItemStatus = TodoListItemStatus.PENDING,
        uuid: String? = null
        ): TodoListItem {
        return TodoListItem(
            uuid = uuid ?: generateItemUuid(),
            title = title,
            status = status,
            isSynced = false,
            createdAt = getCurrentTimeInMillis(),
            updatedAt = null,
            lastSyncAt = null
        )
    }
}