package org.jgsilveira.todolist.android.features.todo.domain.usecase

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jgsilveira.todolist.android.features.todo.domain.model.TodoListItem
import org.jgsilveira.todolist.android.features.todo.domain.repository.LocalTodoListRepository

class GetActiveItemsUseCase(
    private val localTodoListRepository: LocalTodoListRepository,
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    suspend operator fun invoke(): Result<List<TodoListItem>> {
        return runCatching {
            withContext(coroutineDispatcher) {
                localTodoListRepository.getActiveItems()
            }
        }
    }
}