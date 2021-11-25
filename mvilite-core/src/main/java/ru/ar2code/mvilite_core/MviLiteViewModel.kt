package ru.ar2code.mvilite_core

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Simple MVI ViewModel that provides ui state and side effects as flows.
 * UI State can be atomically updated by some of [updateState] methods.
 * New subscribers will receive current state.
 *
 * Side effects can be emitted with [emitSideEffect].
 * New subscribers will not receive previous effect. Only active subscribers can receive side effects.
 */
abstract class MviLiteViewModel<S, E>(
    initialStateFactory: InitialStateFactory<S>
) : ViewModel() {

    private val viewStateMutable = MutableStateFlow(initialStateFactory.getState())
    private val sideEffectsMutable =
        MutableSharedFlow<E>(0, 1, onBufferOverflow = BufferOverflow.DROP_LATEST)

    /**
     * UI State flow
     */
    val uiState = viewStateMutable.asStateFlow()

    /**
     * Side effects flow
     */
    val uiSideEffects = sideEffectsMutable.asSharedFlow()

    /**
     * Update UI state atomically.
     * [reducerFunction] may be evaluated multiple times, if [MutableStateFlow.value] is being concurrently updated, so
     * you should not put any calculation operations inside reducer, if recalculating is not required by your logic.
     */
    protected fun updateState(reducerFunction: (S) -> S?) {
        viewStateMutable.updateIfNotNull(reducerFunction)
    }

    /**
     * Update UI state atomically.
     * [reducerFunction] may be evaluated multiple times, if [MutableStateFlow.value] is being concurrently updated, so
     * you should not put any calculation operations inside reducer, if recalculating is not required by your logic.
     *
     * @return previos state
     */
    protected fun updateStateAndGetPrevious(reducerFunction: (S) -> S?): S {
        return viewStateMutable.updateIfNotNullAndGetPrevious(reducerFunction)
    }

    /**
     * Update UI state atomically.
     * [reducerFunction] may be evaluated multiple times, if [MutableStateFlow.value] is being concurrently updated, so
     * you should not put any calculation operations inside reducer, if recalculating is not required by your logic.
     *
     * @return new state
     */
    protected fun updateStateAndGetUpdated(reducerFunction: (S) -> S?): S? {
        return viewStateMutable.updateIfNotNullAndGetUpdated(reducerFunction)
    }

    /**
     * Send side effect
     */
    protected fun emitSideEffect(effect: E) {
        sideEffectsMutable.tryEmit(effect)
    }

}