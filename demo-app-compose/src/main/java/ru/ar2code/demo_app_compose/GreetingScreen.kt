package ru.ar2code.demo_app_compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.ar2code.demo_app_compose.ui.theme.MVILiteTheme
import ru.ar2code.demo_app_shared.GreetingViewModel


@Composable
fun GreetingApp(greetingViewModel: GreetingViewModel) {

    val uiState = greetingViewModel.uiState.collectAsState()

    MVILiteTheme {

        Box(
            Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {

            if (uiState.value.isLoading) {
                Loading()
            }
            if (uiState.value.isHelloScreenVisible) {
                GreetingMessage(uiState.value.getHelloText())
            }
            if (uiState.value.isGreetingEditScreenVisible) {
                GreetingEditScreen(
                    uiState.value.greeting,
                    uiState.value.name,
                    uiState.value.greetingEditError,
                    uiState.value.nameEditError,
                    greetingViewModel::updateGreeting,
                    greetingViewModel::updateName,
                    greetingViewModel::greeting
                )
            }
        }
    }
}

@Composable
fun GreetingEditScreen(
    greeting: String?,
    name: String?,
    greetingErrorMsg: String?,
    nameErrorMsg: String?,
    onGreetingChanged: (String) -> Unit,
    onNameChanged: (String) -> Unit,
    onSubmit: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        FieldEditor(text = greeting, onValueChange = onGreetingChanged, greetingErrorMsg)
        FieldEditor(text = name, onValueChange = onNameChanged, nameErrorMsg)
        GreetingScreenSubmit(onSubmit)
    }
}

@Composable
fun FieldEditor(text: String?, onValueChange: (String) -> Unit, errorMsg: String?) {
    OutlinedTextField(
        value = text.orEmpty(),
        onValueChange = onValueChange,
        isError = !errorMsg.isNullOrEmpty()
    )
}

@Composable
fun GreetingScreenSubmit(onSubmit: () -> Unit) {
    Button(onClick = onSubmit, Modifier.padding(16.dp)) {
        Text(text = "Say, hello")
    }
}

@Composable
fun GreetingMessage(message: String) {
    Text(text = message, style = MaterialTheme.typography.h3)
}

@Composable
fun Loading() {
    CircularProgressIndicator()
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MVILiteTheme {
        GreetingEditScreen("Hello", "Alex", null, null, {}, {}, {})
    }
}