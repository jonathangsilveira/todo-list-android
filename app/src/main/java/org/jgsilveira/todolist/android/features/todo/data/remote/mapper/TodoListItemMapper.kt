package org.jgsilveira.todolist.android.features.todo.data.remote.mapper

import org.jgsilveira.todolist.android.features.todo.data.remote.model.RemoteTodoListItem
import org.jgsilveira.todolist.android.features.todo.data.remote.model.TodoListItemBody
import org.jgsilveira.todolist.android.features.todo.domain.model.TodoListItem
import org.jgsilveira.todolist.android.features.todo.domain.model.TodoListItemStatus

internal fun TodoListItem.toBody(): TodoListItemBody {
    return TodoListItemBody(
        uuid = uuid,
        title = title,
        status = status.name,
        createdAt = createdAt,
        updatedAt = updatedAt,
        lastSyncAt = lastSyncAt
    )
}

internal fun RemoteTodoListItem.toDomain(): TodoListItem {
    return TodoListItem(
        uuid = uuid.orEmpty(),
        title = title.orEmpty(),
        status = TodoListItemStatus.valueOf(status.orEmpty()),
        createdAt = createdAt ?: 0L,
        updatedAt = updatedAt,
        lastSyncAt = lastSyncAt,
        isSynced = true
    )
}