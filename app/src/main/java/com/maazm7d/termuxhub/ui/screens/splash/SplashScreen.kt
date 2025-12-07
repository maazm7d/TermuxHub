package com.maazm7d.termuxhub.ui.screens.splash

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.delay
import androidx.compose.material3.Text

@Composable
fun SplashScreen(
    onFinished: (Boolean) -> Unit
) {
    val vm: SplashViewModel = hiltViewModel()

    val terminalWhite = Color(0xFFD9D9D9)

    val bootLines = listOf(
        "Loading Termux Hub System...",
        "Initializing environment...",
        "Preparing database...",
        "Fetching latest tools...",
        "Verifying dependencies...",
        "System ready."
    )

    var displayedLines by remember { mutableStateOf(listOf<String>()) }
    var cursorVisible by remember { mutableStateOf(true) }

    // blinking cursor animation
    val infiniteTransition = rememberInfiniteTransition()
    val cursorAlpha by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 0.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    // subtle glass reflection gradient (far better look than green)
    val screenGlass = Brush.verticalGradient(
        colors = listOf(
            Color(0x10FFFFFF),
            Color.Transparent
        )
    )

    LaunchedEffect(Unit) {
        bootLines.forEach { line ->
            displayedLines = displayedLines + line
            delay(450)
        }
        delay(500)
        vm.load { success -> onFinished(success) }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0D0D0D))
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(22.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {

            displayedLines.forEach { line ->
                Text(
                    text = line,
                    color = terminalWhite,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Normal,
                    textAlign = TextAlign.Start
                )
                Spacer(modifier = Modifier.height(6.dp))
            }

            Text(
                text = if (cursorVisible) "█" else "",
                color = terminalWhite,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.alpha(cursorAlpha)
            )
        }

        // reflection overlay
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(screenGlass)
        )
    }
}
