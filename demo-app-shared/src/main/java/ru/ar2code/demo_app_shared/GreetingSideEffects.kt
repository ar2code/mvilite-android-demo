package ru.ar2code.demo_app_shared

sealed class GreetingSideEffects {
    class GreetingToast(val msg: String) : GreetingSideEffects()
}