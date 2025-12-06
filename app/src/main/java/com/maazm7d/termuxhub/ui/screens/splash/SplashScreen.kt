package com.maazm7d.termuxhub.ui.screens.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import com.maazm7d.termuxhub.R

/**
 * SplashScreen UI - uses Hilt to obtain SplashViewModel internally.
 * onFinished(success) will be called after load completes.
 */
@Composable
fun SplashScreen(
    onFinished: (Boolean) -> Unit
) {
    // obtain Hilt ViewModel here (so caller doesn't need to pass the VM)
    val vm: SplashViewModel = hiltViewModel()

    // start loading when composed
    androidx.compose.runtime.LaunchedEffect(Unit) {
        vm.load { success -> onFinished(success) }
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.mipmap.termux),
                contentDescription = "Termux Hub",
                modifier = Modifier.size(96.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Termux Hub", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(12.dp))
            CircularProgressIndicator()
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "Loading tools...",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
        }
    }
}
