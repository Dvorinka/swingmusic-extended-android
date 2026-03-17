package com.android.swingmusic.home.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.swingmusic.core.data.util.Resource
import com.android.swingmusic.home.domain.usecase.GetHomeDataUseCase
import com.android.swingmusic.home.presentation.state.HomeUiEvent
import com.android.swingmusic.home.presentation.state.HomeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getHomeDataUseCase: GetHomeDataUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    fun onEvent(event: HomeUiEvent) {
        when (event) {
            is HomeUiEvent.LoadHomeData -> {
                loadHomeData()
            }
            is HomeUiEvent.NavigateToAlbum -> {
                // Handle navigation - this would be handled by navigation component
                // For now, we can log or trigger navigation events
            }
            is HomeUiEvent.NavigateToArtist -> {
                // Handle navigation
            }
            is HomeUiEvent.NavigateToPlaylist -> {
                // Handle navigation
            }
            is HomeUiEvent.NavigateToFavorites -> {
                // Handle navigation
            }
            is HomeUiEvent.ShuffleAll -> {
                // Handle shuffle all
            }
            is HomeUiEvent.NavigateToSettings -> {
                // Handle navigation to settings
            }
        }
    }

    private fun loadHomeData() {
        viewModelScope.launch {
            _uiState.update { 
                it.copy(
                    isLoading = true,
                    error = null
                )
            }

            getHomeDataUseCase().collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }
                    is Resource.Success -> {
                        _uiState.update { 
                            it.copy(
                                homeData = resource.data,
                                isLoading = false,
                                error = null
                            )
                        }
                    }
                    is Resource.Error -> {
                        _uiState.update { 
                            it.copy(
                                isLoading = false,
                                error = resource.message ?: "Failed to load home data"
                            )
                        }
                    }
                }
            }
        }
    }
}
