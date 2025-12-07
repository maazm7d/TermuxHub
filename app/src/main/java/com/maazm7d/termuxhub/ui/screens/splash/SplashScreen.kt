package com.maazm7d.termuxhub.ui.screens.splash

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onFinished: (Boolean) -> Unit
) {
    val vm: SplashViewModel = hiltViewModel()

    val green = Color(0xFF15FF77)
    val bootLines = listOf(
        "Initializing Termux Hub...",
        "Booting system environment...",
        "Loading modules...",
        "Fetching remote tools list...",
        "Sync complete.",
        "System Ready."
    )

    var printedLines by remember { mutableStateOf(listOf<String>()) }
    var currentTypedText by remember { mutableStateOf("") }
    var cursorVisible by remember { mutableStateOf(true) }

    // Blink cursor
    LaunchedEffect(Unit) {
        while (true) {
            cursorVisible = !cursorVisible
            delay(350)
        }
    }

    // Typewriter effect
    LaunchedEffect(Unit) {
        for (line in bootLines) {
            currentTypedText = ""
            for (char in line) {
                currentTypedText += char
                delay(40)
            }
            printedLines = printedLines + currentTypedText
            delay(300)
        }

        delay(600)
        vm.load { success -> onFinished(success) }
    }

    // Scanline flicker animation
    val infiniteTransition = rememberInfiniteTransition(label = "")
    val alphaAnim by infiniteTransition.animateFloat(
        initialValue = 0.9f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(550, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = ""
    )

    // Fade-in intro
    val fadeAlpha by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(900),
        label = ""
    )

    // Scanlines
    val scanBrush = Brush.verticalGradient(
        listOf(
            Color(0x2200FF00),
            Color.Transparent,
            Color(0x2200FF00)
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .graphicsLayer {
                // CRT curvature effect
                transformOrigin = TransformOrigin(0.5f, 0.5f)
                scaleX = 1.06f
                scaleY = 1.08f
            }
            .alpha(fadeAlpha)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(18.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            printedLines.forEach { line ->
                Text(
                    text = line,
                    color = green,
                    fontSize = 18.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(Modifier.height(4.dp))
            }

            if (cursorVisible) {
                Text(
                    text = ">",
                    color = green,
                    fontSize = 20.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Black
                )
            }
        }

        // Scanlines overlay
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(scanBrush)
                .alpha(alphaAnim)
        )
    }
}
