package ru.ar2code.mvilite_ui

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ru.ar2code.mvilite_core.MviLiteViewModel

abstract class MviLiteViewBindingFragment<VB, S, E> :
    ViewBindingFragment<VB>() where VB : ViewBinding {

    protected abstract val viewModel: MviLiteViewModel<S, E>

    protected var currentUiViewState: S? = null
        private set

    protected var previousUiSideEffect: E? = null
        private set

    protected abstract fun onNewUiViewState(newUiViewState: S)

    protected open fun onNewUiSideEffect(newUiViewEvent: E) {
        //Реакция на вью-событие
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeUiState()
        observeSideEffects()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        currentUiViewState = null
        previousUiSideEffect = null
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                    onNewUiViewState(it)
                    currentUiViewState = it
                }
            }
        }
    }

    private fun observeSideEffects() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiSideEffects.collect {
                    onNewUiSideEffect(it)
                    previousUiSideEffect = it
                }
            }
        }
    }

}