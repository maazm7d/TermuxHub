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
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.material3.Text

@Composable
fun SplashScreen(
    onFinished: (Boolean) -> Unit
) {
    val vm: SplashViewModel = hiltViewModel()

    val green = Color(0xFF00FF5A)

    // Typing animation setup
    val bootLines = listOf(
        "Booting Termux Hub System...",
        "Initializing modules...",
        "Loading user tools...",
        "Checking repository updates...",
        "Starting UI service...",
        "System Ready."
    )

    var displayedLines by remember { mutableStateOf(listOf<String>()) }
    var cursorVisible by remember { mutableStateOf(true) }

    // Scanline flicker animation
    val infiniteTransition = rememberInfiniteTransition()
    val alphaAnim by infiniteTransition.animateFloat(
        initialValue = 0.85f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    // vertical scan gradient
    val scanlineBrush = Brush.verticalGradient(
        colors = listOf(
            Color(0x6600FF00),
            Color.Transparent,
            Color(0x6600FF00)
        )
    )

    LaunchedEffect(Unit) {
        // typing lines one by one
        bootLines.forEach { line ->
            displayedLines = displayedLines + line
            delay(500)
        }

        // blinking cursor
        while (true) {
            cursorVisible = !cursorVisible
            delay(350)
        }
    }

    // start loading repo
    LaunchedEffect(true) {
        vm.load { success -> onFinished(success) }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(18.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {

            displayedLines.forEach { line ->
                Text(
                    text = line,
                    color = green,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Start
                )
                Spacer(modifier = Modifier.height(4.dp))
            }

            // terminal cursor
            if (cursorVisible) {
                Text(
                    text = ">",
                    color = green,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }
        }

        // CRT scanlines overlay
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(scanlineBrush)
                .alpha(alphaAnim)
        )
    }
}
