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
import androidx.compose.ui.draw.alpha

@Composable
fun SplashScreen(onFinished: () -> Unit) {
    val totalParticles = 1200 // more particles overall
    val particles = remember { mutableStateListOf<Particle>() }

    val targetPoints = remember { generateTargetPointsDense() }

    var animationAlpha by remember { mutableStateOf(1f) }
    var glitchOffset by remember { mutableStateOf(Offset(0f, 0f)) }

    // Initialize random particles
    LaunchedEffect(Unit) {
        particles.clear()
        repeat(totalParticles) {
            particles += Particle(
                startX = Random.nextFloat() * 2000f - 1000f,
                startY = Random.nextFloat() * 2000f - 1000f,
                target = targetPoints.random()
            )
        }
    }

    // Animate particles
    LaunchedEffect(Unit) {
        val steps = 90 // ~1.5s
        repeat(steps) {
            particles.forEach { it.moveTowardsTarget() }
            delay(16)
        }

        // Glitch effect
        repeat(6) {
            glitchOffset = Offset(Random.nextFloat() * 20 - 10, Random.nextFloat() * 20 - 10)
            delay(50)
            glitchOffset = Offset(0f, 0f)
            delay(50)
        }

        // Fade-out
        val fadeSteps = 30
        repeat(fadeSteps) {
            animationAlpha -= 1f / fadeSteps
            delay(16)
        }

        onFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier
            .fillMaxSize()
            .alpha(animationAlpha)
        ) {
            val centerX = size.width / 2
            val centerY = size.height / 2

            particles.forEach { particle ->
                drawCircle(
                    color = Color.Black,
                    radius = 3f,
                    center = Offset(
                        centerX + particle.x + glitchOffset.x,
                        centerY + particle.y + glitchOffset.y
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
        val speed = 0.18f
        val dx = target.x - x
        val dy = target.y - y
        val dist = sqrt(dx.pow(2) + dy.pow(2))

        if (dist > 1f) {
            x += dx * speed
            y += dy * speed
        }
    }
}

// Dense Termux ">_" shape
fun generateTargetPointsDense(): List<Offset> {
    val points = mutableListOf<Offset>()
    val scale = 25f

    // Build '>'
    for (i in 0..40 step 2) {
        points += Offset(i.toFloat() - 20f, -40f + i)          // diagonal /
        points += Offset(i.toFloat() - 20f, -20f + i/2f)      // thicker diagonal
    }

    // Build '_', right under '>'
    for (i in -15..40 step 2) {
        points += Offset(i.toFloat(), 45f) // underscore line
    }

    return points
}
