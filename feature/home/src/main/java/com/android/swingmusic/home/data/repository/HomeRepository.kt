package com.android.swingmusic.home.data.repository

import com.android.swingmusic.core.data.util.Resource
import com.android.swingmusic.home.domain.model.HomeData
import com.android.swingmusic.home.domain.model.HomeStats
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeRepository @Inject constructor() {
    
    suspend fun getHomeData(): Flow<Resource<HomeData>> = flow {
        emit(Resource.Loading())
        try {
            // TODO: Implement actual data fetching from API/database
            // For now, return mock data
            val mockData = HomeData(
                recentlyAdded = emptyList(),
                recentlyPlayed = emptyList(),
                topAlbums = emptyList(),
                topArtists = emptyList(),
                dailyMixes = emptyList(),
                stats = HomeStats(
                    totalTracks = 0,
                    totalAlbums = 0,
                    totalArtists = 0,
                    totalPlaytime = 0L
                )
            )
            emit(Resource.Success(mockData))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Unknown error occurred"))
        }
    }
}
