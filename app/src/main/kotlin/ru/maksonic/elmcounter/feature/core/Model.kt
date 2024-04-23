package ru.maksonic.elmcounter.feature.core

import ru.maksonic.elmcounter.elm.ElmCommand
import ru.maksonic.elmcounter.elm.ElmEffect
import ru.maksonic.elmcounter.elm.ElmMessage
import ru.maksonic.elmcounter.elm.ElmModel

/**
 * @Author maksonic on 11.04.2024
 */
data class Model(
    val counterValue: Int,
    val isAutoIncrement: Boolean
) : ElmModel {
    companion object {
        val Initial = Model(
            counterValue = 0,
            isAutoIncrement = false,
        )
        val Preview = Model(
            isAutoIncrement = false,
            counterValue = 100500
        )
    }
}

sealed class Message : ElmMessage {
    data object OnIncrementClicked : Message()
    data object OnDecrementClicked : Message()
    data object OnAutoIncrementClicked : Message()
    data object OnResetCounterClicked : Message()
    data object ShowToastEffect : Message()
    data class FetchedAutoincrementResult(val value: Int) : Message()
}

sealed class Effect : ElmEffect {
    data class ShowToast(val value: Int) : Effect()
}

sealed class Command : ElmCommand {
    data class StartAutoincrement(val currentValue: Int) : Command()
    data object StopAutoincrement : Command()
    data object RestartAutoincrement : Command()
}