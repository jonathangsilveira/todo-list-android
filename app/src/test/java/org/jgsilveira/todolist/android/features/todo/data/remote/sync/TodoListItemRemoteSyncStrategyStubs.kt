package org.jgsilveira.todolist.android.features.todo.data.remote.sync

import org.jgsilveira.todolist.android.features.todo.data.remote.model.RemoteTodoListItem
import org.jgsilveira.todolist.android.features.todo.data.remote.model.TodoListItemBody
import org.jgsilveira.todolist.android.features.todo.domain.model.TodoListItemStatus

internal object TodoListItemRemoteSyncStrategyStubs {

    val addItemBody = TodoListItemBody(
        uuid = "uuid",
        title = "",
        status = TodoListItemStatus.PENDING.name,
        createdAt = 0L,
        updatedAt = null,
        lastSyncAt = null
    )

    val addItemResponse = RemoteTodoListItem(
        uuid = "uuid",
        title = "",
        status = TodoListItemStatus.PENDING.name,
        createdAt = 0L,
        updatedAt = null,
        lastSyncAt = 1L
    )

    val updateItemBody = addItemBody.copy(
        title = "Task",
        updatedAt = 2L,
        lastSyncAt = 3L
    )

    val updateItemResponse = RemoteTodoListItem(
        uuid = "uuid",
        title = "Task",
        status = TodoListItemStatus.PENDING.name,
        createdAt = 0L,
        updatedAt = 2L,
        lastSyncAt = 3L
    )
}