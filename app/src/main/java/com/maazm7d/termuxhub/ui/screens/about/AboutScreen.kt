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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.maazm7d.termuxhub.R
import androidx.compose.material.icons.filled.Gavel

@Composable
fun AboutScreen() {
    val context = LocalContext.current
    val uriHandler = LocalUriHandler.current

    val appVersion = try {
        context.packageManager
            .getPackageInfo(context.packageName, 0)
            .versionName ?: "Unknown"
    } catch (_: PackageManager.NameNotFoundException) {
        "Unknown"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // App Icon
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

        // App Information
        SectionCard(
            title = "App Information",
            icon = Icons.Default.Info
        ) {
            InfoItem(Icons.Default.Android, "Version", appVersion)
            InfoItem(Icons.Default.Update, "Last Updated", "Jan 2026")
            InfoItem(Icons.Default.Person, "Developer", "maazm7d")
            InfoItem(Icons.Default.Code, "Platform", "Android / Termux")
            InfoItem(Icons.Default.Public, "Open Source", "Yes")
            InfoItem(Icons.Default.Gavel, "License", "GPL-3.0")
        }

        // Links
        SectionCard(
            title = "Links",
            icon = Icons.Default.Link
        ) {
            LinkItem(Icons.Default.Code, "Open Source Repository") {
                uriHandler.openUri("https://github.com/maazm7d/TermuxHub")
            }

            LinkItem(Icons.Default.BugReport, "Issue Tracker") {
                uriHandler.openUri("https://github.com/maazm7d/TermuxHub/issues")
            }

            LinkItem(Icons.Default.Person, "Developer GitHub") {
                uriHandler.openUri("https://github.com/maazm7d")
            }

            Divider(modifier = Modifier.padding(vertical = 8.dp))

            LinkItem(Icons.Default.AttachMoney, "Donate via PayPal") {
                uriHandler.openUri("https://paypal.me/maazm7d")
            }

            LinkItem(Icons.Default.Coffee, "Buy Me a Coffee") {
                uriHandler.openUri("https://buymeacoffee.com/maazm7d")
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

/* ───────────── Reusable Components ───────────── */

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
            containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.08f)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(icon, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = title, style = MaterialTheme.typography.titleMedium)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Column(modifier = Modifier.fillMaxWidth()) {
                content()
            }
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
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 0.dp) // optional, cleaner alignment
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(12.dp))
            Text(text, style = MaterialTheme.typography.bodyMedium)
        }
    }
}
