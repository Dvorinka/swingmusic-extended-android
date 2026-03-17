package com.android.swingmusic.waveform.presentation.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import kotlin.math.abs
import kotlin.math.sin

@Composable
fun WaveformVisualization(
    samples: FloatArray,
    currentPosition: Long,
    duration: Long,
    isPlaying: Boolean
) {
    val animatedProgress by animateFloatAsState(
        targetValue = if (duration > 0) currentPosition.toFloat() / duration else 0f,
        animationSpec = tween(durationMillis = 200),
        label = "waveformProgress"
    )
    
    val density = LocalDensity.current
    
    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .height(60.dp)
    ) {
        val canvasWidth = size.width
        val canvasHeight = size.height
        val centerY = canvasHeight / 2
        
        // Draw center line
        drawLine(
            color = webSecondary.copy(alpha = 0.3f),
            start = Offset(0f, centerY),
            end = Offset(canvasWidth, centerY),
            strokeWidth = 1.dp.toPx()
        )
        
        // Draw waveform
        if (samples.isNotEmpty()) {
            val barWidth = canvasWidth / samples.size
            val maxAmplitude = samples.maxOrNull() ?: 0f
            
            samples.forEachIndexed { index, amplitude ->
                val x = index * barWidth
                val normalizedAmplitude = if (maxAmplitude > 0) amplitude / maxAmplitude else 0f
                val barHeight = normalizedAmplitude * (canvasHeight / 2 - 4.dp.toPx())
                
                // Determine if this bar is before or after current position
                val progress = index.toFloat() / samples.size
                val isBeforeCurrent = progress < 1f && progress > index / samples.size
                val isAfterCurrent = progress < 1f && progress <= index / samples.size
                val isCurrentBar = abs(progress - (index.toFloat() / samples.size)) < 0.02f
                
                val barColor = when {
                    isCurrentBar -> Color(0xFF006eff)
                    isBeforeCurrent -> webSecondary.copy(alpha = 0.6f)
                    isAfterCurrent -> webSecondary.copy(alpha = 0.4f)
                    else -> webSecondary.copy(alpha = 0.3f)
                }
                
                // Draw bar
                drawRect(
                    color = barColor,
                    topLeft = Offset(
                        x = x + barWidth * 0.1f,
                        y = centerY - barHeight / 2
                    ),
                    size = Size(
                        width = barWidth * 0.8f,
                        height = barHeight
                    )
                )
            }
            
            // Draw progress indicator
            if (isPlaying && duration > 0) {
                val progressX = animatedProgress * canvasWidth
                
                drawLine(
                    color = Color(0xFF006eff),
                    start = Offset(progressX, 0f),
                    end = Offset(progressX, canvasHeight),
                    strokeWidth = 2.dp.toPx()
                )
                
                // Draw glow effect
                drawLine(
                    color = Color(0xFF006eff).copy(alpha = 0.3f),
                    start = Offset(progressX, 0f),
                    end = Offset(progressX, canvasHeight),
                    strokeWidth = 6.dp.toPx()
                )
            }
        }
    }
}

@Composable
fun CircularWaveformVisualization(
    samples: FloatArray,
    currentPosition: Long,
    duration: Long,
    isPlaying: Boolean
) {
    val animatedProgress by animateFloatAsState(
        targetValue = if (duration > 0) currentPosition.toFloat() / duration else 0f,
        animationSpec = tween(durationMillis = 200),
        label = "circularWaveformProgress"
    )
    
    val density = LocalDensity.current
    
    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .height(60.dp)
    ) {
        val canvasWidth = size.width
        val canvasHeight = size.height
        val centerX = canvasWidth / 2
        val centerY = canvasHeight / 2
        val radius = minOf(canvasWidth, canvasHeight) / 2 - 8.dp.toPx()
        
        if (samples.isNotEmpty()) {
            val maxAmplitude = samples.maxOrNull() ?: 0f
            
            // Draw circular waveform
            samples.forEachIndexed { index, amplitude ->
                val angle = (index.toFloat() / samples.size) * 2 * Math.PI - Math.PI / 2
                val normalizedAmplitude = if (maxAmplitude > 0) amplitude / maxAmplitude else 0f
                
                val progress = index.toFloat() / samples.size
                val isBeforeCurrent = animatedProgress < 1f && progress > index / samples.size
                val isAfterCurrent = animatedProgress < 1f && progress <= index / samples.size
                val isCurrentSegment = abs(animatedProgress - (index.toFloat() / samples.size)) < 0.05f
                
                val barRadius = radius * normalizedAmplitude * 0.8f
                val startRadius = radius - barRadius
                val endRadius = radius + barRadius
                
                val segmentColor = when {
                    isCurrentSegment -> Color(0xFF006eff)
                    isBeforeCurrent -> webSecondary.copy(alpha = 0.6f)
                    isAfterCurrent -> webSecondary.copy(alpha = 0.4f)
                    else -> webSecondary.copy(alpha = 0.3f)
                }
                
                // Draw arc segment
                drawArc(
                    color = segmentColor,
                    startAngle = angle.toFloat(),
                    sweepAngle = (360f / samples.size).toFloat(),
                    useCenter = false,
                    topLeft = Offset(centerX - radius, centerY - radius),
                    size = Size(radius * 2, radius * 2),
                    style = Stroke(width = 2.dp.toPx())
                )
            }
            
            // Draw center circle
            drawCircle(
                color = webSecondary.copy(alpha = 0.2f),
                radius = 4.dp.toPx(),
                center = Offset(centerX, centerY)
            )
            
            // Draw progress indicator
            if (isPlaying && duration > 0) {
                val progressAngle = (animatedProgress * 360f) - 90f
                
                drawLine(
                    color = Color(0xFF006eff),
                    start = Offset(centerX, centerY),
                    end = Offset(
                        x = centerX + kotlin.math.cos(progressAngle * Math.PI / 180) * radius,
                        y = centerY + kotlin.math.sin(progressAngle * Math.PI / 180) * radius
                    ),
                    strokeWidth = 3.dp.toPx()
                )
                
                // Draw progress dot
                drawCircle(
                    color = Color(0xFF006eff),
                    radius = 6.dp.toPx(),
                    center = Offset(
                        x = centerX + kotlin.math.cos(progressAngle * Math.PI / 180) * radius,
                        y = centerY + kotlin.math.sin(progressAngle * Math.PI / 180) * radius
                    )
                )
            }
        }
    }
}

@Composable
fun MinimalWaveformVisualization(
    samples: FloatArray,
    currentPosition: Long,
    duration: Long,
    isPlaying: Boolean
) {
    val animatedProgress by animateFloatAsState(
        targetValue = if (duration > 0) currentPosition.toFloat() / duration else 0f,
        animationSpec = tween(durationMillis = 200),
        label = "minimalWaveformProgress"
    )
    
    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .height(40.dp)
    ) {
        val canvasWidth = size.width
        val canvasHeight = size.height
        
        if (samples.isNotEmpty()) {
            val barWidth = 2.dp.toPx()
            val spacing = 1.dp.toPx()
            val totalWidth = samples.size * barWidth + (samples.size - 1) * spacing
            val startX = (canvasWidth - totalWidth) / 2
            
            samples.forEachIndexed { index, amplitude ->
                val x = startX + index * (barWidth + spacing)
                val normalizedAmplitude = amplitude / samples.maxOrNull()!!
                val barHeight = normalizedAmplitude * canvasHeight
                
                val progress = index.toFloat() / samples.size
                val isBeforeCurrent = animatedProgress < 1f && progress > index / samples.size
                val isAfterCurrent = animatedProgress < 1f && progress <= index / samples.size
                val isCurrentBar = abs(animatedProgress - (index.toFloat() / samples.size)) < 0.02f
                
                val barColor = when {
                    isCurrentBar -> Color(0xFF006eff)
                    isBeforeCurrent -> webSecondary.copy(alpha = 0.8f)
                    isAfterCurrent -> webSecondary.copy(alpha = 0.4f)
                    else -> webSecondary.copy(alpha = 0.6f)
                }
                
                drawRect(
                    color = barColor,
                    topLeft = Offset(x, (canvasHeight - barHeight) / 2),
                    size = Size(barWidth, barHeight)
                )
            }
        }
    }
}

// Helper colors from theme
private val webSecondary = Color(0xFF8e8e93)
