package ru.ar2code.mvilite_core

import androidx.lifecycle.SavedStateHandle

abstract class SavedStateHandleInitialStateFactory<S>(private val savedStateHandle: SavedStateHandle) :
    InitialStateFactory<S> {

    abstract fun getState(savedStateHandle: SavedStateHandle): S

    final override fun getState(): S {
        return getState(savedStateHandle)
    }
}