package com.android.swingmusic.playlist.domain.model

data class Playlist(
    val id: String,
    val title: String,
    val description: String,
    val trackCount: Int,
    val duration: Long,
    val image: String?,
    val isPublic: Boolean = false,
    val isCollaborative: Boolean = false,
    val owner: PlaylistOwner,
    val collaborators: List<PlaylistCollaborator> = emptyList(),
    val tags: List<String> = emptyList(),
    val createdAt: Long,
    val updatedAt: Long,
    val artistLinks: List<ArtistLink> = emptyList()
)

data class PlaylistOwner(
    val id: String,
    val name: String,
    val imageUrl: String?
)

data class PlaylistCollaborator(
    val userId: String,
    val name: String,
    val role: PlaylistRole,
    val addedAt: Long
)

data class ArtistLink(
    val artistHash: String,
    val artistName: String,
    val imageUrl: String?,
    val trackCount: Int,
    val addedAt: Long,
    val addedBy: String
)

enum class PlaylistRole {
    OWNER,
    EDITOR,
    VIEWER
}

data class PlaylistTrack(
    val id: String,
    val playlistId: String,
    val trackHash: String,
    val position: Int,
    val addedAt: Long,
    val addedBy: String
)
