package com.maazm7d.termuxhub.ui.screens.splash

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.draw.alpha
import kotlinx.coroutines.delay
import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.random.Random

@Composable
fun SplashScreen(onFinished: () -> Unit) {
    val totalParticles = 1400
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
        val steps = 90
        repeat(steps) {
            particles.forEach { it.moveTowardsTarget() }
            delay(16)
        }

        // Glitch effect
        repeat(6) {
            glitchOffset = Offset(
                Random.nextFloat() * 20 - 10,
                Random.nextFloat() * 20 - 10
            )
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
        Canvas(
            modifier = Modifier
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
        val speed = 0.20f
        val dx = target.x - x
        val dy = target.y - y
        val dist = sqrt(dx.pow(2) + dy.pow(2))

        if (dist > 0.8f) {
            x += dx * speed
            y += dy * speed
        }
    }
}

/**
 * Dense Termux-like ">_" made of points.
 *
 * Coordinate system is local around (0,0); splash centers it on screen.
 */
fun generateTargetPointsDense(): List<Offset> {
    val points = mutableListOf<Offset>()

    // overall size
    val scale = 3.5f

    // basic geometry for ">"
    val x0 = -24f * scale   // left base (common for both strokes)
    val y0 = -16f * scale   // upper base
    val x1 = 16f * scale    // tip
    val y1 = 0f
    val x2 = -24f * scale   // lower base
    val y2 = 16f * scale

    // center offset to balance in canvas
    val cx = 0f
    val cy = -4f * scale

    // small thickness for strokes
    val halfThick = 3f

    // upper stroke (x0,y0) -> (x1,y1)
    for (i in 0..140) {
        val t = i / 140f
        val x = x0 + (x1 - x0) * t + cx
        val y = y0 + (y1 - y0) * t + cy
        points += Offset(x, y)
        points += Offset(x, y - halfThick)
        points += Offset(x, y + halfThick)
    }

    // lower stroke (x0,y2) -> (x1,y1)
    for (i in 0..140) {
        val t = i / 140f
        val x = x0 + (x1 - x0) * t + cx
        val y = y2 + (y1 - y2) * t + cy
        points += Offset(x, y)
        points += Offset(x, y - halfThick)
        points += Offset(x, y + halfThick)
    }

    // underscore "_"
    val underscoreWidth = 52f * scale
    val underscoreY = 26f * scale + cy
    val underscoreLeft = -2f * scale + cx   // slightly shifted right

    for (i in 0..150) {
        val t = i / 150f
        val x = underscoreLeft + underscoreWidth * t
        points += Offset(x, underscoreY)
        points += Offset(x, underscoreY - halfThick)
        points += Offset(x, underscoreY + halfThick)
    }

    return points
}
