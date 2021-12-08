package ru.ar2code.demo_app_shared

interface IGreetingInteractor {
    suspend fun greeting(greeting: String?, name: String?): GreetingResult
}