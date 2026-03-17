package com.android.swingmusic.library.presentation.state

enum class LibraryTab(val title: String, val icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Albums("Albums", androidx.compose.material.icons.Icons.Default.Album),
    Playlists("Playlists", androidx.compose.material.icons.Icons.Default.PlaylistPlay),
    Favorites("Favorites", androidx.compose.material.icons.Icons.Default.Favorite),
    Folders("Folders", androidx.compose.material.icons.Icons.Default.Folder),
    Artists("Artists", androidx.compose.material.icons.Icons.Default.LibraryMusic)
}

data class LibraryUiState(
    val albums: List<Any> = emptyList(), // TODO: Replace with Album model
    val playlists: List<Any> = emptyList(), // TODO: Replace with Playlist model
    val favoriteTracks: List<Any> = emptyList(), // TODO: Replace with Track model
    val folders: List<Any> = emptyList(), // TODO: Replace with Folder model
    val artists: List<Any> = emptyList(), // TODO: Replace with Artist model
    val isLoading: Boolean = false,
    val error: String? = null
)

sealed class LibraryUiEvent {
    object LoadLibraryData : LibraryUiEvent()
    data class NavigateToAlbum(val albumHash: String) : LibraryUiEvent()
    data class NavigateToPlaylist(val playlistId: String) : LibraryUiEvent()
    data class NavigateToArtist(val artistHash: String) : LibraryUiEvent()
    data class NavigateToFolder(val folderPath: String) : LibraryUiEvent()
    data class PlayTrack(val track: Any) : LibraryUiEvent()
    data class ShuffleAll : LibraryUiEvent()
    data class CreatePlaylist : LibraryUiEvent()
}
