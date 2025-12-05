package com.maazm7d.termuxhub.ui.components

import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Menu

@Composable
fun BottomNavBar(selectedIndex: Int = 0, onSelected: (Int) -> Unit) {
    NavigationBar {
        NavigationBarItem(selected = selectedIndex == 0, onClick = { onSelected(0) }, icon = { Icon(Icons.Default.List, contentDescription = "Tools") }, label = { Text("Tools") })
        NavigationBarItem(selected = selectedIndex == 1, onClick = { onSelected(1) }, icon = { Icon(Icons.Default.Menu, contentDescription = "Blogs") }, label = { Text("Blogs") })
    }
}