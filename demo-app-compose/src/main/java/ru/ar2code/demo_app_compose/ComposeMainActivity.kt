package ru.ar2code.demo_app_compose

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ru.ar2code.demo_app_shared.GreetingSideEffects
import ru.ar2code.demo_app_shared.GreetingViewModel

class ComposeMainActivity : ComponentActivity() {

    private val greetingViewModel: GreetingViewModel by viewModels(factoryProducer = {
        ru.ar2code.demo_app_shared.GreetingViewModelFactory(
            this,
            null
        )
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            GreetingApp(greetingViewModel)
        }

        bindSideEffects()
    }

    private fun bindSideEffects() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                greetingViewModel.uiSideEffects.collect {
                    when (it) {
                        is GreetingSideEffects.GreetingToast -> Toast.makeText(
                            this@ComposeMainActivity,
                            it.msg,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
    }
}
