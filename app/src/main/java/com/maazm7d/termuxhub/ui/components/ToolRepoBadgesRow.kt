package com.maazm7d.termuxhub.ui.components

import android.text.format.DateUtils
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BugReport
import androidx.compose.material.icons.outlined.CallSplit
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Merge
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.maazm7d.termuxhub.domain.model.ToolDetails

@Composable
fun ToolRepoBadgesRow(tool: ToolDetails) {
    val badges = buildList {
        add(RepoBadgeData(Icons.Outlined.StarOutline, tool.stars.toString()))
        add(RepoBadgeData(Icons.Outlined.CallSplit, tool.forks.toString()))
        add(RepoBadgeData(Icons.Outlined.BugReport, tool.issues.toString()))
        add(RepoBadgeData(Icons.Outlined.Merge, tool.pullRequests.toString()))
        tool.license?.let { add(RepoBadgeData(Icons.Outlined.Description, it)) }
        add(
            RepoBadgeData(
                Icons.Outlined.Schedule,
                DateUtils.getRelativeTimeSpanString(
                    tool.lastUpdated,
                    System.currentTimeMillis(),
                    DateUtils.DAY_IN_MILLIS
                ).toString()
            )
        )
    }

    LazyRow(
        modifier = Modifier.padding(vertical = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        items(badges, key = { it.text + it.icon.hashCode() }) { badge ->
            RepoBadge(icon = badge.icon, text = badge.text)
        }
    }
}

private data class RepoBadgeData(val icon: ImageVector, val text: String)

@Composable
private fun RepoBadge(
    icon: ImageVector,
    text: String
) {
    val badgeBackground = Color(0xFFE7F1FF)
    val badgeContent = Color(0xFF1E3A5F)

    Surface(
        shape = RoundedCornerShape(50), // pill shape
        color = badgeBackground,
        tonalElevation = 2.dp,
        shadowElevation = 1.dp
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = badgeContent
            )

            Spacer(modifier = Modifier.width(6.dp))

            Text(
                text = text,
                style = MaterialTheme.typography.labelMedium,
                color = badgeContent
            )
        }
    }
}
