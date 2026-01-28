package org.jgsilveira.todolist.android.core.database.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todo_list_item")
data class TodoListItemEntity(
    @PrimaryKey @ColumnInfo(name = "uuid") val uuid: String,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "status") val status: String,
    @ColumnInfo(name = "is_synced") val isSynced: Int,
    @ColumnInfo(name = "created_at") val createdAtMillis: Long,
    @ColumnInfo(name = "updated_at", defaultValue = "NULL") val updatedAtMillis: Long?,
    @ColumnInfo(name = "last_sync_at", defaultValue = "NULL") val lastSyncAtMillis: Long?
)