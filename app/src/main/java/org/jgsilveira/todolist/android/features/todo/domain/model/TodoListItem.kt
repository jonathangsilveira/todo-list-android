package org.jgsilveira.todolist.android.features.todo.domain.model

import java.util.Date

data class TodoListItem(
    val uuid: String,
    val title: String,
    val status: TodoListItemStatus,
    val createdAt: Date,
    val isSynced: Boolean,
    val updatedAt: Date?,
    val lastSyncAt: Date?
)
