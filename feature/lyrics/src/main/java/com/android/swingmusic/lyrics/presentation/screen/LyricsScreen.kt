package com.android.swingmusic.lyrics.presentation.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.android.swingmusic.core.domain.model.Track
import com.android.swingmusic.lyrics.presentation.component.LyricsDisplay
import com.android.swingmusic.lyrics.presentation.state.LyricsUiEvent
import com.android.swingmusic.lyrics.presentation.viewmodel.LyricsViewModel
import com.android.swingmusic.player.presentation.viewmodel.MediaControllerViewModel
import com.android.swingmusic.uicomponent.presentation.theme.SwingMusicTheme
import com.android.swingmusic.uicomponent.presentation.theme.webOnSurface
import com.android.swingmusic.uicomponent.presentation.theme.webSecondary
import com.ramcosta.composedestinations.annotation.Destination
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
fun LyricsScreen(
    lyricsViewModel: LyricsViewModel = hiltViewModel(),
    mediaControllerViewModel: MediaControllerViewModel = hiltViewModel()
) {
    val uiState by lyricsViewModel.uiState.collectAsState()
    val playerUiState by mediaControllerViewModel.playerUiState.collectAsState()
    val baseUrl by mediaControllerViewModel.baseUrl.collectAsState()
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    
    // Load lyrics when track changes
    LaunchedEffect(playerUiState.nowPlayingTrack) {
        playerUiState.nowPlayingTrack?.let { track ->
            lyricsViewModel.onEvent(
                LyricsUiEvent.LoadLyrics(
                    filepath = track.filepath,
                    trackHash = track.trackHash
                )
            )
        } ?: run {
            lyricsViewModel.onEvent(LyricsUiEvent.ClearLyrics)
        }
    }
    
    // Update lyrics position based on playback
    LaunchedEffect(playerUiState.duration) {
        playerUiState.nowPlayingTrack?.let { track ->
            if (playerUiState.duration.current > 0) {
                val currentPosition = (playerUiState.duration.current * playerUiState.duration.percent) / 100
                lyricsViewModel.onEvent(
                    LyricsUiEvent.UpdatePlaybackPosition(currentPosition.toLong())
                )
            }
        }
    }
    
    SwingMusicTheme {
        Scaffold(
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "Lyrics",
                            style = MaterialTheme.typography.titleMedium,
                            color = webOnSurface,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = { /* Handle back navigation */ }
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back",
                                tint = webOnSurface
                            )
                        }
                    },
                    actions = {
                        if (uiState.lyrics.isNotEmpty()) {
                            IconButton(
                                onClick = { /* Show more options */ }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.MoreVert,
                                    contentDescription = "More",
                                    tint = webOnSurface
                                )
                            }
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent,
                        scrolledContainerColor = Color.Transparent,
                        navigationIconContentColor = webOnSurface,
                        titleContentColor = webOnSurface,
                        actionIconContentColor = webOnSurface
                    )
                )
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .nestedScroll(remember { androidx.compose.ui.input.nestedscroll.NestedScrollConnection(it) })
            ) {
                // Animated background gradient
                val backgroundColors = listOf(
                    Color(0xFF181a1c),
                    Color(0xFF1a1919),
                    Color(0xFF2c2c2e)
                )
                
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = backgroundColors,
                                startY = 0f,
                                endY = 1f
                            )
                        )
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Main lyrics content
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxSize()
                                .padding(horizontal = 16.dp)
                        ) {
                            LyricsDisplay(
                                lyrics = uiState.lyrics,
                                currentLine = uiState.currentLine,
                                copyright = uiState.copyright,
                                isLoading = uiState.isLoading,
                                error = uiState.error,
                                onLineClick = { position ->
                                    // Seek to position when lyric line is clicked
                                    scope.launch {
                                        mediaControllerViewModel.seekToPosition(position)
                                    }
                                },
                                onScrollToLine = { line ->
                                    lyricsViewModel.onEvent(LyricsUiEvent.ScrollToLine(line))
                                }
                            )
                        }
                        
                        // Progress indicator sidebar
                        if (uiState.lyrics.isNotEmpty() && uiState.isSynced) {
                            Box(
                                modifier = Modifier
                                    .width(80.dp)
                                    .fillMaxSize()
                                    .padding(vertical = 16.dp)
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.SpaceBetween
                                ) {
                                    // Current time indicator
                                    Box(
                                        modifier = Modifier
                                            .size(4.dp)
                                            .clip(RoundedCornerShape(2.dp))
                                            .background(MaterialTheme.colorScheme.primary)
                                    )
                                    
                                    Spacer(modifier = Modifier.height(8.dp))
                                    
                                    // Progress dots
                                    Column(
                                        verticalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        repeat(uiState.lyrics.size) { index ->
                                            val isActive = index == uiState.currentLine
                                            val isPast = index < uiState.currentLine
                                            val isUpcoming = index == uiState.currentLine + 1
                                            
                                            Box(
                                                modifier = Modifier
                                                    .size(
                                                        width = if (isActive) 24.dp else 8.dp,
                                                        height = 4.dp
                                                    )
                                                    .clip(RoundedCornerShape(2.dp))
                                                    .background(
                                                        color = when {
                                                            isActive -> MaterialTheme.colorScheme.primary
                                                            isPast -> webSecondary.copy(alpha = 0.5f)
                                                            isUpcoming -> webSecondary.copy(alpha = 0.3f)
                                                            else -> webSecondary.copy(alpha = 0.2f)
                                                        }
                                                    )
                                            )
                                        }
                                    }
                                    
                                    Spacer(modifier = Modifier.height(8.dp))
                                    
                                    // Total time
                                    Text(
                                        text = formatDuration(uiState.lyrics.lastOrNull()?.time ?: 0L),
                                        style = MaterialTheme.typography.bodySmall,
                                        color = webSecondary,
                                        textAlign = TextAlign.Center,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun formatDuration(timeMs: Long): String {
    val seconds = (timeMs / 1000) % 60
    val minutes = (timeMs / 1000 / 60) % 60
    val hours = timeMs / 1000 / 60 / 60
    
    return if (hours > 0) {
        String.format("%d:%02d:%02d", hours, minutes, seconds)
    } else {
        String.format("%d:%02d", minutes, seconds)
    }
}
