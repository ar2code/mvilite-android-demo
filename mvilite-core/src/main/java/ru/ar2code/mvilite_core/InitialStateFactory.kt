package ru.ar2code.mvilite_core

interface InitialStateFactory<S> {

    fun getState(): S
}