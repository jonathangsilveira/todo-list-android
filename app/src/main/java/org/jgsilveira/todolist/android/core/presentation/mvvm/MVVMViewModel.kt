package org.jgsilveira.todolist.android.core.presentation.mvvm

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import org.jgsilveira.todolist.android.core.presentation.model.ViewEffect
import org.jgsilveira.todolist.android.core.presentation.model.ViewState

abstract class MVVMViewModel<STATE: ViewState, EFFECT: ViewEffect>(
    initialState: STATE
) : ViewModel() {

    private val mutableViewState = MutableStateFlow(initialState)

    private val viewEffectChannel = Channel<EFFECT>(
        capacity = Channel.BUFFERED,
        onBufferOverflow = BufferOverflow.SUSPEND
    )

    val viewState: StateFlow<STATE>
        get() = mutableViewState

    val viewEffects: Flow<EFFECT>
        get() = viewEffectChannel.receiveAsFlow()

    protected fun setState(reducer: (currentState: STATE) -> STATE) {
        mutableViewState.update(reducer)
    }

    protected fun sendEffect(uiEffect: EFFECT) {
        viewEffectChannel.trySend(uiEffect)
    }
}