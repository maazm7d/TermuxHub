package com.maazm7d.termuxhub.ui.screens.splash

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.delay
import kotlin.math.pow
import kotlin.math.sqrt
import androidx.compose.ui.Alignment

@Composable
fun SplashScreen(onFinished: () -> Unit) {
    val vm: SplashViewModel = hiltViewModel()

    val particleCount = 300
    val particles = remember { mutableStateListOf<Particle>() }

    // target icon points (Termux >_)
    val targetPoints = remember {
        generateTargetPoints()
    }

    // initialize particles randomly
    LaunchedEffect(Unit) {
        particles.clear()
        repeat(particleCount) {
            particles += Particle(
                x = (-600..600).random().toFloat(),
                y = (-600..600).random().toFloat(),
                target = targetPoints.random()
            )
        }
    }

    // animate particles movement
    LaunchedEffect(Unit) {
        repeat(140) {  // smooth 2s movement
            particles.forEach { it.moveTowardsTarget() }
            delay(16)
        }

        delay(400) // final hold
        vm.load {
            onFinished()
        }
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
    var x: Float,
    var y: Float,
    val target: Offset
) {
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

// generate Termux ">_" pixel points
fun generateTargetPoints(): List<Offset> {
    val points = mutableListOf<Offset>()
    val pixel = 18f

    // builds > and _
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
