package com.android.swingmusic.core.data.dto

import com.android.swingmusic.core.domain.model.LyricsLine
import com.android.swingmusic.core.domain.model.LyricsResponse
import com.android.swingmusic.core.domain.model.LyricsWord
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.squareup.moshi.JsonQualifier

@JsonClass(generateAdapter = true)
data class LyricsResponseDto(
    val lyrics: List<LyricsLineDto>,
    val copyright: String = "",
    val synced: Boolean = true,
    val exists: Boolean = true,
    val error: String? = null
)

@JsonClass(generateAdapter = true)
data class LyricsLineDto(
    val time: Long,
    val text: String
)

@JsonClass(generateAdapter = true)
data class LyricsExistsResponseDto(
    val exists: Boolean
)

fun LyricsResponseDto.toDomain(): LyricsResponse {
    return LyricsResponse(
        lyrics = lyrics.map { it.toDomain() },
        copyright = copyright,
        synced = synced,
        exists = exists,
        error = error
    )
}

fun LyricsLineDto.toDomain(): LyricsLine {
    return LyricsLine(
        time = time,
        text = text,
        words = parseWords(text)
    )
}

private fun parseWords(text: String): List<LyricsWord> {
    val words = mutableListOf<LyricsWord>()
    val cleanedText = text.trim()
    
    if (cleanedText.isEmpty()) return words
    
    // Simple word parsing - split by spaces and punctuation
    val wordMatches = Regex("""[^\s\W]+""").findAll(cleanedText)
    var currentTime = 0L
    
    wordMatches.forEach { match ->
        val word = match.value
        val wordLength = word.length.toLong()
        words.add(
            LyricsWord(
                text = word,
                startTime = currentTime,
                endTime = currentTime + wordLength * 50, // Rough timing estimate
                isHighlighted = false
            )
        )
        currentTime += wordLength * 50 + 100 // Add pause between words
    }
    
    return words
}
