package com.maazm7d.termuxhub.ui.screens.hall

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
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
            .padding(16.dp)
    ) {

        Text(
            text = "Hall of Fame",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(Modifier.height(16.dp))

        when {
            isLoading -> {
                CircularProgressIndicator()
            }

            members.isEmpty() -> {
                Text(
                    text = "No internet connection.\nPlease connect to the internet to load Hall of Fame.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            else -> {
                Column(
                    modifier = Modifier.verticalScroll(rememberScrollState())
                ) {
                    members.forEach {
                        HallOfFameCard(it)
                        Spacer(Modifier.height(12.dp))
                    }
                }
            }
        }
    }
}
