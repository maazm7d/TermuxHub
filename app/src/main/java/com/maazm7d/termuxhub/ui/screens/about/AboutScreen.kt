package com.maazm7d.termuxhub.ui.screens.about

import android.content.pm.PackageManager
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.maazm7d.termuxhub.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val uriHandler = LocalUriHandler.current

    val appVersion = try {
        context.packageManager
            .getPackageInfo(context.packageName, 0)
            .versionName ?: "Unknown"
    } catch (e: PackageManager.NameNotFoundException) {
        "Unknown"
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            TopAppBar(
                title = { Text("About") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // App Icon (Quircle)
            Image(
                painter = painterResource(id = R.drawable.ic_launcher),
                contentDescription = "App Icon",
                modifier = Modifier
                    .size(96.dp)
                    .clip(RoundedCornerShape(28.dp))
            )

            Spacer(modifier = Modifier.height(12.dp))

            // App Name
            Text(
                text = "Termux Hub",
                style = MaterialTheme.typography.headlineSmall
            )

            // Tagline
            Text(
                text = "Open-source hub for powerful Termux tools.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 6.dp, start = 24.dp, end = 24.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // App Info Card
            SectionCard(title = "App Information", icon = Icons.Default.Info) {
                InfoItem(Icons.Default.Android, "Version", appVersion)
                InfoItem(Icons.Default.Update, "Last Updated", "Dec 2025")
                InfoItem(Icons.Default.Person, "Main Developer", "Maaz M")
                InfoItem(Icons.Default.Code, "Platform", "Android / Termux")
                InfoItem(Icons.Default.Public, "Open Source", "Yes")
            }

            // Links Card
            SectionCard(title = "Links", icon = Icons.Default.Link) {
                LinkItem(
                    Icons.Default.Code,
                    "Open Source Repository"
                ) { uriHandler.openUri("https://github.com/maazm7d/TermuxHub") }

                LinkItem(
                    Icons.Default.BugReport,
                    "Issue Tracker"
                ) { uriHandler.openUri("https://github.com/maazm7d/TermuxHub/issues") }

                LinkItem(
                    Icons.Default.Person,
                    "Developer GitHub"
                ) { uriHandler.openUri("https://github.com/maazm7d") }
            }

            // License Card
            SectionCard(title = "License", icon = Icons.Default.Gavel) {
                InfoItem(
                    Icons.Default.Description,
                    "License Type",
                    "AGPL v3"
                )

                Text(
                    text = "This app is licensed under the GNU AGPL v3. " +
                            "Any modified version must also be open-sourced.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            // Donation Card
            SectionCard(title = "Support Development", icon = Icons.Default.Favorite) {
                LinkItem(
                    Icons.Default.AttachMoney,
                    "Donate via PayPal"
                ) {
                    uriHandler.openUri("https://paypal.me/yourlink")
                }

                LinkItem(
                    Icons.Default.Coffee,
                    "Buy Me a Coffee"
                ) {
                    uriHandler.openUri("https://buymeacoffee.com/yourlink")
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Made with ❤️ for the Termux community",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

/* ---------- Reusable Components ---------- */

@Composable
private fun SectionCard(
    title: String,
    icon: ImageVector,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Spacer(modifier = Modifier.height(12.dp))
            content()
        }
    }
}

@Composable
private fun InfoItem(
    icon: ImageVector,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(label, style = MaterialTheme.typography.bodySmall)
            Text(value, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
private fun LinkItem(
    icon: ImageVector,
    text: String,
    onClick: () -> Unit
) {
    TextButton(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(12.dp))
            Text(text, style = MaterialTheme.typography.bodyMedium)
        }
    }
}
