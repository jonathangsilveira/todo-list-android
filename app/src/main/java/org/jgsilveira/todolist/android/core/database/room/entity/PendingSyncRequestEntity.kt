package org.jgsilveira.todolist.android.core.database.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pending_sync_request")
data class PendingSyncRequestEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("id") val id: Long? = null,
    @ColumnInfo("action_type") val actionType: String,
    @ColumnInfo("entity_id") val entityId: String,
    @ColumnInfo("retry_count") val retryCount: Int,
    @ColumnInfo("created_at") val createdAt: Long,
    @ColumnInfo("updated_at") val updatedAt: Long?,
    @ColumnInfo("payload", defaultValue = "NULL") val payload: String?
)
