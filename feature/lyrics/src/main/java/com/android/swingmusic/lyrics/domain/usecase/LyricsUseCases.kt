package com.android.swingmusic.lyrics.domain.usecase

import com.android.swingmusic.core.data.util.Resource
import com.android.swingmusic.core.domain.model.LyricsLine
import com.android.swingmusic.core.domain.model.LyricsResponse
import com.android.swingmusic.lyrics.data.repository.LyricsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetLyricsUseCase @Inject constructor(
    private val repository: LyricsRepository
) {
    suspend operator fun invoke(filepath: String, trackHash: String): Flow<Resource<LyricsResponse>> {
        return repository.getLyrics(filepath, trackHash)
    }
}

class CheckLyricsExistsUseCase @Inject constructor(
    private val repository: LyricsRepository
) {
    suspend operator fun invoke(filepath: String, trackHash: String): Flow<Resource<Boolean>> {
        return repository.checkLyricsExists(filepath, trackHash)
    }
}

class CalculateCurrentLineUseCase {
    operator fun invoke(
        lyrics: List<LyricsLine>,
        currentTimeMillis: Long,
        isSynced: Boolean
    ): Int {
        if (!isSynced || lyrics.isEmpty()) return -1
        
        return lyrics.indexOfFirst { line ->
            line.time >= currentTimeMillis
        } - 1
    }
}

class ParseWordsUseCase {
    operator fun invoke(text: String): List<LyricsWord> {
        val words = mutableListOf<LyricsWord>()
        val cleanedText = text.trim()
        
        if (cleanedText.isEmpty()) return words
        
        // Enhanced word parsing with timing
        val wordMatches = Regex("""[^\s\W]+""").findAll(cleanedText)
        var currentTime = 0L
        
        wordMatches.forEach { match ->
            val word = match.value
            val wordLength = word.length.toLong()
            words.add(
                LyricsWord(
                    text = word,
                    startTime = currentTime,
                    endTime = currentTime + wordLength * 80, // Better timing estimate
                    isHighlighted = false
                )
            )
            currentTime += wordLength * 80 + 120 // Refined pause between words
        }
        
        return words
    }
}
