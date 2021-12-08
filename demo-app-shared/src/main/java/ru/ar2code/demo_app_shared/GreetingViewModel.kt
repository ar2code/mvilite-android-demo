package ru.ar2code.demo_app_shared

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ru.ar2code.mvilite_core.MviLiteInitialStateFactory
import ru.ar2code.mvilite_core.MviLiteReducer
import ru.ar2code.mvilite_core.MviLiteViewModel

class GreetingViewModel(
    private val savedStateHandle: SavedStateHandle,
    initialStateFactory: MviLiteInitialStateFactory<GreetingUiState>,
    private val greetingInteractor: IGreetingInteractor = GreetingInteractor(),
    private val updateGreetingReducer: MviLiteReducer<UpdateTextFieldIntent, GreetingUiState> = UpdateGreetingFieldReducer(),
    private val updateNameReducer: MviLiteReducer<UpdateTextFieldIntent, GreetingUiState> = UpdateNameFieldReducer(),
    private val greetingEditInvalidReducer: MviLiteReducer<GreetingResult.GreetingFieldInvalid, GreetingUiState> = GreetingEditInvalidReducer(),
    private val nameEditInvalidReducer: MviLiteReducer<GreetingResult.NameFieldInvalid, GreetingUiState> = NameEditInvalidReducer(),
    private val loadingReducer: MviLiteReducer<Unit, GreetingUiState> = LoadingReducer(),
    private val helloReducer: MviLiteReducer<GreetingResult.Hello, GreetingUiState> = HelloReducer(),
    private val greetingReducer: MviLiteReducer<GreetingResult, GreetingUiState> = GreetingReducer(
        greetingEditInvalidReducer,
        nameEditInvalidReducer,
        helloReducer
    )
) :
    MviLiteViewModel<GreetingUiState, GreetingSideEffects>(initialStateFactory) {

    init {
        collectUiStateForSavingViewModelState()
    }

    //region Reducers

    class UpdateGreetingFieldReducer : MviLiteReducer<UpdateTextFieldIntent, GreetingUiState> {
        override fun reduce(
            intent: UpdateTextFieldIntent,
            currentState: GreetingUiState
        ): GreetingUiState {
            return currentState.copy(greeting = intent.newValue, greetingEditError = null)
        }
    }

    class UpdateNameFieldReducer : MviLiteReducer<UpdateTextFieldIntent, GreetingUiState> {
        override fun reduce(
            intent: UpdateTextFieldIntent,
            currentState: GreetingUiState
        ): GreetingUiState {
            return currentState.copy(name = intent.newValue, nameEditError = null)
        }
    }

    class GreetingEditInvalidReducer :
        MviLiteReducer<GreetingResult.GreetingFieldInvalid, GreetingUiState> {
        override fun reduce(
            intent: GreetingResult.GreetingFieldInvalid,
            currentState: GreetingUiState
        ): GreetingUiState {
            return currentState.copy(
                isLoading = false,
                isGreetingEditScreenVisible = true,
                isHelloScreenVisible = false,
                greetingEditError = intent.error
            )
        }
    }

    class NameEditInvalidReducer :
        MviLiteReducer<GreetingResult.NameFieldInvalid, GreetingUiState> {
        override fun reduce(
            intent: GreetingResult.NameFieldInvalid,
            currentState: GreetingUiState
        ): GreetingUiState {
            return currentState.copy(
                isLoading = false,
                isGreetingEditScreenVisible = true,
                isHelloScreenVisible = false,
                nameEditError = intent.error
            )
        }
    }

    class LoadingReducer : MviLiteReducer<Unit, GreetingUiState> {
        override fun reduce(intent: Unit, currentState: GreetingUiState): GreetingUiState {
            return currentState.copy(
                isLoading = true,
                isGreetingEditScreenVisible = false,
                isHelloScreenVisible = false
            )
        }
    }

    class HelloReducer : MviLiteReducer<GreetingResult.Hello, GreetingUiState> {
        override fun reduce(
            intent: GreetingResult.Hello,
            currentState: GreetingUiState
        ): GreetingUiState {
            return GreetingUiState(
                isLoading = false,
                intent.greeting,
                intent.name,
                isGreetingEditScreenVisible = false,
                isHelloScreenVisible = true,
                null,
                null
            )
        }
    }

    class GreetingReducer(
        private val greetingEditInvalidReducer: MviLiteReducer<GreetingResult.GreetingFieldInvalid, GreetingUiState>,
        private val nameEditInvalidReducer: MviLiteReducer<GreetingResult.NameFieldInvalid, GreetingUiState>,
        private val helloReducer: MviLiteReducer<GreetingResult.Hello, GreetingUiState>
    ) : MviLiteReducer<GreetingResult, GreetingUiState> {
        override fun reduce(
            intent: GreetingResult,
            currentState: GreetingUiState
        ): GreetingUiState? {
            return when (intent) {
                is GreetingResult.GreetingFieldInvalid -> {
                    greetingEditInvalidReducer.reduce(intent, currentState)
                }
                is GreetingResult.NameFieldInvalid -> {
                    nameEditInvalidReducer.reduce(intent, currentState)
                }
                is GreetingResult.Hello -> {
                    helloReducer.reduce(intent, currentState)
                }
            }
        }
    }

    //endregion

    fun updateGreeting(newValue: String?) {
        updateWithReducerAndGetUpdated(UpdateTextFieldIntent(newValue), updateGreetingReducer)
    }

    fun updateName(newValue: String?) {
        updateWithReducerAndGetUpdated(UpdateTextFieldIntent(newValue), updateNameReducer)
    }

    fun greeting() {
        viewModelScope.launch {
            updateWithReducerAndGetUpdated(Unit, loadingReducer)

            val greetingResult =
                greetingInteractor.greeting(uiState.value.greeting, uiState.value.name)

            updateWithReducerAndGetUpdated(greetingResult, greetingReducer)
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