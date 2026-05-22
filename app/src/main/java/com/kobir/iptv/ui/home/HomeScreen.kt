package com.kobir.iptv.ui.home

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.tv.foundation.lazy.TvLazyRow
import androidx.tv.foundation.lazy.items
import androidx.tv.material3.Card
import androidx.tv.material3.CardDefaults
import androidx.tv.material3.Text
import coil.compose.AsyncImage
import com.kobir.iptv.data.model.Channel
import com.kobir.iptv.ui.theme.TVAccent
import com.kobir.iptv.ui.theme.TVBackground
import com.kobir.iptv.ui.theme.TVFocusGlowLarge
import com.kobir.iptv.ui.theme.TVSecondary
import com.kobir.iptv.ui.theme.TVSurface
import com.kobir.iptv.ui.theme.TVSurfaceVariant

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HomeScreen(
    onChannelClick: (Long) -> Unit,
    onEpgClick: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val channels by viewModel.allChannels
    val categories by viewModel.categories
    val selectedCategory by viewModel.selectedCategory
    val favorites by viewModel.favoriteChannels

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(TVBackground)
    ) {
        Header(
            modifier = Modifier.padding(horizontal = 48.dp, vertical = 24.dp)
        )

        CategoryRow(
            categories = categories,
            selectedCategory = selectedCategory,
            onCategoryClick = { viewModel.selectCategory(it) },
            modifier = Modifier.padding(horizontal = 48.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (favorites.isNotEmpty()) {
            ChannelRow(
                title = "Favorites",
                channels = favorites,
                onChannelClick = onChannelClick,
                onFavoriteClick = { viewModel.toggleFavorite(it) }
            )
        }

        ChannelRow(
            title = if (selectedCategory != null) selectedCategory!! else "All Channels",
            channels = if (selectedCategory != null) {
                val filtered by viewModel.filteredChannels
                filtered
            } else channels,
            onChannelClick = onChannelClick,
            onFavoriteClick = { viewModel.toggleFavorite(it) }
        )
    }
}

@Composable
private fun Header(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.CenterStart
    ) {
        Column {
            Text(
                text = "Kobir IPTV",
                style = androidx.tv.material3.MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = "Full Free Apps, No Premium Subscription",
                style = androidx.tv.material3.MaterialTheme.typography.bodyLarge,
                color = TVAccent
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun CategoryRow(
    categories: List<String>,
    selectedCategory: String?,
    onCategoryClick: (String?) -> Unit,
    modifier: Modifier = Modifier
) {
    var localSelected by remember { mutableStateOf(selectedCategory) }

    Column(modifier = modifier) {
        Text(
            text = "Categories",
            style = androidx.tv.material3.MaterialTheme.typography.titleMedium,
            color = TVSecondary
        )

        Spacer(modifier = Modifier.height(8.dp))

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            CategoryChip(
                label = "All",
                selected = localSelected == null,
                onClick = {
                    localSelected = null
                    onCategoryClick(null)
                }
            )
            categories.forEach { category ->
                CategoryChip(
                    label = category,
                    selected = localSelected == category,
                    onClick = {
                        localSelected = category
                        onCategoryClick(category)
                    }
                )
            }
        }
    }
}

@Composable
private fun CategoryChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    var isFocused by remember { mutableStateOf(false) }

    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(
            containerColor = if (selected) TVAccent else TVSurfaceVariant
        ),
        scale = CardDefaults.scale(focusedScale = 1.08f),
        modifier = Modifier
            .onFocusChanged { isFocused = it.isFocused }
    ) {
        Text(
            text = label,
            style = androidx.tv.material3.MaterialTheme.typography.labelLarge,
            color = if (selected) Color.White else TVSecondary,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)
        )
    }
}

@Composable
fun ChannelRow(
    title: String,
    channels: List<Channel>,
    onChannelClick: (Long) -> Unit,
    onFavoriteClick: ((Channel) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    if (channels.isEmpty()) return

    Column(modifier = modifier) {
        Text(
            text = title,
            style = androidx.tv.material3.MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.SemiBold,
            color = Color.White,
            modifier = Modifier.padding(horizontal = 48.dp, vertical = 12.dp)
        )

        TvLazyRow(
            contentPadding = PaddingValues(horizontal = 48.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(channels.take(20)) { channel ->
                ChannelCard(
                    channel = channel,
                    onClick = { onChannelClick(channel.id) },
                    onFavoriteClick = onFavoriteClick?.let { { it(channel) } }
                )
            }
        }
    }
}

@Composable
fun ChannelCard(
    channel: Channel,
    onClick: () -> Unit,
    onFavoriteClick: (() -> Unit)? = null
) {
    var isFocused by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isFocused) 1.1f else 1f,
        label = "cardScale"
    )
    val alpha by animateFloatAsState(
        targetValue = if (isFocused) 1f else 0.85f,
        label = "cardAlpha"
    )

    Card(
        onClick = onClick,
        modifier = Modifier
            .width(240.dp)
            .scale(scale)
            .alpha(alpha)
            .onFocusChanged { isFocused = it.isFocused }
            .then(
                if (isFocused) {
                    Modifier.drawWithContent {
                        drawContent()
                        drawRect(
                            brush = Brush.radialGradient(
                                colors = listOf(TVFocusGlowLarge, Color.Transparent),
                                radius = 0.8f
                            )
                        )
                    }
                } else Modifier
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = TVSurface),
        scale = CardDefaults.scale(focusedScale = 1f)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .width(240.dp)
                    .height(140.dp)
                    .background(TVSurfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                if (!channel.logoUrl.isNullOrBlank()) {
                    AsyncImage(
                        model = channel.logoUrl,
                        contentDescription = channel.name,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .width(120.dp)
                            .height(120.dp)
                    )
                } else {
                    Text(
                        text = channel.name.take(2).uppercase(),
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = TVAccent
                    )
                }
            }

            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                Text(
                    text = channel.name,
                    style = androidx.tv.material3.MaterialTheme.typography.titleSmall,
                    color = Color.White,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                if (channel.category != null) {
                    Text(
                        text = channel.category,
                        style = androidx.tv.material3.MaterialTheme.typography.bodySmall,
                        color = TVSecondary,
                        maxLines = 1
                    )
                }
            }
        }
    }
}
