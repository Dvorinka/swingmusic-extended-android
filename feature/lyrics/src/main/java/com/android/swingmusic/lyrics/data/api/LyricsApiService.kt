package com.android.swingmusic.lyrics.data.api

import com.android.swingmusic.core.data.dto.lyricsResponseDto
import com.android.swingmusic.core.data.dto.lyricsResponseDto.LyricsExistsResponseDto
import com.android.swingmusic.core.data.util.safeApiCall
import com.android.swingmusic.core.domain.model.Track
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Inject

interface LyricsApi {
    @GET("lyrics")
    suspend fun getLyrics(
        @Query("filepath") filepath: String,
        @Query("trackhash") trackHash: String
    ): Response<LyricsResponseDto>
    
    @GET("lyrics/check")
    suspend fun checkLyricsExists(
        @Query("filepath") filepath: String,
        @Query("trackhash") trackHash: String
    ): Response<LyricsExistsResponseDto>
}

class LyricsApiService @Inject constructor(
    private val api: LyricsApi
) {
    suspend fun getLyrics(filepath: String, trackHash: String) = safeApiCall {
        api.getLyrics(filepath, trackHash)
    }
    
    suspend fun checkLyricsExists(filepath: String, trackHash: String) = safeApiCall {
        api.checkLyricsExists(filepath, trackHash)
    }
}
