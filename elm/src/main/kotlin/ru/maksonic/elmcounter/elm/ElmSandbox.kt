package ru.maksonic.elmcounter.elm

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.job
import kotlinx.coroutines.launch

/**
 * @Author maksonic on 11.04.2024
 */
abstract class ElmSandbox<T : ElmModel, M : ElmMessage, C : ElmCommand, E : ElmEffect>(
    initialModel: T,
    initialCommands: Set<C> = emptySet(),
    initialEffects: Set<E> = emptySet(),
    private val programs: List<ElmProgram<M, C>> = emptyList(),
) {
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO + Job())

    private val mutableModel = MutableStateFlow(initialModel)
    val model = mutableModel.asStateFlow()

    private val effectsChannel: Channel<E> = Channel()
    val effects = effectsChannel.receiveAsFlow()

    init {
        if (initialCommands.isNotEmpty()) {
            executePrograms(initialCommands)
        }
        if (initialEffects.isNotEmpty()) {
            executeEffects(initialEffects)
        }
    }

    private fun executePrograms(commands: Set<C>) {
        if (commands.isNotEmpty()) {
            commands.forEach { command ->
                programs.forEach { program ->
                    scope.launch {
                        program.execute(command, ::send)
                    }
                }
            }
        }
    }

    private fun executeEffects(effects: Set<E>) {
        if (effects.isNotEmpty()) {
            effects.forEach { effect ->
                scope.launch {
                    effectsChannel.send(effect)
                }
            }
        }
    }

    protected abstract fun update(msg: M, model: T): T

    fun update(model: T, commands: Set<C> = emptySet(), effects: Set<E> = emptySet()): T {
        if (commands.isNotEmpty()) {
            executePrograms(commands)
        }

        if (effects.isNotEmpty()) {
            executeEffects(effects)
        }
        return model
    }

    fun send(msg: M) = mutableModel.update { update(msg, mutableModel.value) }

    fun onCleared() {
        scope.coroutineContext.job.cancel()
    }
}