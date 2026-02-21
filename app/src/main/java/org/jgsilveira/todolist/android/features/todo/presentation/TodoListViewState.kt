package org.jgsilveira.todolist.android.features.todo.presentation

internal data class TodoListViewState(
    val todoListItems: List<TodoListItemViewData> = listOf()
)