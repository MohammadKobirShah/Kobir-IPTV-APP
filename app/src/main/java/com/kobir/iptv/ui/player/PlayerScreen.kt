package com.kobir.iptv.ui.player

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.compose.AndroidViewPlayer
import androidx.tv.material3.Button
import androidx.tv.material3.ButtonDefaults
import androidx.tv.material3.Text
import com.kobir.iptv.ui.theme.TVAccent
import com.kobir.iptv.ui.theme.TVSecondary
import com.kobir.iptv.ui.theme.TVSurfaceVariant
import kotlinx.coroutines.delay

@Composable
fun PlayerScreen(
    channelId: Long,
    onBack: () -> Unit,
    viewModel: PlayerViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState
    var showControls by remember { mutableStateOf(true) }
    var hideControlsJob by remember { mutableStateOf(false) }

    val context = androidx.compose.ui.platform.LocalContext.current
    val player = remember {
        ExoPlayer.Builder(context).build().apply {
            playWhenReady = true
        }
    }

    LaunchedEffect(channelId) {
        viewModel.loadChannel(channelId)
    }

    LaunchedEffect(uiState.channel?.url) {
        val url = uiState.channel?.url ?: return@LaunchedEffect
        val mediaItem = MediaItem.Builder()
            .setUri(url)
            .setMediaMetadata(
                androidx.media3.common.MediaMetadata.Builder()
                    .setTitle(uiState.channel?.name)
                    .build()
            )
            .build()
        player.setMediaItem(mediaItem)
        player.prepare()
    }

    // Auto-hide controls
    LaunchedEffect(showControls) {
        if (showControls) {
            delay(4000)
            showControls = false
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            player.release()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        AndroidViewPlayer(
            player = player,
            modifier = Modifier.fillMaxSize(),
            useController = false
        )

        // Top gradient overlay
        AnimatedVisibility(
            visible = showControls,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Black.copy(alpha = 0.8f), Color.Transparent)
                        )
                    )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    verticalAlignment = Alignment.CenterVertically
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

                    Column {
                        Text(
                            text = uiState.channel?.name ?: "Loading...",
                            style = androidx.tv.material3.MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        if (uiState.channel?.category != null) {
                            Text(
                                text = uiState.channel!!.category!!,
                                style = androidx.tv.material3.MaterialTheme.typography.bodyMedium,
                                color = TVSecondary
                            )
                        }
                    }
                }
            }
        }

        // Bottom controls overlay
        AnimatedVisibility(
            visible = showControls,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.8f))
                        )
                    )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 48.dp, vertical = 32.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(24.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = { viewModel.playPrevious() },
                            colors = ButtonDefaults.colors(
                                containerColor = TVSurfaceVariant,
                                focusedContainerColor = TVAccent
                            )
                        ) {
                            Text(
                                text = "◀ Previous",
                                color = Color.White,
                                style = androidx.tv.material3.MaterialTheme.typography.labelLarge
                            )
                        }

                        Button(
                            onClick = {
                                player.playWhenReady = !player.playWhenReady
                            },
                            colors = ButtonDefaults.colors(
                                containerColor = TVAccent,
                                focusedContainerColor = TVAccent.copy(alpha = 0.8f)
                            )
                        ) {
                            Text(
                                text = if (player.playWhenReady) "⏸ Pause" else "▶ Play",
                                color = Color.White,
                                style = androidx.tv.material3.MaterialTheme.typography.labelLarge
                            )
                        }

                        Button(
                            onClick = { viewModel.playNext() },
                            colors = ButtonDefaults.colors(
                                containerColor = TVSurfaceVariant,
                                focusedContainerColor = TVAccent
                            )
                        ) {
                            Text(
                                text = "Next ▶",
                                color = Color.White,
                                style = androidx.tv.material3.MaterialTheme.typography.labelLarge
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "${uiState.currentIndex + 1} of ${uiState.allChannels.size}",
                            style = androidx.tv.material3.MaterialTheme.typography.bodyMedium,
                            color = TVSecondary
                        )
                    }
                }
            }
        }

        // Show channel info briefly on channel change
        if (!showControls) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(24.dp)
                    .alpha(0.9f)
            ) {
                Column {
                    Text(
                        text = uiState.channel?.name ?: "",
                        style = androidx.tv.material3.MaterialTheme.typography.titleSmall,
                        color = Color.White
                    )
                }
            }
        }
    }
}
