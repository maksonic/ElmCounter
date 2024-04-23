package ru.maksonic.elmcounter.feature.core

import androidx.lifecycle.ViewModel

/**
 * @Author maksonic on 11.04.2024
 */
class CounterSandboxStore(
    private val counterSandbox: CounterSandbox,
) : ViewModel() {
    val sandbox get() = counterSandbox
}
