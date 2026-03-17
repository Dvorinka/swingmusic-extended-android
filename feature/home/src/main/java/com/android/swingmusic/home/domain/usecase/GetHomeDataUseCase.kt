package com.android.swingmusic.home.domain.usecase

import com.android.swingmusic.core.data.util.Resource
import com.android.swingmusic.core.domain.model.Album
import com.android.swingmusic.core.domain.model.Artist
import com.android.swingmusic.core.domain.model.Track
import com.android.swingmusic.home.data.repository.HomeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetHomeDataUseCase @Inject constructor(
    private val repository: HomeRepository
) {
    suspend operator fun invoke(): Flow<Resource<HomeData>> {
        return repository.getHomeData()
    }
}

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
