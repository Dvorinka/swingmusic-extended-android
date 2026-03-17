package com.android.swingmusic.lyrics.presentation.component

import androidx.compose.animation.core.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.swingmusic.core.domain.model.LyricsLine
import com.android.swingmusic.core.domain.model.LyricsWord
import com.android.swingmusic.uicomponent.presentation.theme.album_color
import com.android.swingmusic.uicomponent.presentation.theme.webOnSurface
import com.android.swingmusic.uicomponent.presentation.theme.webSecondary
import kotlinx.coroutines.launch

@Composable
fun AnimatedLyricsLine(
    line: LyricsLine,
    isCurrentLine: Boolean,
    isSeenLine: Boolean,
    isBeforeCurrent: Boolean,
    onLineClick: (Long) -> Unit
) {
    val scope = rememberCoroutineScope()
    
    // Animate word highlights
    val animatedWords = line.words.map { word ->
        val targetColor = if (isCurrentLine && word.isHighlighted) {
            Color(0xFF006eff) // Highlight blue
        } else if (isCurrentLine) {
            webOnSurface
        } else if (isSeenLine) {
            webSecondary.copy(alpha = 0.77f)
        } else {
            webSecondary.copy(alpha = 0.7f)
        }
        
        val animatedColor by animateColorAsState(
            targetValue = targetColor,
            animationSpec = tween(durationMillis = 300),
            label = "wordColor"
        )
        
        AnimatedWord(
            word = word.copy(
                text = word.text,
                isHighlighted = word.isHighlighted,
                color = animatedColor
            ),
            onClick = { onLineClick(word.startTime) }
        )
    }
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onLineClick(line.time) },
        horizontalArrangement = Arrangement.Start
    ) {
        animatedWords.forEach { animatedWord ->
            AnimatedWord(
                word = animatedWord.word,
                onClick = animatedWord.onClick
            )
        }
    }
}

@Composable
private fun AnimatedWord(
    word: LyricsWord,
    onClick: () -> Unit
) {
    val animatedScale by animateFloatAsState(
        targetValue = if (word.isHighlighted) 1.1f else 1f,
        animationSpec = tween(durationMillis = 200),
        label = "wordScale"
    )
    
    Text(
        text = word.text,
        modifier = Modifier
            .clickable { onClick() },
        color = word.color,
        fontSize = (18.sp * animatedScale),
        fontWeight = if (word.isHighlighted) FontWeight.Bold else FontWeight.Medium,
        style = MaterialTheme.typography.bodyMedium
    )
}

@Composable
fun LyricsDisplay(
    lyrics: List<LyricsLine>,
    currentLine: Int,
    copyright: String,
    isLoading: Boolean,
    error: String?,
    onLineClick: (Long) -> Unit,
    onScrollToLine: (Int) -> Unit
) {
    val listState = rememberLazyListState()
    val density = LocalDensity.current
    
    // Auto-scroll to current line
    LaunchedEffect(currentLine) {
        if (currentLine >= 0 && currentLine < lyrics.size) {
            kotlinx.coroutines.delay(100) // Small delay for smooth scrolling
            try {
                listState.animateScrollToItem(
                    index = currentLine,
                    scrollOffset = with(density) { -100.dp } // Center the line
                )
            } catch (e: Exception) {
                // Handle scroll exception gracefully
            }
        }
    }
    
    Box(modifier = Modifier.fillMaxSize()) {
        when {
            isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(48.dp),
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "Loading lyrics...",
                            style = MaterialTheme.typography.bodyMedium,
                            color = webSecondary
                        )
                    }
                }
            }
            
            error != null -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "⚠️",
                            fontSize = 48.sp
                        )
                        Text(
                            text = error,
                            style = MaterialTheme.typography.bodyLarge,
                            color = webSecondary,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
            
            lyrics.isEmpty() -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "🎵",
                            fontSize = 64.sp
                        )
                        Text(
                            text = "No lyrics available",
                            style = MaterialTheme.typography.bodyLarge,
                            color = webSecondary,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = "Lyrics will appear here when available",
                            style = MaterialTheme.typography.bodyMedium,
                            color = webSecondary.copy(alpha = 0.7f),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
            
            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    state = listState,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {
                    // Add spacing at top
                    item {
                        Spacer(modifier = Modifier.height(32.dp))
                    }
                    
                    itemsIndexed(lyrics) { index, line ->
                        val isCurrentLine = index == currentLine
                        val isSeenLine = index < currentLine
                        val isBeforeCurrent = index < currentLine - 3
                        
                        AnimatedLyricsLine(
                            line = line,
                            isCurrentLine = isCurrentLine,
                            isSeenLine = isSeenLine,
                            isBeforeCurrent = isBeforeCurrent,
                            onLineClick = onLineClick
                        )
                    }
                    
                    // Add copyright at bottom
                    if (copyright.isNotEmpty()) {
                        item {
                            Spacer(modifier = Modifier.height(32.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 24.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(
                                        Brush.verticalGradient(
                                            colors = listOf(
                                                Color.Transparent,
                                                webSecondary.copy(alpha = 0.1f)
                                            )
                                        )
                                    )
                                    .padding(16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = copyright.uppercase(),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = webSecondary.copy(alpha = 0.7f),
                                    textAlign = TextAlign.Center,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                            Spacer(modifier = Modifier.height(32.dp))
                        }
                    }
                }
            }
        }
    }
}
