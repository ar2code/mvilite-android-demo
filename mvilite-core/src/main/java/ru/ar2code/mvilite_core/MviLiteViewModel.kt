package ru.ar2code.mvilite_core

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

abstract class MviLiteViewModel<S, E>(
    initialStateFactory: InitialStateFactory<S>
) : ViewModel() {

    private val viewStateMutable = MutableStateFlow(initialStateFactory.getState())
    private val sideEffectsMutable =
        MutableSharedFlow<E>(0, 1, onBufferOverflow = BufferOverflow.DROP_LATEST)

    val uiState = viewStateMutable.asStateFlow()
    val uiSideEffects = sideEffectsMutable.asSharedFlow()

    /**
     * [reducerFunction] may be evaluated multiple times, if [MutableStateFlow.value] is being concurrently updated, so
     * you should not put any calculation operations inside reducer, if recalculating is not required by your logic.
     */
    protected fun updateState(reducerFunction: (S) -> S?) {
        viewStateMutable.updateIfNotNull(reducerFunction)
    }

    protected fun updateStateAndGetPrevious(reducerFunction: (S) -> S?): S {
        return viewStateMutable.updateIfNotNullAndGetPrevious(reducerFunction)
    }

    protected fun updateStateAndGetUpdated(reducerFunction: (S) -> S?): S? {
        return viewStateMutable.updateIfNotNullAndGetUpdated(reducerFunction)
    }

    protected fun emitSideEffect(effect: E) {
        sideEffectsMutable.tryEmit(effect)
    }

}