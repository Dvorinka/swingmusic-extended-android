package com.android.swingmusic.core.domain.model

data class LyricsLine(
    val time: Long,
    val text: String,
    val words: List<LyricsWord> = emptyList()
)

data class LyricsWord(
    val text: String,
    val startTime: Long,
    val endTime: Long,
    val isHighlighted: Boolean = false
)

data class LyricsResponse(
    val lyrics: List<LyricsLine>,
    val copyright: String = "",
    val synced: Boolean = true,
    val exists: Boolean = true,
    val error: String? = null
)
