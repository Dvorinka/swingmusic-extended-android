package com.android.swingmusic.home.presentation.state

import com.android.swingmusic.home.domain.model.HomeData

data class HomeUiState(
    val homeData: HomeData? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

sealed class HomeUiEvent {
    object LoadHomeData : HomeUiEvent()
    data class NavigateToAlbum(val albumHash: String) : HomeUiEvent()
    data class NavigateToArtist(val artistHash: String) : HomeUiEvent()
    data class NavigateToPlaylist(val playlistId: String) : HomeUiEvent()
    object NavigateToFavorites : HomeUiEvent()
    object NavigateToSettings : HomeUiEvent()
    object ShuffleAll : HomeUiEvent()
    object PlayDailyMix : HomeUiEvent()
}
