package com.android.swingmusic.lyrics.data.di

import com.android.swingmusic.lyrics.data.api.LyricsApiService
import com.android.swingmusic.lyrics.data.repository.LyricsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.ViewModelComponent
import javax.inject.Singleton

@Module
@InstallIn(ViewModelComponent::class)
abstract class LyricsRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindLyricsApiService(
        lyricsApiService: LyricsApiService
    ): LyricsApiService

    @Binds
    @Singleton
    abstract fun bindLyricsRepository(
        lyricsRepository: LyricsRepository
    ): LyricsRepository
}
