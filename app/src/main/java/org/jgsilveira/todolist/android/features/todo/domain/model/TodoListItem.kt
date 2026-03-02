package org.jgsilveira.todolist.android.features.todo.domain.model

data class TodoListItem(
    val uuid: String,
    val title: String,
    val status: TodoListItemStatus,
    val createdAt: Long,
    val isSynced: Boolean,
    val updatedAt: Long?,
    val lastSyncAt: Long?
)
