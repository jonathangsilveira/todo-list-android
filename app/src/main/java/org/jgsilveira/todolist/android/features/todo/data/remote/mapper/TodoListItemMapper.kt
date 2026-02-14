package org.jgsilveira.todolist.android.features.todo.data.remote.mapper

import org.jgsilveira.todolist.android.features.todo.data.remote.model.RemoteTodoListItem
import org.jgsilveira.todolist.android.features.todo.data.remote.model.TodoListItemBody
import org.jgsilveira.todolist.android.features.todo.domain.model.TodoListItem
import org.jgsilveira.todolist.android.features.todo.domain.model.TodoListItemStatus
import java.util.Date

internal fun TodoListItem.toBody(): TodoListItemBody {
    return TodoListItemBody(
        uuid = uuid,
        title = title,
        status = status.name,
        createdAt = createdAt.time,
        updatedAt = updatedAt?.time,
        lastSyncAt = lastSyncAt?.time
    )
}

internal fun RemoteTodoListItem.toDomain(): TodoListItem {
    return TodoListItem(
        uuid = uuid.orEmpty(),
        title = title.orEmpty(),
        status = TodoListItemStatus.valueOf(status.orEmpty()),
        createdAt = Date(createdAt ?: 0L),
        updatedAt = updatedAt?.let { Date(it) },
        lastSyncAt = lastSyncAt?.let { Date(it) },
        isSynced = true
    )
}