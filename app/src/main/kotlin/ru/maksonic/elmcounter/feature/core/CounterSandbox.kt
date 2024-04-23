package ru.maksonic.elmcounter.feature.core

import ru.maksonic.elmcounter.elm.ElmSandbox

/**
 * @Author maksonic on 11.04.2024
 */
class CounterSandbox(counterProgram: CounterProgram) : ElmSandbox<Model, Message, Command, Effect>(
    initialModel = Model.Initial,
    programs = listOf(counterProgram)
) {

    override fun update(msg: Message, model: Model): Model = when (msg) {
        is Message.OnIncrementClicked -> onIncrementClicked(model)
        is Message.OnDecrementClicked -> onDecrementClicked(model)
        is Message.OnAutoIncrementClicked -> onAutoIncrementClicked(model)
        is Message.OnResetCounterClicked -> onResetCounterClicked(model)
        is Message.FetchedAutoincrementResult -> fetchedAutoincrementResult(model, msg)
        is Message.ShowToastEffect -> showToastEffect(model)
    }

    private fun onIncrementClicked(model: Model) =
        model.copy(counterValue = model.counterValue.inc())

    private fun onDecrementClicked(model: Model): Model {
        val updatedValue = model.counterValue.dec()
        val effect =
            if (updatedValue % 3 == 0) setOf(Effect.ShowToast(updatedValue)) else emptySet()

        return update(model.copy(counterValue = updatedValue), effects = effect)
    }

    private fun onAutoIncrementClicked(model: Model): Model {
        val command = if (!model.isAutoIncrement) Command.StartAutoincrement(model.counterValue)
        else Command.StopAutoincrement

        return update(
            model = model.copy(isAutoIncrement = !model.isAutoIncrement),
            commands = setOf(command)
        )
    }

    private fun onResetCounterClicked(model: Model): Model {
        val command = if (model.isAutoIncrement) setOf(Command.RestartAutoincrement) else emptySet()
        return update(model.copy(counterValue = 0), commands = command)
    }

    private fun fetchedAutoincrementResult(model: Model, msg: Message.FetchedAutoincrementResult) =
        update(model.copy(counterValue = msg.value))

    private fun showToastEffect(model: Model) =
        update(model, effects = setOf(Effect.ShowToast(model.counterValue)))
}