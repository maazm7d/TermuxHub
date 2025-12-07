package com.maazm7d.termuxhub.ui.screens.splash

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.alpha
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.delay
import kotlin.random.Random
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.Animatable

@Composable
fun SplashScreen(
    onFinished: (Boolean) -> Unit
) {
    val vm: SplashViewModel = hiltViewModel()

    val particleCount = 850 // amount of pixels
    var morphing by remember { mutableStateOf(false) }
    var glitch by remember { mutableStateOf(false) }

    val fadeOut = remember { Animatable(1f) }

    // Generate all particles with random starting points and target shape points
    val particles = remember {
        List(particleCount) { Particle.randomParticle(targetsForTermuxShape()) }
    }

    // Sequence Flow
    LaunchedEffect(Unit) {
        delay(900)        // floating stage
        morphing = true   // attract to target shape
        delay(1600)       // allow morph to finish
        glitch = true     // glitch animation
        delay(400)
        fadeOut.animateTo(0f, tween(650))
        vm.load { success -> onFinished(success) }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .alpha(fadeOut.value),
        contentAlignment = Alignment.Center
    ) {

        Canvas(modifier = Modifier.fillMaxSize()) {
            particles.forEach { p ->

                val tx = if (morphing) p.target.x else p.start.x
                val ty = if (morphing) p.target.y else p.start.y

                // particle motion
                if (morphing) {
                    LaunchedEffect(Unit) {
                        p.x.animateTo(tx, spring(stiffness = Spring.StiffnessLow))
                        p.y.animateTo(ty, spring(stiffness = Spring.StiffnessLow))
                    }
                }

                // glitch vibration
                val jitterX = if (glitch) Random.nextFloat() * 12f - 6f else 0f
                val jitterY = if (glitch) Random.nextFloat() * 12f - 6f else 0f

                drawCircle(
                    color = Color.Black,
                    radius = 3f,
                    center = Offset(
                        p.x.value + jitterX,
                        p.y.value + jitterY
                    )
                )
            }
        }
    }
}

/* DATA MODEL */
data class Particle(
    val start: Offset,
    val target: Offset,
    val x: Animatable<Float, AnimationVector1D>,
    val y: Animatable<Float, AnimationVector1D>
) {
    companion object {
        fun randomParticle(targets: List<Offset>): Particle {
            val start = Offset(
                Random.nextFloat() * 1080f,
                Random.nextFloat() * 2400f
            )

            val randomTarget = targets.random()

            return Particle(
                start = start,
                target = randomTarget,
                x = Animatable(start.x),
                y = Animatable(start.y)
            )
        }
    }
}

/* Termux icon shape ">_" built by pixel points */
fun targetsForTermuxShape(): List<Offset> {
    val points = mutableListOf<Offset>()

    val centerX = 540f
    val centerY = 1200f

    // Character: >
    for (i in 0..38) {
        points += Offset(centerX - 120 + i * 5f, centerY - 70 + i * 3f)
        points += Offset(centerX - 120 + i * 5f, centerY + 70 - i * 3f)
    }

    // Character: _
    for (i in 0..70) {
        points += Offset(centerX - 50 + i * 4f, centerY + 120)
    }

    return points
}
