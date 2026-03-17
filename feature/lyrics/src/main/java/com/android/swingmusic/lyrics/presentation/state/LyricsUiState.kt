package com.android.swingmusic.lyrics.presentation.state

import com.android.swingmusic.core.domain.model.LyricsLine
import com.android.swingmusic.core.domain.model.LyricsResponse
import com.android.swingmusic.core.domain.model.Track

data class LyricsUiState(
    val lyrics: List<LyricsLine> = emptyList(),
    val currentTrack: Track? = null,
    val currentLine: Int = -1,
    val isSynced: Boolean = true,
    val exists: Boolean = false,
    val copyright: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val userScrolled: Boolean = false,
    val playbackPosition: Long = 0L
)

sealed class LyricsUiEvent {
    data class LoadLyrics(val filepath: String, val trackHash: String, val force: Boolean = false) : LyricsUiEvent()
    data class SetCurrentLine(val line: Int, val scroll: Boolean = true) : LyricsUiEvent()
    data class UpdatePlaybackPosition(val position: Long) : LyricsUiEvent()
    data class SetUserScrolled(val scrolled: Boolean) : LyricsUiEvent()
    data class ScrollToLine(val line: Int) : LyricsUiEvent()
    data class SeekToPosition(val position: Long) : LyricsUiEvent()
    data class ClearLyrics : LyricsUiEvent()
}
