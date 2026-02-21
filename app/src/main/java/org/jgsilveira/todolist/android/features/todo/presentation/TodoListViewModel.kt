package org.jgsilveira.todolist.android.features.todo.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.jgsilveira.todolist.android.features.todo.domain.model.TodoListItem
import org.jgsilveira.todolist.android.features.todo.domain.model.TodoListItemStatus
import org.jgsilveira.todolist.android.features.todo.domain.usecase.AddItemUseCase
import org.jgsilveira.todolist.android.features.todo.domain.usecase.RemoveItemByUuidUseCase
import org.jgsilveira.todolist.android.features.todo.domain.usecase.StreamActiveItemsUseCase
import org.jgsilveira.todolist.android.features.todo.domain.usecase.UpdateItemUseCase
import java.util.Date
import java.util.UUID

private const val TAG = "TODO_LIST_VIEW"

internal class TodoListViewModel(
    private val addItem: AddItemUseCase,
    private val updateItem: UpdateItemUseCase,
    private val removeItemByUuid: RemoveItemByUuidUseCase,
    private val flowActiveItems: StreamActiveItemsUseCase
) : ViewModel() {
    private val mutableViewState = MutableStateFlow(TodoListViewState())
    val viewState: StateFlow<TodoListViewState>
        get() = mutableViewState

    init {
        viewModelScope.launch {
            flowActiveItems()
                .catch { throwable ->
                    Log.d(TAG, "Error fetching items", throwable)
                }
                .map { activeItems ->
                    activeItems.map { it.toViewData() }
                }
                .stateIn(this)
                .collect { activeItems ->
                    mutableViewState.update { currentState ->
                        currentState.copy(
                            todoListItems = activeItems
                        )
                    }
                }
        }
    }

    fun addItems(count: Int = 1) {
        viewModelScope.launch {
            val newItems = List(size = count) {
                TodoListItem(
                    uuid = UUID.randomUUID().toString(),
                    title = "",
                    status = TodoListItemStatus.PENDING,
                    isSynced = false,
                    createdAt = Date(),
                    updatedAt = null,
                    lastSyncAt = null
                )
            }
            newItems.forEach {
                addItem(it).onFailure { throwable ->
                    Log.d(TAG, "Error adding item", throwable)
                }
            }
        }
    }

    fun checkItem(viewData: TodoListItemViewData, isChecked: Boolean) {
        viewModelScope.launch {
            val updatedViewData = viewData.copy(isDone = isChecked)
            val updatedItem = updatedViewData.toDomain()
            updateItem(updatedItem).onFailure { throwable ->
                Log.d(TAG, "Error updating item ${updatedItem.uuid}", throwable)
            }
        }
    }

    fun removeItem(item: TodoListItemViewData) {
        viewModelScope.launch {
            val removedItem = item.toDomain()
            removeItemByUuid(removedItem)
                .onFailure { throwable ->
                    Log.d(TAG, "Error removing item ${removedItem.uuid}", throwable)
                }
        }
    }

    fun updateItem(viewData: TodoListItemViewData, title: String) {
        Log.d(TAG, "Updating item text ${viewData.id} from ${viewData.text} to $title")
        viewModelScope.launch {
            val updatedViewData = viewData.copy(text = title)
            val updatedItem = updatedViewData.toDomain()
            updateItem(updatedItem)
                .onFailure { throwable ->
                    Log.d(TAG, "Error updating item ${updatedItem.uuid}", throwable)
                }
        }
    }
}