package ru.maksonic.elmcounter.feature.core

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import ru.maksonic.elmcounter.elm.ElmProgram

/**
 * @Author maksonic on 11.04.2024
 */
class CounterProgram : ElmProgram<Message, Command>() {

    private companion object {
        private const val COUNTER_COMMAND_KEY = "counter_command_key"
        private const val INCREMENT_DELAY = 1000L
        private const val INITIAL_COUNTER_VALUE = 0
    }

    override suspend fun execute(cmd: Command, consumer: (Message) -> Unit) {
        when (cmd) {
            is Command.StartAutoincrement -> startAutoincrement(cmd.currentValue, consumer)
            is Command.StopAutoincrement -> cancelCommand(COUNTER_COMMAND_KEY)
            is Command.RestartAutoincrement -> resetCounter(consumer)
        }
    }

    private suspend fun startAutoincrement(currentValue: Int, consumer: (Message) -> Unit) {
        executeCancellable(COUNTER_COMMAND_KEY) {
            autoincrement(currentValue).cancellable().collectLatest {
                consumer(Message.FetchedAutoincrementResult(it))
            }
        }
    }

    private suspend fun resetCounter(consumer: (Message) -> Unit) {
        cancelCommand(COUNTER_COMMAND_KEY)
        startAutoincrement(INITIAL_COUNTER_VALUE, consumer)
    }

    private fun autoincrement(initialValue: Int): Flow<Int> = flow {
        var increment = initialValue
        while (increment < Int.MAX_VALUE) {
            delay(INCREMENT_DELAY)
            increment = increment.inc()
            emit(increment)
        }
    }
}