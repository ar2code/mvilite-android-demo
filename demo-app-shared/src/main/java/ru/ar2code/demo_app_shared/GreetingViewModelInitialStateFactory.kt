package ru.ar2code.demo_app_shared

import androidx.lifecycle.SavedStateHandle
import ru.ar2code.mvilite_core.MviLiteSavedStateHandleInitialFactory

class GreetingViewModelInitialStateFactory(savedStateHandle: SavedStateHandle) :
    MviLiteSavedStateHandleInitialFactory<GreetingUiState>(
        savedStateHandle
    ) {

    override fun getState(savedStateHandle: SavedStateHandle): GreetingUiState {
        val greeting = savedStateHandle.get<String>(GREETING_KEY)
        val name = savedStateHandle.get<String>(NAME_KEY)

        return if (greeting.isNullOrEmpty() || name.isNullOrEmpty()) {
            GreetingUiState(
                isLoading = false,
                "Hello",
                null,
                isGreetingEditScreenVisible = true,
                isHelloScreenVisible = false,
                greetingEditError = null,
                nameEditError = null
            )
        } else {
            GreetingUiState(
                isLoading = false,
                greeting,
                name,
                isGreetingEditScreenVisible = false,
                isHelloScreenVisible = true,
                greetingEditError = null,
                nameEditError = null
            )
        }
    }
}