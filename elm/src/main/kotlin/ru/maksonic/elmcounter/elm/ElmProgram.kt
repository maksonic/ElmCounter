package ru.maksonic.elmcounter.elm

import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

/**
 * @Author maksonic on 11.04.2024
 */
private typealias CommandsContainer = MutableMap<String, Job?>

abstract class ElmProgram<M : ElmMessage, C : ElmCommand> {

    private val commandsContainer: CommandsContainer = mutableMapOf()

    protected suspend fun executeCancellable(
        key: String,
        execution: suspend () -> Unit
    ) = coroutineScope {
        if (commandsContainer.contains(key)) return@coroutineScope

        val cmd = this.launch { execution() }
        commandsContainer[key] = cmd
    }

    protected fun cancelCommand(key: String) {
        if (commandsContainer.contains(key)) {
            commandsContainer[key]?.cancel()
            commandsContainer.remove(key)
        }
    }

    protected fun cancelAllCommands() = commandsContainer.forEach { cmd ->
        cmd.value?.cancel()
    }.let {
        commandsContainer.clear()
    }

    abstract suspend fun execute(cmd: C, consumer: (M) -> Unit)
}