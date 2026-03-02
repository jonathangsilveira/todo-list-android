package org.jgsilveira.todolist.android.features.todo.presentation

import org.jgsilveira.todolist.android.core.presentation.model.ViewState

internal data class TodoListViewState(
    val todoListItems: List<TodoListItemViewData> = listOf()
): ViewState