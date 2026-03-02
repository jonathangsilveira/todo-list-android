package org.jgsilveira.todolist.android.features.todo.presentation

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.jgsilveira.todolist.android.core.date.GetCurrentTimeInMillisUseCase
import org.jgsilveira.todolist.android.core.logging.AppLogger
import org.jgsilveira.todolist.android.core.logging.AppLoggerProvider
import org.jgsilveira.todolist.android.core.presentation.model.ViewEffect
import org.jgsilveira.todolist.android.core.presentation.mvvm.MVVMViewModel
import org.jgsilveira.todolist.android.features.todo.domain.model.TodoListItem
import org.jgsilveira.todolist.android.features.todo.domain.model.TodoListItemStatus
import org.jgsilveira.todolist.android.features.todo.domain.usecase.AddItemUseCase
import org.jgsilveira.todolist.android.features.todo.domain.usecase.CreateItemUseCase
import org.jgsilveira.todolist.android.features.todo.domain.usecase.RemoveItemByUuidUseCase
import org.jgsilveira.todolist.android.features.todo.domain.usecase.StreamActiveItemsUseCase
import org.jgsilveira.todolist.android.features.todo.domain.usecase.UpdateItemUseCase
import java.util.UUID

private const val TAG = "TODO_LIST_VIEW"

internal class TodoListViewModel(
    private val createItem: CreateItemUseCase,
    private val addItem: AddItemUseCase,
    private val updateItem: UpdateItemUseCase,
    private val removeItemByUuid: RemoveItemByUuidUseCase,
    private val flowActiveItems: StreamActiveItemsUseCase,
    private val getCurrentTimeInMillis: GetCurrentTimeInMillisUseCase,
    private val logger: AppLogger = AppLoggerProvider.scope(TAG)
) : MVVMViewModel<TodoListViewState, ViewEffect>(initialState = TodoListViewState()) {

    init {
        viewModelScope.launch {
            flowActiveItems()
                .catch { throwable ->
                    logger.warn(message = "Error fetching items", throwable = throwable)
                    emit(listOf())
                }
                .map { activeItems ->
                    activeItems.map { it.toViewData() }
                }
                .stateIn(this)
                .collect { activeItems ->
                    setState { currentState ->
                        currentState.copy(
                            todoListItems = activeItems
                        )
                    }
                }
        }
    }

    fun addItem() {
        viewModelScope.launch {
            val newItem = createItem()
            addItem(newItem).onFailure { throwable ->
                logger.error(message = "Error adding item", throwable = throwable)
            }
        }
    }

    fun checkItem(viewData: TodoListItemViewData, isChecked: Boolean) {
        viewModelScope.launch {
            val updatedViewData = viewData.copy(
                isDone = isChecked,
                updatedAt = getCurrentTimeInMillis()
            )
            val updatedItem = updatedViewData.toDomain()
            updateItem(updatedItem).onFailure { throwable ->
                logger.error(
                    message = "Error updating item ${updatedItem.uuid}",
                    throwable = throwable
                )
            }
        }
    }

    fun removeItem(viewData: TodoListItemViewData) {
        viewModelScope.launch {
            val removedItem = viewData.toDomain()
            removeItemByUuid(removedItem)
                .onFailure { throwable ->
                    logger.error(
                        message = "Error removing item ${removedItem.uuid}",
                        throwable = throwable
                    )
                }
        }
    }

    fun updateItem(viewData: TodoListItemViewData, title: String) {
        viewModelScope.launch {
            val updatedViewData = viewData.copy(
                text = title,
                updatedAt = getCurrentTimeInMillis()
            )
            val updatedItem = updatedViewData.toDomain()
            updateItem(updatedItem)
                .onFailure { throwable ->
                    logger.error(
                        message = "Error updating item ${updatedItem.uuid}",
                        throwable = throwable
                    )
                }
        }
    }
}