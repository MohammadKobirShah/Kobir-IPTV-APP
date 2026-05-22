package com.kobir.iptv.ui.splash

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.tv.material3.Button
import androidx.tv.material3.ButtonDefaults
import androidx.tv.material3.Text
import com.kobir.iptv.ui.theme.TVAccent
import com.kobir.iptv.ui.theme.TVBackground
import com.kobir.iptv.ui.theme.TVOnSurfaceVariant
import com.kobir.iptv.ui.theme.TVSecondary
import com.kobir.iptv.ui.theme.TVSurfaceVariant
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onNavigateToSetup: () -> Unit,
    onNavigateToHome: () -> Unit,
    viewModel: SplashViewModel = hiltViewModel()
) {
    var startAnimation by remember { mutableStateOf(false) }
    val alphaAnim = animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 1200),
        label = "splashAlpha"
    ).value

    val hasChannels = viewModel.hasChannels.collectAsState().value

    LaunchedEffect(Unit) {
        startAnimation = true
        delay(2500)
        if (hasChannels) onNavigateToHome() else onNavigateToSetup()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.radialGradient(
                    colors = listOf(TVSurfaceVariant, TVBackground),
                    radius = 1.2f
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .alpha(alphaAnim)
                .padding(horizontal = 64.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .width(160.dp)
                    .height(160.dp)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(TVAccent.copy(alpha = 0.3f), Color.Transparent),
                            radius = 1.0f
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "K",
                    fontSize = 80.sp,
                    fontWeight = FontWeight.Bold,
                    color = TVAccent
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Kobir IPTV",
                style = androidx.tv.material3.MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Full Free Apps, No Premium Subscription",
                style = androidx.tv.material3.MaterialTheme.typography.headlineSmall,
                color = TVAccent,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Your Ultimate Free IPTV Experience",
                style = androidx.tv.material3.MaterialTheme.typography.bodyLarge,
                color = TVSecondary,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun SetupScreen(
    onComplete: () -> Unit,
    viewModel: SetupViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState.collectAsState().value

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(TVBackground)
            .padding(48.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth(0.6f)
        ) {
            Text(
                text = "Kobir IPTV",
                style = androidx.tv.material3.MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Full Free Apps, No Premium Subscription",
                style = androidx.tv.material3.MaterialTheme.typography.titleMedium,
                color = TVAccent
            )

            Spacer(modifier = Modifier.height(48.dp))

            Text(
                text = "Enter your M3U playlist URL to get started",
                style = androidx.tv.material3.MaterialTheme.typography.bodyLarge,
                color = TVSecondary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "M3U URL",
                style = androidx.tv.material3.MaterialTheme.typography.labelLarge,
                color = TVOnSurfaceVariant,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            var text by remember { mutableStateOf("") }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .background(TVSurfaceVariant, shape = RoundedCornerShape(12.dp))
                    .padding(horizontal = 20.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                BasicTextField(
                    value = text,
                    onValueChange = { text = it },
                    textStyle = TextStyle(color = Color.White, fontSize = 18.sp),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    if (text.isNotBlank() && !uiState.isLoading) {
                        viewModel.loadPlaylist(text) { onComplete() }
                    }
                },
                enabled = text.isNotBlank() && !uiState.isLoading,
                colors = ButtonDefaults.colors(
                    containerColor = TVAccent,
                    focusedContainerColor = TVAccent.copy(alpha = 0.8f)
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = if (uiState.isLoading) "Loading..." else "Load Playlist",
                    style = androidx.tv.material3.MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }

            if (uiState.error != null) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = uiState.error ?: "",
                    color = Color(0xFFFF4444),
                    style = androidx.tv.material3.MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
