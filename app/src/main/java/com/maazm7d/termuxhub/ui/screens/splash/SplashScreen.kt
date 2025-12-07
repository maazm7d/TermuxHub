package com.maazm7d.termuxhub.ui.screens.splash

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onFinished: (Boolean) -> Unit
) {
    val vm: SplashViewModel = hiltViewModel()

    val fullText = "Termux Hub"
    var visibleText by remember { mutableStateOf("") }
    var showCursor by remember { mutableStateOf(true) }

    // Typing effect
    LaunchedEffect(Unit) {
        for (i in fullText.indices) {
            visibleText = fullText.substring(0, i + 1)
            delay(100) // typing speed
        }

        // keep blinking cursor while loading
        launch {
            while (true) {
                showCursor = !showCursor
                delay(350)
            }
        }

        delay(1200)
        vm.load { success -> onFinished(success) }
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Terminal style animated typing text
            Text(
                text = visibleText + if (showCursor) "|" else "",
                color = Color.Green,
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                "Initializing system modules...",
                color = Color.Green.copy(alpha = 0.7f),
                fontSize = 16.sp
            )
        }
    }
}
