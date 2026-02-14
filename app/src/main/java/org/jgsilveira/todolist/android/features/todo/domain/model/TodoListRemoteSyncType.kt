package org.jgsilveira.todolist.android.features.todo.domain.model

enum class TodoListRemoteSyncType(val value: String) {
    ADD_ITEM("TODO_LIST_ITEM_ADDED"),
    UPDATE_ITEM("TODO_LIST_ITEM_UPDATED"),
    REMOVE_ITEM("TODO_LIST_ITEM_REMOVED")
}