package org.jgsilveira.todolist.android.features.todo.presentation

import androidx.compose.runtime.Immutable

@ConsistentCopyVisibility
@Immutable
data class TodoListItemViewData internal constructor(
    val id: String,
    val text: String,
    val isDone: Boolean,
    val createdAt: Long,
    val updatedAt: Long?,
    val lastSyncAt: Long?
)
