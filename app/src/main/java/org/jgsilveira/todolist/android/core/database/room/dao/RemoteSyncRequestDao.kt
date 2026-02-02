package org.jgsilveira.todolist.android.core.database.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import org.jgsilveira.todolist.android.core.database.room.entity.RemoteSyncRequestEntity

@Dao
abstract class RemoteSyncRequestDao {
    @Insert
    abstract suspend fun add(remoteSyncRequest: RemoteSyncRequestEntity): Long

    @Update
    abstract suspend fun update(remoteSyncRequest: RemoteSyncRequestEntity)

    @Query("DELETE FROM remote_sync_request WHERE id in (:id)")
    abstract suspend fun deleteById(vararg id: Long)

    @Query("SELECT * FROM remote_sync_request WHERE id = :id")
    abstract suspend fun findById(id: Long): RemoteSyncRequestEntity?
}