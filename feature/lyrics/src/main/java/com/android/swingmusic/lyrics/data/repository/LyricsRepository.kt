package com.android.swingmusic.lyrics.data.repository

import com.android.swingmusic.core.data.api.SwingMusicApi
import com.android.swingmusic.core.data.dto.lyricsResponseDto
import com.android.swingmusic.core.data.util.Resource
import com.android.swingmusic.core.data.util.safeApiCall
import com.android.swingmusic.core.domain.model.lyricsResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LyricsRepository @Inject constructor(
    private val api: SwingMusicApi
) {
    suspend fun getLyrics(filepath: String, trackHash: String): Flow<Resource<LyricsResponse>> {
        return flow {
            emit(Resource.Loading())
            val result = safeApiCall {
                api.getLyrics(filepath, trackHash)
            }
            
            when (result) {
                is Resource.Success -> {
                    emit(Resource.Success(result.data.toDomain()))
                }
                is Resource.Error -> {
                    emit(Resource.Error(result.message))
                }
            }
        }
    }
    
    suspend fun checkLyricsExists(filepath: String, trackHash: String): Flow<Resource<Boolean>> {
        return flow {
            emit(Resource.Loading())
            val result = safeApiCall {
                api.checkLyricsExists(filepath, trackHash)
            }
            
            when (result) {
                is Resource.Success -> {
                    emit(Resource.Success(result.data.exists))
                }
                is Resource.Error -> {
                    emit(Resource.Error(result.message))
                }
            }
        }
    }
}
