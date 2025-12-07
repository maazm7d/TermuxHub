package com.maazm7d.termuxhub.ui.screens.splash

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.delay
import kotlin.math.pow
import kotlin.math.sqrt
import androidx.compose.ui.Alignment

@Composable
fun SplashScreen(onFinished: () -> Unit) {
    val particleCount = 300
    val particles = remember { mutableStateListOf<Particle>() }

    // Target shape points for Termux ">_"
    val targetPoints = remember { generateTargetPoints() }

    // Create random starting particles
    LaunchedEffect(Unit) {
        particles.clear()
        repeat(particleCount) {
            particles += Particle(
                startX = (-600..600).random().toFloat(),
                startY = (-600..600).random().toFloat(),
                target = targetPoints.random()
            )
        }
    }

    // Movement animation
    LaunchedEffect(Unit) {
        repeat(180) {   // approx 3 seconds of motion
            particles.forEach { it.moveTowardsTarget() }
            delay(16)
        }

        delay(500) // hold shape for half second
        onFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            particles.forEach { particle ->
                drawCircle(
                    color = Color.Black,
                    radius = 3f,
                    center = Offset(
                        x = size.width / 2 + particle.x,
                        y = size.height / 2 + particle.y
                    )
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
        val speed = 0.07f
        val dx = target.x - x
        val dy = target.y - y
        val dist = sqrt(dx.pow(2) + dy.pow(2))

        if (dist > 1f) {
            x += dx * speed
            y += dy * speed
        }
    }
}

// Build ">_" shape points
fun generateTargetPoints(): List<Offset> {
    val points = mutableListOf<Offset>()
    val pixel = 18f

    val shape = listOf(
        // >
        Offset(0f, -40f), Offset(15f, -25f), Offset(30f, -10f),
        Offset(15f, 5f), Offset(0f, 20f),

        // _
        Offset(-10f, 40f), Offset(10f, 40f), Offset(30f, 40f)
    )

    shape.forEach { p ->
        points += Offset(p.x * pixel / 20f, p.y * pixel / 20f)
    }

    return points
}
