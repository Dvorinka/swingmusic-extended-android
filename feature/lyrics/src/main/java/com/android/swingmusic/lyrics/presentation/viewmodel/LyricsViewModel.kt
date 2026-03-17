package com.android.swingmusic.lyrics.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.swingmusic.core.data.util.Resource
import com.android.swingmusic.core.domain.model.LyricsLine
import com.android.swingmusic.core.domain.model.LyricsResponse
import com.android.swingmusic.core.domain.model.Track
import com.android.swingmusic.lyrics.domain.usecase.CalculateCurrentLineUseCase
import com.android.swingmusic.lyrics.domain.usecase.CheckLyricsExistsUseCase
import com.android.swingmusic.lyrics.domain.usecase.GetLyricsUseCase
import com.android.swingmusic.lyrics.presentation.state.LyricsUiEvent
import com.android.swingmusic.lyrics.presentation.state.LyricsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LyricsViewModel @Inject constructor(
    private val getLyricsUseCase: GetLyricsUseCase,
    private val checkLyricsExistsUseCase: CheckLyricsExistsUseCase,
    private val calculateCurrentLineUseCase: CalculateCurrentLineUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LyricsUiState())
    val uiState: StateFlow<LyricsUiState> = _uiState.asStateFlow()

    private var lineUpdateJob: Job? = null

    fun onEvent(event: LyricsUiEvent) {
        when (event) {
            is LyricsUiEvent.LoadLyrics -> {
                loadLyrics(event.filepath, event.trackHash, event.force)
            }
            is LyricsUiEvent.SetCurrentLine -> {
                setCurrentLine(event.line, event.scroll)
            }
            is LyricsUiEvent.UpdatePlaybackPosition -> {
                updatePlaybackPosition(event.position)
            }
            is LyricsUiEvent.SetUserScrolled -> {
                _uiState.update { it.copy(userScrolled = event.scrolled) }
            }
            is LyricsUiEvent.ScrollToLine -> {
                scrollToLine(event.line)
            }
            is LyricsUiEvent.SeekToPosition -> {
                _uiState.update { it.copy(playbackPosition = event.position) }
                updateCurrentLineBasedOnPosition()
            }
            is LyricsUiEvent.ClearLyrics -> {
                _uiState.update { 
                    LyricsUiState() // Reset to initial state
                }
            }
        }
    }

    private fun loadLyrics(filepath: String, trackHash: String, force: Boolean = false) {
        val currentState = _uiState.value
        
        // Don't reload if same track and not forced
        if (!force && currentState.currentTrack?.trackHash == trackHash) {
            syncWithCurrentPosition()
            return
        }

        viewModelScope.launch {
            _uiState.update { 
                it.copy(
                    isLoading = true,
                    error = null,
                    currentLine = -1
                )
            }

            getLyricsUseCase(filepath, trackHash).collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }
                    is Resource.Success -> {
                        _uiState.update { 
                            it.copy(
                                lyrics = resource.data.lyrics,
                                isSynced = resource.data.synced,
                                exists = resource.data.exists,
                                copyright = resource.data.copyright,
                                isLoading = false,
                                error = null
                            )
                        }
                        syncWithCurrentPosition()
                    }
                    is Resource.Error -> {
                        _uiState.update { 
                            it.copy(
                                isLoading = false,
                                error = resource.message
                            )
                        }
                    }
                }
            }
        }
    }

    private fun setCurrentLine(line: Int, scroll: Boolean = true) {
        _uiState.update { it.copy(currentLine = line) }
        
        if (scroll) {
            lineUpdateJob?.cancel()
            lineUpdateJob = viewModelScope.launch {
                delay(400) // Delay for smooth scrolling
                // Scroll logic will be handled in UI
            }
        }
    }

    private fun updatePlaybackPosition(position: Long) {
        _uiState.update { it.copy(playbackPosition = position) }
        updateCurrentLineBasedOnPosition()
    }

    private fun updateCurrentLineBasedOnPosition() {
        val currentState = _uiState.value
        if (!currentState.isSynced || currentState.lyrics.isEmpty()) return

        val newLine = calculateCurrentLineUseCase(
            lyrics = currentState.lyrics,
            currentTimeMillis = currentState.playbackPosition,
            isSynced = currentState.isSynced
        )
        
        if (newLine != currentState.currentLine) {
            setCurrentLine(newLine)
        }
    }

    private fun syncWithCurrentPosition() {
        val currentState = _uiState.value
        if (currentState.playbackPosition > 0) {
            updateCurrentLineBasedOnPosition()
        }
    }

    private fun scrollToLine(line: Int) {
        // This will be handled by the UI component
        _uiState.update { it.copy(currentLine = line) }
    }

    fun checkLyricsExists(filepath: String, trackHash: String) {
        viewModelScope.launch {
            checkLyricsExistsUseCase(filepath, trackHash).collect { resource ->
                when (resource) {
                    is Resource.Success -> {
                        _uiState.update { it.copy(exists = resource.data) }
                    }
                    is Resource.Error -> {
                        // Handle error silently
                    }
                    else -> {}
                }
            }
        }
    }
}
