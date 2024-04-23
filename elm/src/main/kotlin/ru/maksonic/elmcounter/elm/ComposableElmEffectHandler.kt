package ru.maksonic.elmcounter.elm

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

/**
 * @Author maksonic on 11.04.2024
 */
@Composable
fun ComposableElmEffectHandler(
    effects: Flow<ElmEffect>,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    key: Any? = Unit,
    handle: (ElmEffect) -> Unit
) = LaunchedEffect(key) {
    launch {
        effects.flowWithLifecycle(lifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .collect { effect ->
                handle.invoke(effect)
            }
    }
}