package org.jgsilveira.todolist.android.features.todo.data.local.mapper

import org.jgsilveira.todolist.android.core.database.room.entity.TodoListItemEntity
import org.jgsilveira.todolist.android.features.todo.domain.model.TodoListItem
import org.jgsilveira.todolist.android.features.todo.domain.model.TodoListItemStatus
import java.util.Date

private const val SYNCED = 1
private const val NOT_SYNCED = 0

internal fun TodoListItemEntity.toDomain(): TodoListItem {
    return TodoListItem(
        uuid = uuid,
        title = title,
        status = TodoListItemStatus.valueOf(status),
        isSynced = isSynced > NOT_SYNCED,
        createdAt = Date(createdAtMillis),
        updatedAt = updatedAtMillis?.let { Date(it) },
        lastSyncAt = lastSyncAtMillis?.let { Date(it) }
    )
}

internal fun TodoListItem.toEntity(): TodoListItemEntity {
    return TodoListItemEntity(
        uuid = uuid,
        title = title,
        status = status.name,
        isSynced = if (isSynced) SYNCED else NOT_SYNCED,
        createdAtMillis = createdAt.time,
        updatedAtMillis = updatedAt?.time,
        lastSyncAtMillis = lastSyncAt?.time
    )
}