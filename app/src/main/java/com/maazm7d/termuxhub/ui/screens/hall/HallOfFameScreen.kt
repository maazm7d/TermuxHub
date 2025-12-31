package com.maazm7d.termuxhub.ui.screens.hall

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.maazm7d.termuxhub.ui.components.HallOfFameCard

@Composable
fun HallOfFameScreen(
    viewModel: HallOfFameViewModel = hiltViewModel()
) {
    val members by viewModel.members
    val isLoading by viewModel.isLoading

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {

        // Title with icon (centered)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.EmojiEvents,
                contentDescription = "Hall of Fame",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(22.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = "Hall of Fame",
                style = MaterialTheme.typography.headlineSmall
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Description
        Text(
            text = "This space celebrates contributors and members who made a meaningful impact on TermuxHub.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Subtle divider
        Divider(
    modifier = Modifier
        .width(120.dp)
        .align(Alignment.CenterHorizontally),
    thickness = 1.dp,
    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
)

        Spacer(modifier = Modifier.height(20.dp))

        when {
            isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            members.isEmpty() -> {
                Text(
                    text = "No internet connection.\nPlease connect to the internet to load Hall of Fame.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }

            else -> {
                Column(
                    modifier = Modifier.verticalScroll(rememberScrollState())
                ) {
                    members.forEach { member ->
                        HallOfFameCard(member)
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }
        }
    }
}
