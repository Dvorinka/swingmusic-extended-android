package com.android.swingmusic.home.presentation.screen

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.android.swingmusic.home.domain.usecase.GetHomeDataUseCase
import com.android.swingmusic.home.presentation.component.HomeStatsCard
import com.android.swingmusic.home.presentation.component.QuickActionsSection
import com.android.swingmusic.home.presentation.component.RecentlyAddedSection
import com.android.swingmusic.home.presentation.component.SettingsButton
import com.android.swingmusic.home.presentation.state.HomeUiEvent
import com.android.swingmusic.home.presentation.viewmodel.HomeViewModel
import com.android.swingmusic.uicomponent.presentation.theme.SwingMusicTheme
import com.android.swingmusic.uicomponent.presentation.theme.webOnSurface
import com.android.swingmusic.uicomponent.presentation.theme.webSecondary

@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by homeViewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()
    
    SwingMusicTheme {
        val scrollState = rememberScrollState()
        val scope = rememberCoroutineScope()
        
        LaunchedEffect(Unit) {
            homeViewModel.onEvent(HomeUiEvent.LoadHomeData)
        }
        
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF181a1c),
                            Color(0xFF1a1919)
                        )
                    )
                )
                .nestedScroll(remember { androidx.compose.ui.input.nestedscroll.NestedScrollConnection(scrollState) })
        ) {
            when {
                uiState.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(48.dp),
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = "Loading your music...",
                                style = MaterialTheme.typography.bodyLarge,
                                color = webSecondary
                            )
                        }
                    }
                }
                
                uiState.error != null -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Text(
                                text = "⚠️",
                                fontSize = 64.sp
                            )
                            Text(
                                text = uiState.error ?: "Something went wrong",
                                style = MaterialTheme.typography.headlineSmall,
                                color = webOnSurface,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Please check your connection and try again",
                                style = MaterialTheme.typography.bodyMedium,
                                color = webSecondary,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    }
                }
                
                else -> {
                    Box(modifier = Modifier.fillMaxSize()) {
                        // Top bar with settings button
                        TopAppBar(
                            title = {
                                Text(
                                    text = "Swing Music",
                                    style = MaterialTheme.typography.titleLarge,
                                    color = webOnSurface,
                                    fontWeight = FontWeight.Bold
                                )
                            },
                            actions = {
                                SettingsButton(
                                    onClick = { 
                                        scope.launch {
                                            // Navigate to settings
                                            homeViewModel.onEvent(HomeUiEvent.NavigateToSettings)
                                        }
                                    }
                                )
                            },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = Color.Transparent,
                                titleContentColor = webOnSurface,
                                actionContentColor = webOnSurface
                            )
                        )
                        
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            item {
                                QuickActionsSection()
                            }
                            
                            item {
                                HomeStatsCard(
                                    totalTracks = uiState.homeData?.stats?.totalTracks ?: 0,
                                    totalAlbums = uiState.homeData?.stats?.totalAlbums ?: 0,
                                    totalArtists = uiState.homeData?.stats?.totalArtists ?: 0,
                                    totalPlaytime = uiState.homeData?.stats?.totalPlaytime ?: 0L
                                )
                            }
                            
                            item {
                                RecentlyAddedSection(
                                    albums = uiState.homeData?.recentlyAdded ?: emptyList(),
                                    onAlbumClick = { albumHash ->
                                        homeViewModel.onEvent(HomeUiEvent.NavigateToAlbum(albumHash))
                                    }
                                )
                            }
                            
                            // Add more sections as needed
                            item {
                                Spacer(modifier = Modifier.height(80.dp)) // Bottom padding for mini player
                            }
                        }
                    }
                }
            }
        }
    }
}
