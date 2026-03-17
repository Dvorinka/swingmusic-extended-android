package com.android.swingmusic.analytics.domain.model

data class ListeningStats(
    val totalPlaytime: Long,
    val tracksPlayed: Int,
    val uniqueTracksPlayed: Int,
    val albumsPlayed: Int,
    val artistsPlayed: Int,
    val sessionsCount: Int,
    val averageSessionLength: Long,
    val mostPlayedTrack: TrackPlayStats?,
    val mostPlayedArtist: ArtistPlayStats?,
    val mostPlayedAlbum: AlbumPlayStats?,
    val playtimeByDay: Map<String, Long>, // Day -> milliseconds
    val playtimeByHour: Map<Int, Long>, // Hour -> milliseconds
    val topTracks: List<TrackPlayStats>,
    val topArtists: List<ArtistPlayStats>,
    val topAlbums: List<AlbumPlayStats>,
    val topGenres: List<GenrePlayStats>,
    val recentlyPlayed: List<TrackPlayStats>
)

data class TrackPlayStats(
    val trackHash: String,
    val title: String,
    val artist: String,
    val album: String,
    val playCount: Int,
    val totalPlaytime: Long,
    val lastPlayed: Long,
    val averageSessionLength: Long
)

data class ArtistPlayStats(
    val artistHash: String,
    val name: String,
    val playCount: Int,
    val totalPlaytime: Long,
    val trackCount: Int,
    val albumCount: Int,
    val lastPlayed: Long
)

data class AlbumPlayStats(
    val albumHash: String,
    val title: String,
    val artist: String,
    val playCount: Int,
    val totalPlaytime: Long,
    val trackCount: Int,
    val lastPlayed: Long
)

data class GenrePlayStats(
    val genre: String,
    val playCount: Int,
    val totalPlaytime: Long,
    val trackCount: Int
)

data class ListeningSession(
    val sessionId: String,
    val startTime: Long,
    val endTime: Long?,
    val duration: Long,
    val tracksPlayed: Int,
    val tracksSkipped: Int,
    val averageListenTime: Long,
    val deviceType: String,
    val appVersion: String
)

data class UserPreferences(
    val favoriteGenres: List<String>,
    val preferredAudioQuality: String,
    val typicalListeningTime: String, // morning, afternoon, evening, night
    val sessionFrequency: String, // daily, weekly, occasional
    val discoveryMode: String // curated, algorithm, manual
)
