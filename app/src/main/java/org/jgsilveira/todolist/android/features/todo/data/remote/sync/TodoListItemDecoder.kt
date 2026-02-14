package org.jgsilveira.todolist.android.features.todo.data.remote.sync

import kotlinx.serialization.json.Json
import org.jgsilveira.todolist.android.features.todo.data.remote.model.TodoListItemBody

internal class TodoListItemDecoder(
    private val jsonSerializer: Json
) {
    fun decode(value: String): TodoListItemBody {
        return jsonSerializer.decodeFromString(value)
    }
}