package org.jgsilveira.todolist.android.features.todo.domain

import org.jgsilveira.todolist.android.features.todo.domain.model.TodoListItem
import org.jgsilveira.todolist.android.features.todo.domain.model.TodoListItemStatus

internal object TodoListItemStubs {

    val pendingTodoListItem = TodoListItem(
        uuid = "uuid",
        title = "Water the plants",
        status = TodoListItemStatus.PENDING,
        isSynced = false,
        createdAt = 0L,
        updatedAt = null,
        lastSyncAt = null
    )

    val doneTodoListItem = TodoListItem(
        uuid = "uuid",
        title = "Water the plants",
        status = TodoListItemStatus.DONE,
        isSynced = false,
        createdAt = 0L,
        updatedAt = 0L,
        lastSyncAt = null
    )

    val activeTodoListItems = listOf(pendingTodoListItem)
}