package com.android.swingmusic.home.presentation.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.android.swingmusic.core.domain.model.Album
import com.android.swingmusic.core.domain.model.Artist
import com.android.swingmusic.core.domain.model.Track
import com.android.swingmusic.home.domain.model.HomeStats
import com.android.swingmusic.uicomponent.R
import com.android.swingmusic.uicomponent.presentation.theme.album_color
import com.android.swingmusic.uicomponent.presentation.theme.artist_color
import com.android.swingmusic.uicomponent.presentation.theme.playlist_color
import com.android.swingmusic.uicomponent.presentation.theme.webOnSurface
import com.android.swingmusic.uicomponent.presentation.theme.webSecondary

@Composable
fun HomeStatsCard(
    totalTracks: Int,
    totalAlbums: Int,
    totalArtists: Int,
    totalPlaytime: Long
) {
    val animatedProgress by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(durationMillis = 1000),
        label = "statsProgress"
    )
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                text = "Your Library",
                style = MaterialTheme.typography.headlineSmall,
                color = webOnSurface,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatItem(
                    value = totalTracks.toString(),
                    label = "Tracks",
                    color = playlist_color,
                    progress = animatedProgress
                )
                StatItem(
                    value = totalAlbums.toString(),
                    label = "Albums",
                    color = album_color,
                    progress = animatedProgress
                )
                StatItem(
                    value = totalArtists.toString(),
                    label = "Artists",
                    color = artist_color,
                    progress = animatedProgress
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Total playtime:",
                    style = MaterialTheme.typography.bodyMedium,
                    color = webSecondary,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = formatPlaytime(totalPlaytime),
                    style = MaterialTheme.typography.bodyMedium,
                    color = webOnSurface,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
private fun StatItem(
    value: String,
    label: String,
    color: Color,
    progress: Float
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.headlineMedium,
            color = webOnSurface,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(bottom = 4.dp)
        )
        
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = webSecondary,
            fontWeight = FontWeight.Medium
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Progress indicator
        Box(
            modifier = Modifier
                .width(40.dp)
                .height(4.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            color.copy(alpha = 0.3f),
                            color
                        ),
                        startX = progress * -1f,
                        endX = progress
                    )
                )
        )
    }
}

@Composable
fun RecentlyAddedSection(
    albums: List<Album>,
    onAlbumClick: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Recently Added",
                style = MaterialTheme.typography.titleLarge,
                color = webOnSurface,
                fontWeight = FontWeight.Bold
            )
            
            Text(
                text = "See all",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.clickable { /* Navigate to all albums */ }
            )
        }
        
        Spacer(modifier = Modifier.height(12.dp))
        
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(albums.take(6)) { album ->
                AlbumCard(
                    album = album,
                    onClick = { onAlbumClick(album.albumHash) }
                )
            }
        }
    }
}

@Composable
fun QuickActionsSection() {
    val actions = listOf(
        QuickAction("Shuffle All", R.drawable.shuffle, playlist_color) { /* Shuffle all */ },
        QuickAction("Daily Mix", R.drawable.play_list, album_color) { /* Open daily mix */ },
        QuickAction("Favorites", R.drawable.fav_not_filled, artist_color) { /* Open favorites */ },
        QuickAction("Statistics", R.drawable.grid, webSecondary) { /* Open stats */ }
    )
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = "Quick Actions",
            style = MaterialTheme.typography.titleLarge,
            color = webOnSurface,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(12.dp))
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            actions.forEach { action ->
                QuickActionCard(
                    action = action,
                    onClick = action.onClick
                )
            }
        }
    }
}

@Composable
private fun QuickActionCard(
    action: QuickAction,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .size(80.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = action.color.copy(alpha = 0.1f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                androidx.compose.material3.Icon(
                    painter = painterResource(id = action.icon),
                    contentDescription = action.label,
                    tint = action.color,
                    modifier = Modifier.size(24.dp)
                )
                
                Text(
                    text = action.label,
                    style = MaterialTheme.typography.bodySmall,
                    color = webOnSurface,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Medium,
                    maxLines = 2
                )
            }
        }
    }
}

@Composable
fun AlbumCard(
    album: Album,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(120.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data("img/thumbnail/medium/${album.image}")
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(R.drawable.audio_fallback),
                error = painterResource(R.drawable.audio_fallback),
                contentDescription = album.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = album.title,
                style = MaterialTheme.typography.bodySmall,
                color = webOnSurface,
                fontWeight = FontWeight.Medium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(horizontal = 4.dp)
            )
        }
    }
}

private data class QuickAction(
    val label: String,
    val icon: Int,
    val color: Color,
    val onClick: () -> Unit
)

private fun formatPlaytime(milliseconds: Long): String {
    val hours = milliseconds / (1000 * 60 * 60)
    val days = hours / 24
    
    return if (days > 0) {
        "${days}d ${hours % 24}h"
    } else if (hours > 0) {
        "${hours}h"
    } else {
        "Less than 1h"
    }
}
