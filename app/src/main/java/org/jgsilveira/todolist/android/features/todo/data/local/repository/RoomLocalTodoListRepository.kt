package org.jgsilveira.todolist.android.features.todo.data.local.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.jgsilveira.todolist.android.core.database.room.dao.TodoListItemDao
import org.jgsilveira.todolist.android.features.todo.data.local.mapper.toDomain
import org.jgsilveira.todolist.android.features.todo.data.local.mapper.toEntity
import org.jgsilveira.todolist.android.features.todo.domain.model.TodoListItem
import org.jgsilveira.todolist.android.features.todo.domain.model.TodoListItemStatus
import org.jgsilveira.todolist.android.features.todo.domain.repository.LocalTodoListRepository

internal class RoomLocalTodoListRepository(
    private val dao: TodoListItemDao
): LocalTodoListRepository {
    override suspend fun addItem(item: TodoListItem) {
        val entity = item.toEntity()
        dao.addItem(entity)
    }

    override suspend fun updateItem(item: TodoListItem) {
        val entity = item.toEntity()
        dao.updateItem(entity)
    }

    override suspend fun removeItemByUuid(uuid: String) {
        dao.deleteItemByUuid(uuid)
    }

    override suspend fun getActiveItems(): List<TodoListItem> {
        val statuses = listOf(
            TodoListItemStatus.PENDING.name,
            TodoListItemStatus.DONE.name
        )
        return dao.getItemsByStatus(statuses = statuses)
            .map { it.toDomain() }
    }

    override fun flowActiveItems(): Flow<List<TodoListItem>> {
        val statuses = listOf(
            TodoListItemStatus.PENDING.name,
            TodoListItemStatus.DONE.name
        )
        return dao.flowItemsByStatus(statuses).map { entities ->
            entities.map { entity -> entity.toDomain() }
        }
    }
}