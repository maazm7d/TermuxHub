package com.maazm7d.termuxhub.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * A row of category filter chips.
 *
 * @param chips List of pairs (category name, count) to display.
 * @param selectedIndex Index of the currently selected chip.
 * @param onChipSelected Callback when a chip is selected, providing its index.
 */
@Composable
fun CategoryChips(
    chips: List<Pair<String, Int>>,
    selectedIndex: Int,
    onChipSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        itemsIndexed(chips, key = { index, _ -> index }) { index, (category, count) ->
            val label = if (category == "All") "All ($count)" else "$category ($count)"

            FilterChip(
                selected = selectedIndex == index,
                onClick = { onChipSelected(index) },
                label = {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.labelLarge
                    )
                },
                shape = MaterialTheme.shapes.medium,
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                    selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    containerColor = MaterialTheme.colorScheme.surface,
                    labelColor = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                border = FilterChipDefaults.filterChipBorder(
                    enabled = true,
                    selected = selectedIndex == index,
                    borderColor = MaterialTheme.colorScheme.outlineVariant,
                    selectedBorderColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    }
}
