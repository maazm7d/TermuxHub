package com.maazm7d.termuxhub.ui.screens.splash

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onFinished: () -> Unit) {

    var showIcon by remember { mutableStateOf(false) }
    var showText by remember { mutableStateOf(false) }

    val iconAlpha by animateFloatAsState(
        targetValue = if (showIcon) 1f else 0f,
        label = "iconAlpha"
    )

    val textAlpha by animateFloatAsState(
        targetValue = if (showText) 1f else 0f,
        label = "textAlpha"
    )

    LaunchedEffect(Unit) {
        delay(200)
        showIcon = true          // icon appears
        delay(350)
        showText = true          // text appears after
        delay(700)
        onFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Termux-like icon
            Text(
                text = ">_",
                fontSize = 56.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.alpha(iconAlpha)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // App name
            Text(
                text = "Termux Hub",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                textAlign = TextAlign.Center,
                modifier = Modifier.alpha(textAlpha)
            )
        }
    }
}
