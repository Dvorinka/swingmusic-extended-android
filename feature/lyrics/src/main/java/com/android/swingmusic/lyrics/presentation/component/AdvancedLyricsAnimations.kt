package com.android.swingmusic.lyrics.presentation.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.swingmusic.core.domain.model.LyricsWord
import com.android.swingmusic.uicomponent.presentation.theme.webOnSurface
import kotlin.math.absoluteValue

@Composable
fun AnimatedLyricsBackground(
    currentLine: Int,
    totalLines: Int,
    playbackProgress: Float
) {
    val animatedProgress by animateFloatAsState(
        targetValue = playbackProgress,
        animationSpec = tween(durationMillis = 1000),
        label = "playbackProgress"
    )
    
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val canvasWidth = size.width
            val canvasHeight = size.height
            
            // Draw animated gradient background
            val gradientBrush = Brush.verticalGradient(
                colors = listOf(
                    Color(0xFF181a1c),
                    Color(0xFF1a1919).copy(alpha = 0.8f),
                    Color(0xFF2c2c2e).copy(alpha = 0.6f)
                ),
                startY = 0f,
                endY = canvasHeight
            )
            drawRect(brush = gradientBrush)
            
            // Draw progress wave
            drawProgressWave(
                canvasWidth = canvasWidth,
                canvasHeight = canvasHeight,
                progress = animatedProgress,
                currentLine = currentLine,
                totalLines = totalLines
            )
            
            // Draw floating particles
            drawFloatingParticles(
                canvasWidth = canvasWidth,
                canvasHeight = canvasHeight,
                progress = animatedProgress
            )
        }
    }
}

private fun DrawScope.drawProgressWave(
    canvasWidth: Float,
    canvasHeight: Float,
    progress: Float,
    currentLine: Int,
    totalLines: Int
) {
    if (totalLines == 0) return
    
    val waveHeight = 40.dp.toPx()
    val waveAmplitude = 20.dp.toPx()
    val waveFrequency = 2f
    
    // Calculate wave position based on current line
    val lineProgress = if (totalLines > 0) currentLine.toFloat() / totalLines else 0f
    val waveY = canvasHeight * 0.7f + (lineProgress - 0.5f) * waveHeight
    
    // Draw wave path
    val wavePath = Path().apply {
        moveTo(0f, waveY)
        
        for (x in 0..canvasWidth.toInt() step 5) {
            val normalizedX = x.toFloat() / canvasWidth
            val waveOffset = kotlin.math.sin(normalizedX * Math.PI * 2 * waveFrequency) * waveAmplitude
            val y = waveY + waveOffset * (1f - progress.absoluteValue)
            lineTo(x.toFloat(), y)
        }
    }
    
    drawPath(
        path = wavePath,
        brush = Brush.horizontalGradient(
            colors = listOf(
                Color(0xFF006eff).copy(alpha = 0.3f),
                Color(0xFF006eff).copy(alpha = 0.1f)
            )
        ),
        style = Stroke(
            width = 2.dp.toPx(),
            cap = androidx.compose.ui.graphics.drawscope.StrokeCap.Round
        )
    )
}

private fun DrawScope.drawFloatingParticles(
    canvasWidth: Float,
    canvasHeight: Float,
    progress: Float
) {
    val particleCount = 15
    val time = progress * 1000
    
    repeat(particleCount) { index ->
        val normalizedIndex = index.toFloat() / particleCount
        val x = (normalizedIndex * canvasWidth + time * 0.1f) % canvasWidth
        val baseY = canvasHeight * (0.2f + normalizedIndex * 0.6f)
        val y = baseY + kotlin.math.sin(time * 0.002f + index) * 20f
        val size = 2.dp.toPx() + kotlin.math.sin(time * 0.001f + index * 2) * 1.dp.toPx()
        
        val alpha = 0.3f + kotlin.math.sin(time * 0.001f + index) * 0.2f
        
        drawCircle(
            center = Offset(x, y),
            radius = size,
            color = Color(0xFF006eff).copy(alpha = alpha)
        )
    }
}

@Composable
fun WordByWordAnimation(
    words: List<LyricsWord>,
    currentWordIndex: Int,
    onWordClick: (LyricsWord) -> Unit
) {
    val textMeasurer = rememberTextMeasurer()
    val density = LocalDensity.current
    
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val canvasWidth = size.width
            val canvasHeight = size.height
            
            var currentX = 0f
            var currentY = 0f
            var maxWidthInRow = 0f
            
            words.forEachIndexed { index, word ->
                val isCurrentWord = index == currentWordIndex
                val isPastWord = index < currentWordIndex
                
                // Measure word
                val textStyle = TextStyle(
                    fontSize = 24.sp,
                    fontWeight = if (isCurrentWord) FontWeight.Bold else FontWeight.Medium,
                    color = if (isCurrentWord) {
                        Color(0xFF006eff)
                    } else if (isPastWord) {
                        webOnSurface.copy(alpha = 0.6f)
                    } else {
                        webOnSurface.copy(alpha = 0.8f)
                    }
                )
                
                val textLayoutResult = textMeasurer.measure(
                    text = word.text,
                    style = textStyle,
                    maxLines = 1
                )
                
                val wordWidth = textLayoutResult.size.width
                val wordHeight = textLayoutResult.size.height
                
                // Check if we need to wrap to next line
                if (currentX + wordWidth > canvasWidth - 32.dp.toPx()) {
                    currentX = 0f
                    currentY += wordHeight + 8.dp.toPx()
                    maxWidthInRow = 0f
                }
                
                // Draw word with animation
                val scale = if (isCurrentWord) 1.1f else 1f
                val animatedScale by animateFloatAsState(
                    targetValue = scale,
                    animationSpec = tween(durationMillis = 300),
                    label = "wordScale_$index"
                )
                
                val wordX = currentX + (wordWidth * (1 - animatedScale)) / 2
                val wordY = currentY
                
                // Draw word background for current word
                if (isCurrentWord) {
                    drawRoundRect(
                        color = Color(0xFF006eff).copy(alpha = 0.1f),
                        topLeft = Offset(wordX - 8.dp.toPx(), wordY - 4.dp.toPx()),
                        size = Size(wordWidth + 16.dp.toPx(), wordHeight + 8.dp.toPx()),
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(8.dp.toPx())
                    )
                }
                
                // Draw word
                drawText(
                    textMeasurer = textMeasurer,
                    text = word.text,
                    topLeft = Offset(wordX, wordY),
                    style = textStyle.copy(
                        fontSize = (24.sp * animatedScale)
                    )
                )
                
                // Make word clickable
                currentX += wordWidth + 12.dp.toPx()
                maxWidthInRow = maxOf(maxWidthInRow, wordWidth)
            }
        }
    }
}
