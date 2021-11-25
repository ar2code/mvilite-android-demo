package ru.ar2code.demo_app_shared

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ru.ar2code.mvilite_core.InitialStateFactory
import ru.ar2code.mvilite_core.MviLiteViewModel

class GreetingViewModel(
    private val savedStateHandle: SavedStateHandle,
    initialStateFactory: InitialStateFactory<GreetingUiState>
) :
    MviLiteViewModel<GreetingUiState, GreetingSideEffects>(initialStateFactory) {

    init {
        collectUiStateForSavingViewModelState()
    }

    fun updateGreeting(newValue: String?) {
        updateState {
            it.copy(greeting = newValue, greetingEditError = null)
        }
    }

    fun updateName(newValue: String?) {
        updateState {
            it.copy(name = newValue, nameEditError = null)
        }
    }

    fun greeting() {

        fun emitGreetingSideEffect(greetingState: GreetingUiState) {
            emitSideEffect(
                GreetingSideEffects.GreetingToast("${greetingState.getHelloText()} effect.")
            )
        }

        fun setGreetingEditInvalidState() {
            updateState { currentState ->
                currentState.copy(
                    isLoading = false,
                    isGreetingEditScreenVisible = true,
                    isHelloScreenVisible = false,
                    greetingEditError = "Greeting is required"
                )
            }
        }

        fun setNameEditInvalidState() {
            updateState { currentState ->
                currentState.copy(
                    isLoading = false,
                    isGreetingEditScreenVisible = true,
                    isHelloScreenVisible = false,
                    nameEditError = "Name is required"
                )
            }
        }

        fun setHelloState(greeting: String, name: String) {
            //Ui state is loading
            updateState {
                it.copy(
                    isLoading = true,
                    isGreetingEditScreenVisible = false,
                    isHelloScreenVisible = false
                )
            }

            viewModelScope.launch {
                //Emulates long background operation
                delay(1000)

                //Now we can update the ui state depends on the result from previous background operation
                updateStateAndGetUpdated {
                    GreetingUiState(
                        isLoading = false,
                        greeting,
                        name,
                        isGreetingEditScreenVisible = false,
                        isHelloScreenVisible = true,
                        null,
                        null
                    )
                }?.let {
                    emitGreetingSideEffect(it)
                }
            }
        }

        val currentState = uiState.value

        when {
            currentState.greeting.isNullOrEmpty() -> {
                setGreetingEditInvalidState()
            }
            currentState.name.isNullOrEmpty() -> {
                setNameEditInvalidState()
            }
            else -> {
                setHelloState(currentState.greeting, currentState.name)
            }
        }
    }

    private fun saveCurrentState(greetingState: GreetingUiState) {
        savedStateHandle.set(GREETING_KEY, greetingState.greeting)
        savedStateHandle.set(NAME_KEY, greetingState.name)
    }

    private fun collectUiStateForSavingViewModelState() {
        viewModelScope.launch {
            uiState.collect {
                saveCurrentState(it)
            }
        }
    }
}