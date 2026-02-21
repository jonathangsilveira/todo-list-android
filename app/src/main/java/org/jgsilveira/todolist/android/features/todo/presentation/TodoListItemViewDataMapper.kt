package org.jgsilveira.todolist.android.features.todo.presentation

import org.jgsilveira.todolist.android.features.todo.domain.model.TodoListItem
import org.jgsilveira.todolist.android.features.todo.domain.model.TodoListItemStatus
import java.util.Date

internal fun TodoListItemViewData.toDomain(): TodoListItem {
    return TodoListItem(
        uuid = id,
        title = text,
        status = if (isDone) TodoListItemStatus.DONE else TodoListItemStatus.PENDING,
        isSynced = false,
        createdAt = Date(createdAt),
        updatedAt = updatedAt?.let { Date(it) },
        lastSyncAt = lastSyncAt?.let { Date(it) }
    )
}

internal fun TodoListItem.toViewData(): TodoListItemViewData {
    return TodoListItemViewData(
        id = uuid,
        text = title,
        isDone = status == TodoListItemStatus.DONE,
        createdAt = createdAt.time,
        updatedAt = updatedAt?.time,
        lastSyncAt = lastSyncAt?.time
    )
}