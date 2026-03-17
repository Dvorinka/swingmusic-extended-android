package com.android.swingmusic.library.presentation.screen

import androidx.compose.animation.animateColorAsState
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Album
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.PlaylistPlay
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabPosition
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.android.swingmusic.library.presentation.state.LibraryTab
import com.android.swingmusic.library.presentation.viewmodel.LibraryViewModel
import com.android.swingmusic.uicomponent.R
import com.android.swingmusic.uicomponent.presentation.theme.SwingMusicTheme
import com.android.swingmusic.uicomponent.presentation.theme.album_color
import com.android.swingmusic.uicomponent.presentation.theme.artist_color
import com.android.swingmusic.uicomponent.presentation.theme.playlist_color
import com.android.swingmusic.uicomponent.presentation.theme.webOnSurface
import com.android.swingmusic.uicomponent.presentation.theme.webSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibraryScreen(
    libraryViewModel: LibraryViewModel = hiltViewModel()
) {
    val uiState by libraryViewModel.uiState.collectAsState()
    var selectedTab by remember { mutableStateOf(LibraryTab.Albums) }
    
    LaunchedEffect(Unit) {
        libraryViewModel.onEvent(LibraryUiEvent.LoadLibraryData)
    }
    
    SwingMusicTheme {
        Column(
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
        ) {
            // Tab Row
            ScrollableTabRow(
                selectedTabIndex = selectedTab.ordinal,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Transparent),
                containerColor = Color.Transparent,
                contentColor = webOnSurface,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        modifier = Modifier
                            .tabIndicatorOffset(tabPositions[selectedTab.ordinal])
                            .clip(RoundedCornerShape(topEnd = 4.dp, topStart = 4.dp))
                            .background(MaterialTheme.colorScheme.primary),
                        color = MaterialTheme.colorScheme.primary
                    )
                },
                tabs = {
                    LibraryTab.values().forEach { tab ->
                        LibraryTabItem(
                            tab = tab,
                            isSelected = selectedTab == tab,
                            onClick = { selectedTab = tab }
                        )
                    }
                }
            )
            
            // Content
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
            ) {
                when (selectedTab) {
                    LibraryTab.Albums -> AlbumsContent(
                        albums = uiState.albums,
                        onAlbumClick = { albumHash ->
                            libraryViewModel.onEvent(LibraryUiEvent.NavigateToAlbum(albumHash))
                        }
                    )
                    LibraryTab.Playlists -> PlaylistsContent(
                        playlists = uiState.playlists,
                        onPlaylistClick = { playlistId ->
                            libraryViewModel.onEvent(LibraryUiEvent.NavigateToPlaylist(playlistId))
                        }
                    )
                    LibraryTab.Favorites -> FavoritesContent(
                        favoriteTracks = uiState.favoriteTracks,
                        onTrackClick = { track ->
                            libraryViewModel.onEvent(LibraryUiEvent.PlayTrack(track))
                        }
                    )
                    LibraryTab.Folders -> FoldersContent(
                        folders = uiState.folders,
                        onFolderClick = { folderPath ->
                            libraryViewModel.onEvent(LibraryUiEvent.NavigateToFolder(folderPath))
                        }
                    )
                    LibraryTab.Artists -> ArtistsContent(
                        artists = uiState.artists,
                        onArtistClick = { artistHash ->
                            libraryViewModel.onEvent(LibraryUiEvent.NavigateToArtist(artistHash))
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun LibraryTabItem(
    tab: LibraryTab,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val animatedColor by animateColorAsState(
        targetValue = if (isSelected) webOnSurface else webSecondary,
        animationSpec = tween(durationMillis = 300),
        label = "tabColor"
    )
    
    Tab(
        selected = isSelected,
        onClick = onClick,
        selectedContentColor = webOnSurface,
        unselectedContentColor = webSecondary,
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = tab.icon,
                contentDescription = tab.title,
                tint = animatedColor,
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = tab.title,
                color = animatedColor,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
private fun AlbumsContent(
    albums: List<Any>, // TODO: Replace with Album model
    onAlbumClick: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp)
    ) {
        items(albums.take(20)) { album ->
            LibraryItemCard(
                title = "Album Title", // TODO: Use actual album data
                subtitle = "Artist Name",
                icon = Icons.Default.Album,
                color = album_color,
                onClick = { /* onAlbumClick(album.albumHash) */ }
            )
        }
    }
}

@Composable
private fun PlaylistsContent(
    playlists: List<Any>, // TODO: Replace with Playlist model
    onPlaylistClick: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp)
    ) {
        items(playlists.take(20)) { playlist ->
            LibraryItemCard(
                title = "Playlist Title", // TODO: Use actual playlist data
                subtitle = "20 songs",
                icon = Icons.Default.PlaylistPlay,
                color = playlist_color,
                onClick = { /* onPlaylistClick(playlist.id) */ }
            )
        }
    }
}

@Composable
private fun FavoritesContent(
    favoriteTracks: List<Any>, // TODO: Replace with Track model
    onTrackClick: (Any) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp)
    ) {
        items(favoriteTracks.take(20)) { track ->
            LibraryItemCard(
                title = "Track Title", // TODO: Use actual track data
                subtitle = "Artist Name",
                icon = Icons.Default.Favorite,
                color = Color(0xFFf7635c),
                onClick = { /* onTrackClick(track) */ }
            )
        }
    }
}

@Composable
private fun FoldersContent(
    folders: List<Any>, // TODO: Replace with Folder model
    onFolderClick: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp)
    ) {
        items(folders.take(20)) { folder ->
            LibraryItemCard(
                title = "Folder Name", // TODO: Use actual folder data
                subtitle = "15 items",
                icon = Icons.Default.Folder,
                color = artist_color,
                onClick = { /* onFolderClick(folder.path) */ }
            )
        }
    }
}

@Composable
private fun ArtistsContent(
    artists: List<Any>, // TODO: Replace with Artist model
    onArtistClick: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp)
    ) {
        items(artists.take(20)) { artist ->
            LibraryItemCard(
                title = "Artist Name", // TODO: Use actual artist data
                subtitle = "10 albums",
                icon = Icons.Default.LibraryMusic,
                color = artist_color,
                onClick = { /* onArtistClick(artist.artistHash) */ }
            )
        }
    }
}

@Composable
private fun LibraryItemCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    color: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = color.copy(alpha = 0.1f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(32.dp)
            )
            
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge,
                    color = webOnSurface,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = webSecondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
