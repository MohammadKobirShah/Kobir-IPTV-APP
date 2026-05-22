package com.kobir.iptv.ui.epg

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.tv.material3.Button
import androidx.tv.material3.ButtonDefaults
import androidx.tv.material3.Text
import com.kobir.iptv.data.model.Channel
import com.kobir.iptv.ui.theme.TVAccent
import com.kobir.iptv.ui.theme.TVBackground
import com.kobir.iptv.ui.theme.TVSecondary
import com.kobir.iptv.ui.theme.TVSurface
import com.kobir.iptv.ui.theme.TVSurfaceVariant

@Composable
fun EPGScreen(
    onBack: () -> Unit,
    viewModel: EPGViewModel = hiltViewModel()
) {
    val channels by viewModel.channels
    val categories by viewModel.categories

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(TVBackground)
            .padding(48.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = onBack,
                colors = ButtonDefaults.colors(
                    containerColor = Color.Transparent,
                    focusedContainerColor = TVAccent.copy(alpha = 0.3f)
                )
            ) {
                Text(
                    text = "← Back",
                    color = Color.White,
                    style = androidx.tv.material3.MaterialTheme.typography.titleSmall
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = "TV Guide",
                style = androidx.tv.material3.MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Time header row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        ) {
            Box(
                modifier = Modifier
                    .width(200.dp)
                    .padding(8.dp)
            ) {
                Text(
                    text = "Channel",
                    style = androidx.tv.material3.MaterialTheme.typography.labelLarge,
                    color = TVSecondary,
                    fontWeight = FontWeight.SemiBold
                )
            }
            TimeHeaders()
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(channels) { channel ->
                EPGRow(channel = channel)
            }
        }
    }
}

@Composable
private fun TimeHeaders() {
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier.fillMaxWidth()
    ) {
        val hours = listOf("6AM", "9AM", "12PM", "3PM", "6PM", "9PM", "12AM")
        hours.forEach { hour ->
            Text(
                text = hour,
                style = androidx.tv.material3.MaterialTheme.typography.bodySmall,
                color = TVSecondary,
                modifier = Modifier.width(80.dp)
            )
        }
    }
}

@Composable
private fun EPGRow(channel: Channel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(TVSurface, shape = RoundedCornerShape(8.dp)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .width(200.dp)
                .padding(horizontal = 12.dp)
        ) {
            Text(
                text = channel.name,
                style = androidx.tv.material3.MaterialTheme.typography.bodyLarge,
                color = Color.White,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        // Mock EPG program blocks (would be replaced with real EPG data)
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .width(160.dp)
                    .height(44.dp)
                    .background(TVSurfaceVariant, shape = RoundedCornerShape(4.dp))
                    .padding(8.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = "Programming",
                    style = androidx.tv.material3.MaterialTheme.typography.bodySmall,
                    color = TVSecondary,
                    maxLines = 1
                )
            }
            Box(
                modifier = Modifier
                    .width(200.dp)
                    .height(44.dp)
                    .background(TVAccent.copy(alpha = 0.2f), shape = RoundedCornerShape(4.dp))
                    .padding(8.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = "Now Playing",
                    style = androidx.tv.material3.MaterialTheme.typography.bodySmall,
                    color = TVAccent,
                    maxLines = 1
                )
            }
            Box(
                modifier = Modifier
                    .width(160.dp)
                    .height(44.dp)
                    .background(TVSurfaceVariant, shape = RoundedCornerShape(4.dp))
                    .padding(8.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = "Up Next",
                    style = androidx.tv.material3.MaterialTheme.typography.bodySmall,
                    color = TVSecondary,
                    maxLines = 1
                )
            }
        }
    }
}
