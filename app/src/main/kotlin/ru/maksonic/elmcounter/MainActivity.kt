package ru.maksonic.elmcounter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import ru.maksonic.elmcounter.feature.ui.CounterScreen
import ru.maksonic.elmcounter.ui.theme.ElmCounterTheme

class MainActivity : ComponentActivity() {

    private val counterSandboxStore by lazy { (this.application as AppCounter).counterSandboxStore }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ElmCounterTheme {
                CounterScreen(counterSandboxStore)
            }
        }
    }
}