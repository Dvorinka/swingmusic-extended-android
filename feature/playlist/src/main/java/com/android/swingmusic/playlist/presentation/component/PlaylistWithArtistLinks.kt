package com.android.swingmusic.playlist.presentation.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.android.swingmusic.playlist.domain.model.ArtistLink
import com.android.swingmusic.playlist.domain.model.Playlist
import com.android.swingmusic.uicomponent.presentation.theme.playlist_color
import com.android.swingmusic.uicomponent.presentation.theme.webOnSurface
import com.android.swingmusic.uicomponent.presentation.theme.webSecondary

@Composable
fun PlaylistWithArtistLinksCard(
    playlist: Playlist,
    onPlaylistClick: () -> Unit,
    onArtistClick: (String) -> Unit,
    onAddArtist: () -> Unit,
    onMoreOptions: () -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }
    val animatedScale by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(durationMillis = 200),
        label = "playlistScale"
    )
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onPlaylistClick() }
            .padding(horizontal = 8.dp, vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = playlist_color.copy(alpha = 0.1f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Playlist Image
            AsyncImage(
                model = playlist.image ?: "img/placeholder/playlist",
                placeholder = null,
                error = null,
                contentDescription = playlist.title,
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
            
            // Playlist Info
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = playlist.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = webOnSurface,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = "${playlist.trackCount} tracks • ${formatDuration(playlist.duration)}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = webSecondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                
                if (playlist.description.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = playlist.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = webSecondary,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                
                // Artist Links Section
                if (playlist.artistLinks.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Link,
                            contentDescription = "Linked artists",
                            tint = playlist_color,
                            modifier = Modifier.size(16.dp)
                        )
                        
                        Text(
                            text = "Linked Artists: ${playlist.artistLinks.size}",
                            style = MaterialTheme.typography.bodySmall,
                            color = playlist_color,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    
                    // Artist Links Pills
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        playlist.artistLinks.take(3).forEach { artistLink ->
                            ArtistLinkPill(
                                artistLink = artistLink,
                                onClick = { onArtistClick(artistLink.artistHash) }
                            )
                        }
                        
                        if (playlist.artistLinks.size > 3) {
                            Text(
                                text = "+${playlist.artistLinks.size - 3}",
                                style = MaterialTheme.typography.bodySmall,
                                color = webSecondary,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
            
            // Action Buttons
            Column(
                horizontalAlignment = Alignment.End
            ) {
                IconButton(
                    onClick = onAddArtist
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            Brush.radialGradient(
                                colors = listOf(
                                    playlist_color.copy(alpha = 0.2f),
                                    playlist_color.copy(alpha = 0.1f)
                                )
                            )
                        )
                        .clip(RoundedCornerShape(20.dp))
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add artist",
                        tint = playlist_color,
                        modifier = Modifier.size(20.dp)
                    )
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Box {
                    IconButton(
                        onClick = { showMenu = !showMenu }
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "More options",
                            tint = webSecondary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    
                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Edit Playlist") },
                            onClick = { 
                                showMenu = false
                                onMoreOptions()
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Share Playlist") },
                            onClick = { 
                                showMenu = false
                                onMoreOptions()
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Duplicate Playlist") },
                            onClick = { 
                                showMenu = false
                                onMoreOptions()
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Delete Playlist") },
                            onClick = { 
                                showMenu = false
                                onMoreOptions()
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ArtistLinkPill(
    artistLink: ArtistLink,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clickable { onClick() }
            .background(
                color = playlist_color.copy(alpha = 0.15f),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            AsyncImage(
                model = artistLink.imageUrl ?: "img/placeholder/artist",
                placeholder = null,
                error = null,
                contentDescription = artistLink.artistName,
                modifier = Modifier
                    .size(20.dp)
                    .clip(RoundedCornerShape(10.dp))
            )
            
            Text(
                text = artistLink.artistName,
                style = MaterialTheme.typography.bodySmall,
                color = playlist_color,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

private fun formatDuration(milliseconds: Long): String {
    val hours = milliseconds / (1000 * 60 * 60)
    val minutes = (milliseconds % (1000 * 60 * 60)) / (1000 * 60)
    
    return if (hours > 0) {
        "${hours}h ${minutes}m"
    } else {
        "${minutes}m"
    }
}
