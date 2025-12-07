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
    val totalParticles = 1400 // slight increase for smoother shape
    val particles = remember { mutableStateListOf<Particle>() }

    // target points now approximate a bold Termux-style ">_" glyph
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
    val scale = 3.2f                // tweak to fit your screen density
    val chevronLength = 80f * scale
    val chevronThickness = 14f * scale
    val chevronGap = 10f * scale    // gap between upper/lower strokes
    val barWidth = 90f * scale
    val barHeight = 14f * scale
    val barYOffset = 40f * scale    // distance of "_" from chevron center

    // center offsets so shape is nicely balanced
    val centerOffsetX = -5f * scale
    val centerOffsetY = -5f * scale

    // --- Build ">" as two thick strokes (upper and lower) ---

    // Upper stroke
    for (t in 0..100) {
        val f = t / 100f
        val x = -chevronLength / 2f + chevronLength * f
        val y = -chevronLength / 4f + chevronLength * f / 2f

        // thickness: offset perpendicular to line
        for (k in -chevronThickness.toInt()..chevronThickness.toInt() step 3) {
            val px = x
            val py = y + k
            points += Offset(
                px + centerOffsetX,
                py + centerOffsetY - chevronGap / 2f
            )
        }
    }

    // Lower stroke
    for (t in 0..100) {
        val f = t / 100f
        val x = -chevronLength / 2f + chevronLength * f
        val y = chevronLength / 4f - chevronLength * f / 2f

        for (k in -chevronThickness.toInt()..chevronThickness.toInt() step 3) {
            val px = x
            val py = y + k
            points += Offset(
                px + centerOffsetX,
                py + centerOffsetY + chevronGap / 2f
            )
        }
    }

    // --- Build "_" as a rounded thick bar ---

    val left = -barWidth / 2f + centerOffsetX + 20f
    val right = barWidth / 2f + centerOffsetX + 20f
    val top = barYOffset - barHeight / 2f + centerOffsetY
    val bottom = barYOffset + barHeight / 2f + centerOffsetY
    val radius = barHeight / 2f

    // horizontal run
    for (x in left.toInt()..right.toInt() step 3) {
        for (y in top.toInt()..bottom.toInt() step 3) {
            points += Offset(x.toFloat(), y.toFloat())
        }
    }

    // rounded ends (left & right semicircles)
    for (dx in -radius.toInt()..radius.toInt() step 2) {
        for (dy in -radius.toInt()..radius.toInt() step 2) {
            if (dx * dx + dy * dy <= radius * radius) {
                // left
                points += Offset(left + dx, (top + bottom) / 2f + dy)
                // right
                points += Offset(right + dx, (top + bottom) / 2f + dy)
            }
        }
    }

    return points
}
