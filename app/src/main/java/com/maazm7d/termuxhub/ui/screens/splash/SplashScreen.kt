package com.maazm7d.termuxhub.ui.screens.splash

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.delay
import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.random.Random

@Composable
fun SplashScreen(onFinished: () -> Unit) {
    val particleCount = 800 // more particles for full screen
    val particles = remember { mutableStateListOf<Particle>() }

    // Target shape points for Termux ">_"
    val targetPoints = remember { generateTargetPoints() }

    // Initialize particles randomly across the screen
    LaunchedEffect(Unit) {
        particles.clear()
        repeat(particleCount) {
            particles += Particle(
                startX = Random.nextFloat() * 2000f - 1000f, // cover larger area
                startY = Random.nextFloat() * 2000f - 1000f,
                target = targetPoints.random()
            )
        }
    }

    // Animate particles quickly (max 1.5 sec)
    LaunchedEffect(Unit) {
        val steps = 90  // ~1.5 seconds at 16ms per frame
        repeat(steps) {
            particles.forEach { it.moveTowardsTarget() }
            delay(16)
        }
        delay(200) // brief hold for shape
        onFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val centerX = size.width / 2
            val centerY = size.height / 2

            particles.forEach { particle ->
                drawCircle(
                    color = Color.Black,
                    radius = 3f,
                    center = Offset(centerX + particle.x, centerY + particle.y)
                )
            }
        }
    }
}

data class Particle(
    val startX: Float,
    val startY: Float,
    val target: Offset
) {
    var x by mutableStateOf(startX)
    var y by mutableStateOf(startY)

    fun moveTowardsTarget() {
        val speed = 0.15f // faster convergence
        val dx = target.x - x
        val dy = target.y - y
        val dist = sqrt(dx.pow(2) + dy.pow(2))

        if (dist > 1f) {
            x += dx * speed
            y += dy * speed
        }
    }
}

// Build larger ">_" shape points
fun generateTargetPoints(): List<Offset> {
    val points = mutableListOf<Offset>()
    val pixel = 50f // increase size

    val shape = listOf(
        // >
        Offset(0f, -40f), Offset(20f, -20f), Offset(40f, 0f),
        Offset(20f, 20f), Offset(0f, 40f),

        // _
        Offset(-30f, 60f), Offset(0f, 60f), Offset(30f, 60f)
    )

    shape.forEach { p ->
        points += Offset(p.x * pixel / 20f, p.y * pixel / 20f)
    }

    return points
}
