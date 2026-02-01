package org.jgsilveira.todolist.android.core.database.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_sync_request")
data class RemoteSyncRequestEntity(
    @ColumnInfo("frequency") val frequency: String,
    @ColumnInfo("strategy_name") val strategyName: String,
    @ColumnInfo("entity_id") val entityId: String,
    @ColumnInfo("attempt_count") val attempts: Int = 0,
    @PrimaryKey(autoGenerate = true) @ColumnInfo("id") val id: Long? = null,
    @ColumnInfo("payload", defaultValue = "NULL") val payload: String? = null
)
