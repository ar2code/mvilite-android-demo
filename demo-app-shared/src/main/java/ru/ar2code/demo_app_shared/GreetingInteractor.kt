package ru.ar2code.demo_app_shared

import kotlinx.coroutines.delay

internal class GreetingInteractor : IGreetingInteractor {
    override suspend fun greeting(greeting: String?, name: String?): GreetingResult {

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

