package ru.ar2code.mvilite.feature

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import ru.ar2code.demo_app_shared.GreetingSideEffects
import ru.ar2code.demo_app_shared.GreetingUiState
import ru.ar2code.demo_app_shared.GreetingViewModel
import ru.ar2code.demo_app_shared.GreetingViewModelFactory
import ru.ar2code.mvilite.databinding.FragmentGreetingBinding
import ru.ar2code.mvilite_ui.MviLiteViewBindingFragment

class GreetingFragment :
    MviLiteViewBindingFragment<FragmentGreetingBinding, GreetingUiState, GreetingSideEffects>() {

    override val viewModel: GreetingViewModel by viewModels(factoryProducer = {
        GreetingViewModelFactory(
            this
        )
    })

    override fun createViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentGreetingBinding {
        return FragmentGreetingBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindGreetingEditor()
        bindNameEditor()
        bindSubmit()
    }

    override fun onNewUiViewState(newUiViewState: GreetingUiState) {
        renderUiState(newUiViewState)
    }

    override fun onNewUiSideEffect(newUiViewEvent: GreetingSideEffects) {
        super.onNewUiSideEffect(newUiViewEvent)

        when (newUiViewEvent) {
            is GreetingSideEffects.GreetingToast -> Toast.makeText(
                requireContext(),
                newUiViewEvent.msg,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun renderUiState(newUiViewState: GreetingUiState) {
        viewBinding?.greetingEdit?.isVisible = newUiViewState.isGreetingEditScreenVisible
        viewBinding?.nameEdit?.isVisible = newUiViewState.isGreetingEditScreenVisible
        viewBinding?.submit?.isVisible = newUiViewState.isGreetingEditScreenVisible
        viewBinding?.hello?.isVisible = newUiViewState.isHelloScreenVisible
        viewBinding?.progress?.isVisible = newUiViewState.isLoading

        if (newUiViewState.isHelloShouldBeRendered(currentUiViewState)) {
            viewBinding?.hello?.text = newUiViewState.getHelloText()
        }
        if (newUiViewState.isGreetingShouldBeRendered(viewBinding?.greetingEdit?.text?.toString())) {
            viewBinding?.greetingEdit?.setText(newUiViewState.greeting)
        }
        if (newUiViewState.isNameShouldBeRendered(viewBinding?.nameEdit?.text?.toString())) {
            viewBinding?.nameEdit?.setText(newUiViewState.name)
        }
        if (newUiViewState.isGreetingEditErrorShouldBeRendered(currentUiViewState)) {
            viewBinding?.greetingEdit?.error = newUiViewState.greetingEditError
        }
        if (newUiViewState.isNameEditErrorShouldBeRendered(currentUiViewState)) {
            viewBinding?.nameEdit?.error = newUiViewState.nameEditError
        }
    }

    private fun bindGreetingEditor() {
        viewBinding?.greetingEdit?.doOnTextChanged { text, _, _, _ ->
            viewModel.updateGreeting(
                text.toString()
            )
        }
    }

    private fun bindNameEditor() {
        viewBinding?.nameEdit?.doOnTextChanged { text, _, _, _ ->
            viewModel.updateName(
                text.toString()
            )
        }
    }

    private fun bindSubmit() {
        viewBinding?.submit?.setOnClickListener {
            viewModel.greeting()
        }
    }
}