package ru.ar2code.demo_app_shared

import kotlinx.coroutines.delay

interface IGreetingUseCase {
    suspend fun run(greeting: String?, name: String?): GreetingResult
}

class GreetingUseCase : IGreetingUseCase {

    override suspend fun run(greeting: String?, name: String?): GreetingResult {

        return when {
            greeting.isNullOrEmpty() -> {
                return GreetingResult.GreetingFieldInvalid("Greeting is required")
            }
            name.isNullOrEmpty() -> {
                return GreetingResult.NameFieldInvalid("name is required")
            }
            else -> {
                //Emulates long background operation
                delay(1000)
                //Result after background operation
                GreetingResult.Hello(greeting, name)
            }
        }

    }


}

sealed class GreetingResult {

    data class GreetingFieldInvalid(val error: String) : GreetingResult()
    data class NameFieldInvalid(val error: String) : GreetingResult()
    data class Hello(val greeting: String, val name: String) : GreetingResult()
}