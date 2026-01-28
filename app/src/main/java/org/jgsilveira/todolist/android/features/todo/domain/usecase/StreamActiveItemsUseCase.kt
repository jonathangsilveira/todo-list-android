package org.jgsilveira.todolist.android.features.todo.domain.usecase

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import org.jgsilveira.todolist.android.features.todo.domain.model.TodoListItem
import org.jgsilveira.todolist.android.features.todo.domain.repository.LocalTodoListRepository

class StreamActiveItemsUseCase(
    private val localTodoListRepository: LocalTodoListRepository,
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    operator fun invoke(): Flow<List<TodoListItem>> {
        return localTodoListRepository.flowActiveItems()
            .flowOn(coroutineDispatcher)
    }
}