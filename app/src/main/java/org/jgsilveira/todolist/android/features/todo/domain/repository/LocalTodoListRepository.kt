package org.jgsilveira.todolist.android.features.todo.domain.repository

import kotlinx.coroutines.flow.Flow
import org.jgsilveira.todolist.android.features.todo.domain.model.TodoListItem

interface LocalTodoListRepository {
    suspend fun addItem(item: TodoListItem)
    suspend fun updateItem(item: TodoListItem)
    suspend fun removeItemByUuid(uuid: String)
    suspend fun getActiveItems(): List<TodoListItem>
    fun flowActiveItems(): Flow<List<TodoListItem>>
}