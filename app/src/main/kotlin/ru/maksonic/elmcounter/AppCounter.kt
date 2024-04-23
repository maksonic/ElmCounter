package ru.maksonic.elmcounter

import android.app.Application
import ru.maksonic.elmcounter.feature.core.CounterProgram
import ru.maksonic.elmcounter.feature.core.CounterSandbox
import ru.maksonic.elmcounter.feature.core.CounterSandboxStore

/**
 * @Author maksonic on 11.04.2024
 */
class AppCounter : Application() {

    private val counterProgram by lazy { CounterProgram() }
    private val counterSandbox by lazy { CounterSandbox(counterProgram) }
    val counterSandboxStore: CounterSandboxStore by lazy { CounterSandboxStore(counterSandbox) }

}