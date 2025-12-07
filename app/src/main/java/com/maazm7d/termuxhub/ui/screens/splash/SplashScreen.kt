package com.maazm7d.termuxhub.ui.screens.splash

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawCircle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.delay
import kotlin.random.Random

@Composable
fun SplashScreen(
    onFinished: () -> Unit
) {
    val vm: SplashViewModel = hiltViewModel()

    val particleCount = 350
    var particles by remember { mutableStateOf(generateParticles(particleCount)) }
    var morphToIcon by remember { mutableStateOf(false) }
    var fadeOut by remember { mutableStateOf(false) }

    val fadeAlpha by animateFloatAsState(
        targetValue = if (fadeOut) 0f else 1f,
        animationSpec = tween(durationMillis = 900, easing = LinearEasing),
        finishedListener = { onFinished() }
    )

    LaunchedEffect(Unit) {
        delay(1800)
        morphToIcon = true

        delay(1700)
        fadeOut = true

        vm.load { }
    }

    LaunchedEffect(morphToIcon) {
        if (morphToIcon) {
            particles = particles.mapIndexed { index, particle ->
                particle.copy(
                    targetX = termuxShape[index % termuxShape.size].first,
                    targetY = termuxShape[index % termuxShape.size].second
                )
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .alpha(fadeAlpha),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            particles.forEach { particle ->
                val animX by animateFloatAsState(
                    targetValue = particle.targetX,
                    animationSpec = tween(1600, easing = FastOutSlowInEasing)
                )
                val animY by animateFloatAsState(
                    targetValue = particle.targetY,
                    animationSpec = tween(1600, easing = FastOutSlowInEasing)
                )

                drawCircle(
                    color = Color.Black,
                    radius = 3f,
                    center = androidx.compose.ui.geometry.Offset(animX, animY)
                )
            }
        }
    }
}

/*** DATA STRUCT ***/
data class Particle(
    val id: Int,
    val targetX: Float,
    val targetY: Float
)

/*** INITIAL RANDOM POSITIONS ***/
fun generateParticles(count: Int): List<Particle> {
    return List(count) { i ->
        Particle(
            id = i,
            targetX = Random.nextFloat() * 1080f,
            targetY = Random.nextFloat() * 2400f
        )
    }
}

/*** TERMUX ICON SHAPE COORDINATES (Pixel cloud target map) ***/
val termuxShape = listOf(
    540f to 700f,
    540f to 720f,
    540f to 740f,
    540f to 760f,
    545f to 720f,
    550f to 740f,
    555f to 760f,
    560f to 780f,
    565f to 800f,
    570f to 820f,
    575f to 840f,
    580f to 860f,
    590f to 880f,
    600f to 900f,
    610f to 920f,
    620f to 940f,
    630f to 960f,
    640f to 980f,
    650f to 1000f
)
