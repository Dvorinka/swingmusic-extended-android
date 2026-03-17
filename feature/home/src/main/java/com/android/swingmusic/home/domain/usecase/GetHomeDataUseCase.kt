package com.android.swingmusic.home.domain.usecase

import com.android.swingmusic.core.data.util.Resource
import com.android.swingmusic.core.domain.model.Album
import com.android.swingmusic.core.domain.model.Artist
import com.android.swingmusic.core.domain.model.Track
import com.android.swingmusic.home.data.repository.HomeRepository
import com.android.swingmusic.home.domain.model.HomeData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetHomeDataUseCase @Inject constructor(
    private val repository: HomeRepository
) {
    suspend operator fun invoke(): Flow<Resource<HomeData>> {
        return repository.getHomeData()
    }
}
