package com.maazm7d.termuxhub.ui.screens.splash

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.maazm7d.termuxhub.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onFinished: (Boolean) -> Unit
) {
    val vm: SplashViewModel = hiltViewModel()

    // Animation trigger state
    var startAnimation by remember { mutableStateOf(false) }

    // Animate scale from 0.6f to 1f in 800ms
    val scale = animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0.6f,
        animationSpec = tween(
            durationMillis = 800,
            easing = FastOutSlowInEasing
        ),
        label = ""
    )

    // Start animation when composed
    LaunchedEffect(Unit) {
        startAnimation = true
        delay(1200)  // Allow animation to finish before loading data
        vm.load { success -> onFinished(success) }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Animated Logo
            Image(
                painter = painterResource(id = R.drawable.splashlogo),
                contentDescription = "Termux Hub",
                modifier = Modifier
                    .scale(scale.value)
                    .size(180.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                "Termux Hub",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(20.dp))

            CircularProgressIndicator(
                modifier = Modifier.size(42.dp),
                strokeWidth = 4.dp
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                "Loading tools...",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
        }
    }
}
