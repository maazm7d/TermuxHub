package com.maazm7d.termuxhub.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.maazm7d.termuxhub.R

@Composable
fun AppDrawer(
    onMyToolsClick: () -> Unit = {},
    onFavoritesClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

        // -------- Drawer Header --------
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .background(Color.Black),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher),  // fixed drawable resource
                contentDescription = "App Icon",
                modifier = Modifier
                    .size(95.dp)
                    .clip(
                        RoundedCornerShape(
                            topStart = 50.dp,
                            topEnd = 50.dp,
                            bottomStart = 0.dp,
                            bottomEnd = 0.dp
                        )
                    )
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // -------- Drawer Items --------
        DrawerItem(
            icon = Icons.Default.List,
            text = "My Tools",
            onClick = onMyToolsClick
        )

        DrawerItem(
            icon = Icons.Default.Favorite,
            text = "Favorites",
            onClick = onFavoritesClick
        )

        DrawerItem(
            icon = Icons.Default.Settings,
            text = "Settings",
            onClick = onSettingsClick
        )

        Spacer(modifier = Modifier.weight(1f))

        Divider(thickness = 1.dp, color = Color.Gray.copy(alpha = 0.3f))

        Text(
            text = "Termux Hub v1.0",
            modifier = Modifier.padding(16.dp),
            fontSize = 13.sp,
            color = Color.Gray
        )
    }
}

@Composable
fun DrawerItem(icon: androidx.compose.ui.graphics.vector.ImageVector, text: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 14.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = text,
            tint = Color.White
        )

        Spacer(modifier = Modifier.width(14.dp))

        Text(
            text = text,
            fontSize = 17.sp,
            color = Color.White
        )
    }
}
