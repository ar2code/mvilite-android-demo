package ru.ar2code.mvilite_core

/**
 * Factory that creates initial state for [MviLiteViewModel]
 */
interface InitialStateFactory<S> {

    /**
     * Get initial state
     */
    fun getState(): S
}