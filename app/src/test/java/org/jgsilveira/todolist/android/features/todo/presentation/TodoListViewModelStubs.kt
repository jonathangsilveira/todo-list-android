package org.jgsilveira.todolist.android.features.todo.presentation

import org.jgsilveira.todolist.android.features.todo.domain.model.TodoListItem
import org.jgsilveira.todolist.android.features.todo.domain.model.TodoListItemStatus

internal object TodoListViewModelStubs {

    val pendingItem = TodoListItem(
        uuid = "id",
        title = "title",
        status = TodoListItemStatus.PENDING,
        createdAt = 0L,
        isSynced = false,
        updatedAt = 0L,
        lastSyncAt = null
    )

    val doneItem = TodoListItem(
        uuid = "id",
        title = "title",
        status = TodoListItemStatus.PENDING,
        createdAt = 0L,
        isSynced = true,
        updatedAt = 1L,
        lastSyncAt = 2L
    )

    val addedItem = TodoListItem(
        uuid = "id",
        title = "title",
        status = TodoListItemStatus.PENDING,
        createdAt = 0L,
        isSynced = false,
        updatedAt = null,
        lastSyncAt = null
    )
}