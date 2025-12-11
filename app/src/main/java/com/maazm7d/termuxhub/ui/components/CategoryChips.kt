package com.maazm7d.termuxhub.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CategoryChips(
    chips: List<Pair<String, Int>>,
    selectedIndex: Int,
    onChipSelected: (Int) -> Unit
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(horizontal = 12.dp, vertical = 2.dp)
    ) {

        itemsIndexed(chips) { index, item ->

            val label = if (item.first == "All") "All (${item.second})"
            else "${item.first} (${item.second})"

            FilterChip(
                selected = selectedIndex == index,
                onClick = { onChipSelected(index) },
                label = { Text(label) },
                shape = MaterialTheme.shapes.medium,
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                    selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    }
}
