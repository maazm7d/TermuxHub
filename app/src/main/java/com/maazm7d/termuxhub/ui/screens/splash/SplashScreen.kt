package com.maazm7d.termuxhub.ui.screens.splash

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.Image
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.maazm7d.termuxhub.R
import kotlinx.coroutines.delay
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun SplashScreen(onFinished: (Boolean) -> Unit) {
    val vm: SplashViewModel = viewModel()
    var started by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        if (!started) {
            started = true
            vm.load { success ->
                onFinished(success)
            }
        }
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize().padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // App icon - use mipmap/ic_launcher
            Image(painter = painterResource(id = R.mipmap.ic_launcher), contentDescription = "Termux Hub", modifier = Modifier.size(96.dp))
            Spacer(modifier = Modifier.height(16.dp))
            Text("Termux Hub", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(12.dp))
            CircularProgressIndicator()
            Spacer(modifier = Modifier.height(8.dp))
            Text("Loading tools...", style = MaterialTheme.typography.bodyMedium, textAlign = TextAlign.Center)
        }
    }
}