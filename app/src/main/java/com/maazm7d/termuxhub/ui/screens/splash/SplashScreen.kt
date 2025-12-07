package com.maazm7d.termuxhub.ui.screens.splash

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlin.math.hypot
import kotlin.random.Random

@Composable
fun SplashScreen(onFinished: () -> Unit) {
    val particleCount = 650

    var phase by remember { mutableStateOf(0) }   // 0 = float, 1 = magnet, 2 = complete
    val particles = remember { mutableStateListOf<Particle>() }

    // Termux icon mask (| shape)
    val targetPoints = remember { mutableStateListOf<Pair<Float, Float>>() }

    val density = LocalDensity.current

    LaunchedEffect(Unit) {
        // Generate particles
        particles.clear()
        repeat(particleCount) {
            particles += Particle(
                x = Random.nextFloat() * 1.1f - 0.05f,
                y = Random.nextFloat() * 1.1f - 0.05f,
                vx = Random.nextFloat() * 0.002f - 0.001f,
                vy = Random.nextFloat() * 0.002f - 0.001f
            )
        }

        // Build mask points for Termux | icon (narrow vertical bar)
        targetPoints.clear()
        for (i in 0 until 230) {
            val px = 0.48f + Random.nextFloat() * 0.04f
            val py = 0.25f + Random.nextFloat() * 0.50f
            targetPoints += px to py
        }

        delay(1800)
        phase = 1            // → attract particles
        delay(2300)
        phase = 2            // → fade transition
        delay(600)
        onFinished()
    }

    val alpha by animateFloatAsState(if (phase == 2) 0f else 1f, tween(900))

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize().alpha(alpha)) {

            particles.forEachIndexed { i, p ->
                if (phase == 0) {
                    // free float
                    p.x += p.vx
                    p.y += p.vy
                }
                if (phase == 1) {
                    // attract to icon points
                    val (tx, ty) = targetPoints[i % targetPoints.size]
                    val dx = tx - p.x
                    val dy = ty - p.y
                    val dist = hypot(dx, dy)
                    val force = 0.015f
                    p.x += dx * force
                    p.y += dy * force
                }

                drawCircle(
                    color = Color.Black,
                    radius = 2.2f,
                    center = androidx.compose.ui.geometry.Offset(
                        size.width * p.x,
                        size.height * p.y
                    )
                )
            }
        }
    }
}

data class Particle(
    var x: Float,
    var y: Float,
    var vx: Float,
    var vy: Float
)
