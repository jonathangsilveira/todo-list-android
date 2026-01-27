package org.jgsilveira.todolist.android.core.database.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import org.jgsilveira.todolist.android.core.database.room.entity.TodoListItemEntity

@Dao
abstract class TodoListItemDao {
    @Query("SELECT * FROM todo_list_item ORDER BY status IN (:statuses) ASC, created_at ASC")
    abstract suspend fun getActiveItems(statuses: List<String>): List<TodoListItemEntity>

    @Insert
    abstract suspend fun addItem(item: TodoListItemEntity)

    @Update(onConflict = OnConflictStrategy.Companion.REPLACE)
    abstract suspend fun updateItem(item: TodoListItemEntity)

    @Query("DELETE FROM todo_list_item WHERE uuid in (:uuid)")
    abstract suspend fun deleteItemByUuid(vararg uuid: String)

    @Query("SELECT * FROM todo_list_item ORDER BY status IN (:statuses) ASC, created_at ASC")
    abstract fun flowActiveItems(statuses: List<String>): Flow<List<TodoListItemEntity>>
}