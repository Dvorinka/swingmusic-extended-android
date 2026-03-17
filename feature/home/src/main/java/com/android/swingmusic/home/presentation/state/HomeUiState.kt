package com.android.swingmusic.home.presentation.state

import com.android.swingmusic.home.domain.usecase.HomeData

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
    data class NavigateToFavorites : HomeUiEvent()
    data class NavigateToSettings : HomeUiEvent()
    data class ShuffleAll : HomeUiEvent()
    data class PlayDailyMix : HomeUiEvent()
}
