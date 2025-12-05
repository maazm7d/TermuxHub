package com.maazm7d.termuxhub.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ToolChipsRow(
    chips: List<String>,
    selectedIndex: Int,
    onSelected: (index: Int) -> Unit
) {
    val scroll = rememberScrollState()
    Row(modifier = Modifier
        .horizontalScroll(scroll)
        .padding(horizontal = 8.dp, vertical = 6.dp)
    ) {
        chips.forEachIndexed { index, label ->
            val selected = index == selectedIndex
            AssistChip(
                onClick = { onSelected(index) },
                label = { Text(label) },
                colors = AssistChipDefaults.assistChipColors(
                    containerColor = if (selected) androidx.compose.material3.MaterialTheme.colorScheme.primary else androidx.compose.material3.MaterialTheme.colorScheme.surface,
                    labelColor = if (selected) androidx.compose.material3.MaterialTheme.colorScheme.onPrimary else androidx.compose.material3.MaterialTheme.colorScheme.onSurface
                ),
                modifier = Modifier.padding(end = 8.dp)
            )
        }
    }
}