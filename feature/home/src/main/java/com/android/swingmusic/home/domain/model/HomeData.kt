package com.android.swingmusic.home.domain.model

import com.android.swingmusic.core.domain.model.Album
import com.android.swingmusic.core.domain.model.Artist
import com.android.swingmusic.core.domain.model.Track

data class HomeData(
    val recentlyAdded: List<Album>,
    val recentlyPlayed: List<Track>,
    val topAlbums: List<Album>,
    val topArtists: List<Artist>,
    val dailyMixes: List<Any>, // TODO: Implement mixes
    val stats: HomeStats
)

data class HomeStats(
    val totalTracks: Int,
    val totalAlbums: Int,
    val totalArtists: Int,
    val totalPlaytime: Long
)
