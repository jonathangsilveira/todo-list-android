package org.jgsilveira.todolist.android.core.database.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import org.jgsilveira.todolist.android.core.database.room.dao.RemoteSyncRequestDao
import org.jgsilveira.todolist.android.core.database.room.dao.TodoListItemDao
import org.jgsilveira.todolist.android.core.database.room.entity.RemoteSyncRequestEntity
import org.jgsilveira.todolist.android.core.database.room.entity.TodoListItemEntity

@Database(
    version = 1,
    entities = [
        TodoListItemEntity::class,
        RemoteSyncRequestEntity::class
    ]
)
abstract class RoomAppDatabase : RoomDatabase() {
    abstract fun todoListItemDao(): TodoListItemDao
    abstract fun remoteSyncRequestDao(): RemoteSyncRequestDao

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