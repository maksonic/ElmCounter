package ru.maksonic.elmcounter.feature.ui

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.Flow
import ru.maksonic.elmcounter.elm.ComposableElmEffectHandler
import ru.maksonic.elmcounter.feature.core.CounterSandboxStore
import ru.maksonic.elmcounter.feature.core.Effect
import ru.maksonic.elmcounter.feature.core.Message
import ru.maksonic.elmcounter.feature.core.Model

/**
 * @Author maksonic on 11.04.2024
 */
private typealias SendMessage = (Message) -> Unit

@Composable
fun CounterScreen(sandboxStore: CounterSandboxStore) {
    val model by sandboxStore.sandbox.model.collectAsStateWithLifecycle()

    HandleUiEffects(effects = sandboxStore.sandbox.effects)

    CounterScreenContainer(model = model, send = sandboxStore.sandbox::send)
}

@Composable
private fun CounterScreenContainer(model: Model, send: SendMessage) {
    CounterScreenContent(
        model = model,
        onIncrementClicked = { send(Message.OnIncrementClicked) },
        onDecrementClicked = { send(Message.OnDecrementClicked) },
        onAutoIncrementClicked = { send(Message.OnAutoIncrementClicked) },
        onResetCounterClicked = { send(Message.OnResetCounterClicked) },
        showToast = { send(Message.ShowToastEffect) }
    )
}

@Composable
private fun CounterScreenContent(
    model: Model,
    onIncrementClicked: () -> Unit,
    onDecrementClicked: () -> Unit,
    onAutoIncrementClicked: () -> Unit,
    onResetCounterClicked: () -> Unit,
    showToast: (Int) -> Unit,
    modifier: Modifier = Modifier
) {

    var counterUiValue by rememberSaveable { mutableStateOf("0") }
    var autoincrementBtnTitle by rememberSaveable { mutableStateOf("Autoincrement") }

    LaunchedEffect(model.counterValue) {
        val counter = model.counterValue
        counterUiValue = counter.toString()

        if (counter != 0 && counter % 4 == 0 && !model.isAutoIncrement) {
            showToast(counter)
        }
    }

    LaunchedEffect(model.isAutoIncrement) {
        val title = if (model.isAutoIncrement) "Stop" else "Autoincrement"
        autoincrementBtnTitle = title
    }

    Column(
        modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ButtonCounter(
                onClick = onDecrementClicked,
                enabled = !model.isAutoIncrement,
                title = "Dec"
            )

            Text(
                text = counterUiValue,
                style = MaterialTheme.typography.displayMedium
            )

            ButtonCounter(
                onClick = onIncrementClicked,
                enabled = !model.isAutoIncrement,
                title = "Inc"
            )
        }

        Button(
            onClick = onAutoIncrementClicked,
            modifier = modifier
                .fillMaxWidth(0.7f)
                .padding(top = 100.dp)
        ) {
            Text(text = autoincrementBtnTitle, style = MaterialTheme.typography.headlineSmall)
        }

        Button(
            onClick = onResetCounterClicked,
            modifier = modifier
                .fillMaxWidth(0.7f)
                .padding(top = 16.dp)
        ) {
            Text(text = "Reset counter", style = MaterialTheme.typography.headlineSmall)
        }
    }
}

@Composable
private fun HandleUiEffects(effects: Flow<Effect>) {
    val context = LocalContext.current

    ComposableElmEffectHandler(effects) { eff ->
        when (eff) {
            is Effect.ShowToast -> {
                Toast.makeText(context, "${eff.value}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun CounterScreenPreview() {
    val model by remember { mutableStateOf(Model.Preview) }

    CounterScreenContent(
        model = model,
        onIncrementClicked = {},
        onDecrementClicked = {},
        onAutoIncrementClicked = {},
        onResetCounterClicked = {},
        showToast = {}
    )
}