package ru.maksonic.elmcounter.feature.ui

import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

/**
 * @Author maksonic on 11.04.2024
 */
@Composable
internal fun ButtonCounter(
    onClick: () -> Unit,
    enabled: Boolean,
    title: String,
    modifier: Modifier = Modifier
) {
    Button(onClick = onClick, enabled = enabled) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center,
            modifier = modifier.width(64.dp)
        )
    }
}