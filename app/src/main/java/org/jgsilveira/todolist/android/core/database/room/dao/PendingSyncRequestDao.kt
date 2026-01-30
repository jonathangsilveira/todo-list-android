package org.jgsilveira.todolist.android.core.database.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import org.jgsilveira.todolist.android.core.database.room.entity.PendingSyncRequestEntity

@Dao
abstract class PendingSyncRequestDao {
    @Insert
    abstract suspend fun add(pendingSyncAction: PendingSyncRequestEntity): Long

    @Update
    abstract suspend fun update(pendingSyncAction: PendingSyncRequestEntity)

    @Query("DELETE FROM pending_sync_request WHERE id in (:id)")
    abstract suspend fun deleteById(vararg id: Long)

    @Query("SELECT * FROM pending_sync_request WHERE id = :id")
    abstract suspend fun findById(id: Long): PendingSyncRequestEntity?
}