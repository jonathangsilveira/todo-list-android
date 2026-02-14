package org.jgsilveira.todolist.android.features.todo.data.remote.client

import org.jgsilveira.todolist.android.features.todo.data.remote.model.RemoteTodoListItem
import org.jgsilveira.todolist.android.features.todo.data.remote.model.TodoListItemBody

internal interface TodoListClient {
    suspend fun addItem(body: TodoListItemBody): RemoteTodoListItem
    suspend fun updateItem(body: TodoListItemBody): RemoteTodoListItem
    suspend fun removeItemByUuid(uuid: String)
    suspend fun getActiveItems(): List<RemoteTodoListItem>
}