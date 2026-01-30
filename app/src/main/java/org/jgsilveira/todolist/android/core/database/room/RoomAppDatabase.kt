package org.jgsilveira.todolist.android.core.database.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import org.jgsilveira.todolist.android.core.database.room.dao.PendingSyncRequestDao
import org.jgsilveira.todolist.android.core.database.room.dao.TodoListItemDao
import org.jgsilveira.todolist.android.core.database.room.entity.PendingSyncRequestEntity
import org.jgsilveira.todolist.android.core.database.room.entity.TodoListItemEntity

@Database(entities = [TodoListItemEntity::class, PendingSyncRequestEntity::class], version = 1)
abstract class RoomAppDatabase : RoomDatabase() {
    abstract fun todoListItemDao(): TodoListItemDao
    abstract fun pendingSyncRequestDao(): PendingSyncRequestDao

    companion object {
        fun from(appContext: Context, databaseName: String): RoomAppDatabase {
            val databaseBuilder = Room.databaseBuilder(
                context = appContext,
                klass = RoomAppDatabase::class.java,
                name = databaseName
            )
            return databaseBuilder
                .fallbackToDestructiveMigration(dropAllTables = true)
                .build()
        }
    }
}