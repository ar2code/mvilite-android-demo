package ru.ar2code.demo_app_shared

sealed class GreetingResult {
    data class GreetingFieldInvalid(val error: String) : GreetingResult()
    data class NameFieldInvalid(val error: String) : GreetingResult()
    data class Hello(val greeting: String, val name: String) : GreetingResult()
}