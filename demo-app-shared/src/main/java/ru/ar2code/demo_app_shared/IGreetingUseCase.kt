package ru.ar2code.demo_app_shared

interface IGreetingUseCase {
    suspend fun run(greeting: String?, name: String?): GreetingResult
}