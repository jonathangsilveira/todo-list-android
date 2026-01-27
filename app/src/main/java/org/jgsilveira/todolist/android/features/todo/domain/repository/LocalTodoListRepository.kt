package org.jgsilveira.todolist.android.features.todo.domain.repository

import org.jgsilveira.todolist.android.features.todo.domain.model.TodoListItem

interface LocalTodoListRepository {
    suspend fun addItem(item: TodoListItem)
    suspend fun updateItem(item: TodoListItem)
    suspend fun removeItemByUuid(uuid: String)
    suspend fun getActiveItems(): List<TodoListItem>
}