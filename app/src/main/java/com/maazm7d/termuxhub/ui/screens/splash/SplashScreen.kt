package com.maazm7d.termuxhub.ui.screens.splash

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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

    val particleCount = 900
    var morphing by remember { mutableStateOf(false) }
    var glitch by remember { mutableStateOf(false) }

    val fadeOut = remember { Animatable(1f) }

    val targets = remember { targetsForTermuxShape() }

    // create all particle objects
    val particles = remember {
        List(particleCount) { Particle.randomParticle(targets) }
    }

    // *** Animation timeline ***
    LaunchedEffect(Unit) {
        delay(900)  // floating stage
        morphing = true

        particles.forEachIndexed { index, p ->
            launch {
                delay(index * 2L) // staggered magnet pull
                p.x.animateTo(p.target.x, spring(dampingRatio = 0.6f, stiffness = 80f))
                p.y.animateTo(p.target.y, spring(dampingRatio = 0.6f, stiffness = 80f))
            }
        }

        delay(1600)
        glitch = true
        delay(300)
        fadeOut.animateTo(0f, tween(600))

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
            val target = targets.random()
            return Particle(
                start = start,
                target = target,
                x = Animatable(start.x),
                y = Animatable(start.y)
            )
        }
    }
}

/* pixel mapped points for ">_" icon */
fun targetsForTermuxShape(): List<Offset> {
    val points = mutableListOf<Offset>()

    val cx = 540f
    val cy = 1200f

    // character: >
    for (i in 0..38) {
        points += Offset(cx - 140 + i * 6f, cy - 70 + i * 3f)
        points += Offset(cx - 140 + i * 6f, cy + 70 - i * 3f)
    }

    // character: _
    for (i in 0..80) {
        points += Offset(cx - 60 + i * 4f, cy + 120)
    }

    return points
}
