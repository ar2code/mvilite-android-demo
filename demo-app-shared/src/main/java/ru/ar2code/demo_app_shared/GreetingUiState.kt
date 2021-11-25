package ru.ar2code.demo_app_shared

data class GreetingUiState(
    val isLoading: Boolean,
    val greeting: String?,
    val name: String?,
    val isGreetingEditScreenVisible: Boolean,
    val isHelloScreenVisible: Boolean,
    val greetingEditError: String?,
    val nameEditError: String?
) {
    fun getHelloText() = "$greeting, $name"

    fun isHelloShouldBeRendered(previous: GreetingUiState?): Boolean {
        return isGreetingShouldBeRendered(previous?.greeting) || isNameShouldBeRendered(previous?.name)
    }

    fun isGreetingShouldBeRendered(previousText: String?): Boolean {
        return previousText != greeting
    }

    fun isNameShouldBeRendered(previousText: String?): Boolean {
        return previousText != name
    }

    fun isGreetingEditErrorShouldBeRendered(previous: GreetingUiState?): Boolean {
        return previous?.greetingEditError != greetingEditError
    }

    fun isNameEditErrorShouldBeRendered(previous: GreetingUiState?): Boolean {
        return previous?.nameEditError != nameEditError
    }
}