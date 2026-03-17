package com.android.swingmusic.uicomponent.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.swingmusic.uicomponent.theme.*

@Composable
fun SwingMusicCard(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String? = null,
    icon: ImageVector? = null,
    onClick: (() -> Unit)? = null,
    backgroundColor: Color = SwingMusicColors.Surface,
    contentColor: Color = SwingMusicColors.OnSurface,
    elevation: Int = 2
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(SwingMusicShapes.CornerLarge)),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor,
            contentColor = contentColor
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = elevation.dp
        ),
        onClick = onClick ?: {}
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(SwingMusicSpacing.MD),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(SwingMusicSpacing.MD)
        ) {
            icon?.let {
                Icon(
                    imageVector = it,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = contentColor
                )
            }
            
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    style = SwingMusicTypography.TitleMedium,
                    color = contentColor,
                    fontWeight = FontWeight.Medium
                )
                
                subtitle?.let {
                    Text(
                        text = it,
                        style = SwingMusicTypography.BodySmall,
                        color = SwingMusicColors.OnSurfaceVariant,
                        modifier = Modifier.padding(top = SwingMusicSpacing.XS)
                    )
                }
            }
            
            if (onClick != null) {
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = SwingMusicColors.OnSurfaceVariant,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
fun SwingMusicButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: ButtonVariant = ButtonVariant.Primary,
    icon: ImageVector? = null,
    enabled: Boolean = true,
    loading: Boolean = false
) {
    val colors = when (variant) {
        ButtonVariant.Primary -> ButtonDefaults.buttonColors(
            containerColor = SwingMusicColors.Primary,
            contentColor = SwingMusicColors.OnPrimary,
            disabledContainerColor = SwingMusicColors.SurfaceVariant,
            disabledContentColor = SwingMusicColors.OnSurfaceVariant
        )
        ButtonVariant.Secondary -> ButtonDefaults.buttonColors(
            containerColor = SwingMusicColors.SurfaceVariant,
            contentColor = SwingMusicColors.OnSurfaceVariant,
            disabledContainerColor = SwingMusicColors.SurfaceVariant,
            disabledContentColor = SwingMusicColors.OnSurfaceVariant
        )
        ButtonVariant.Ghost -> ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = SwingMusicColors.Primary,
            disabledContainerColor = Color.Transparent,
            disabledContentColor = SwingMusicColors.OnSurfaceVariant
        )
    }
    
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp),
        colors = colors,
        enabled = enabled && !loading,
        shape = RoundedCornerShape(SwingMusicShapes.CornerMedium)
    ) {
        if (loading) {
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                strokeWidth = 2.dp,
                color = SwingMusicColors.OnPrimary
            )
        } else {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                horizontalGap = SwingMusicSpacing.SM
            ) {
                icon?.let {
                    Icon(
                        imageVector = it,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                }
                
                Text(
                    text = text,
                    style = SwingMusicTypography.LabelMedium,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
fun SwingMusicTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String,
    placeholder: String = "",
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null,
    onTrailingIconClick: (() -> Unit)? = null,
    isError: Boolean = false,
    errorMessage: String? = null,
    enabled: Boolean = true
) {
    Column(modifier = modifier) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text(label) },
            placeholder = { Text(placeholder) },
            leadingIcon = leadingIcon?.let {
                {
                    Icon(
                        imageVector = it,
                        contentDescription = null,
                        tint = SwingMusicColors.OnSurfaceVariant
                    )
                }
            },
            trailingIcon = trailingIcon?.let {
                {
                    IconButton(onClick = { onTrailingIconClick?.invoke() }) {
                        Icon(
                            imageVector = it,
                            contentDescription = null,
                            tint = SwingMusicColors.OnSurfaceVariant
                        )
                    }
                }
            },
            isError = isError,
            enabled = enabled,
            shape = RoundedCornerShape(SwingMusicShapes.CornerMedium),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = SwingMusicColors.Primary,
                unfocusedBorderColor = SwingMusicColors.Border,
                errorBorderColor = SwingMusicColors.Error,
                focusedLabelColor = SwingMusicColors.Primary,
                unfocusedLabelColor = SwingMusicColors.OnSurfaceVariant,
                errorLabelColor = SwingMusicColors.Error
            )
        )
        
        errorMessage?.let {
            if (isError) {
                Text(
                    text = it,
                    style = SwingMusicTypography.BodySmall,
                    color = SwingMusicColors.Error,
                    modifier = Modifier.padding(top = SwingMusicSpacing.XS)
                )
            }
        }
    }
}

@Composable
fun SwingMusicProgressIndicator(
    progress: Float,
    modifier: Modifier = Modifier,
    label: String? = null,
    showPercentage: Boolean = true
) {
    Column(modifier = modifier) {
        if (label != null) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = label,
                    style = SwingMusicTypography.BodyMedium,
                    color = SwingMusicColors.OnSurface
                )
                
                if (showPercentage) {
                    Text(
                        text = "${(progress * 100).toInt()}%",
                        style = SwingMusicTypography.LabelSmall,
                        color = SwingMusicColors.OnSurfaceVariant
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(SwingMusicSpacing.SM))
        }
        
        LinearProgressIndicator(
            progress = progress,
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(SwingMusicShapes.CornerFull)),
            color = SwingMusicColors.Primary,
            trackColor = SwingMusicColors.SurfaceVariant,
        )
    }
}

@Composable
fun SwingMusicChip(
    text: String,
    onClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    selected: Boolean = false,
    icon: ImageVector? = null,
    variant: ChipVariant = ChipVariant.Primary
) {
    val backgroundColor = when (variant) {
        ChipVariant.Primary -> if (selected) SwingMusicColors.Primary else SwingMusicColors.SurfaceVariant
        ChipVariant.Secondary -> if (selected) SwingMusicColors.Secondary else SwingMusicColors.SurfaceVariant
        ChipVariant.Outline -> Color.Transparent
    }
    
    val contentColor = when (variant) {
        ChipVariant.Primary -> if (selected) SwingMusicColors.OnPrimary else SwingMusicColors.OnSurfaceVariant
        ChipVariant.Secondary -> if (selected) SwingMusicColors.OnSecondary else SwingMusicColors.OnSurfaceVariant
        ChipVariant.Outline -> if (selected) SwingMusicColors.Primary else SwingMusicColors.OnSurfaceVariant
    }
    
    val border = when (variant) {
        ChipVariant.Outline -> BorderStroke(1.dp, SwingMusicColors.Border)
        else -> null
    }
    
    FilterChip(
        selected = selected,
        onClick = { onClick?.invoke() },
        label = {
            Text(
                text = text,
                style = SwingMusicTypography.LabelMedium,
                fontWeight = FontWeight.Medium
            )
        },
        leadingIcon = icon?.let {
            {
                Icon(
                    imageVector = it,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
            }
        },
        modifier = modifier,
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = backgroundColor,
            selectedLabelColor = contentColor,
            unselectedContainerColor = backgroundColor,
            unselectedLabelColor = contentColor
        ),
        border = border,
        shape = RoundedCornerShape(SwingMusicShapes.CornerFull)
    )
}

@Composable
fun SwingMusicListItem(
    title: String,
    subtitle: String? = null,
    trailing: String? = null,
    icon: ImageVector? = null,
    onClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    selected: Boolean = false
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(SwingMusicShapes.CornerMedium))
            .background(
                if (selected) SwingMusicColors.PrimaryContainer else Color.Transparent
            )
            .padding(SwingMusicSpacing.MD)
            .let { if (onClick != null) it.clickable { onClick() } else it },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(SwingMusicSpacing.MD)
    ) {
        icon?.let {
            Icon(
                imageVector = it,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = if (selected) SwingMusicColors.Primary else SwingMusicColors.OnSurfaceVariant
            )
        }
        
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                style = SwingMusicTypography.BodyLarge,
                color = if (selected) SwingMusicColors.OnPrimaryContainer else SwingMusicColors.OnSurface,
                fontWeight = FontWeight.Medium
            )
            
            subtitle?.let {
                Text(
                    text = it,
                    style = SwingMusicTypography.BodySmall,
                    color = if (selected) SwingMusicColors.OnPrimaryContainer else SwingMusicColors.OnSurfaceVariant,
                    modifier = Modifier.padding(top = SwingMusicSpacing.XS)
                )
            }
        }
        
        trailing?.let {
            Text(
                text = it,
                style = SwingMusicTypography.LabelMedium,
                color = if (selected) SwingMusicColors.Primary else SwingMusicColors.OnSurfaceVariant
            )
        }
    }
}

@Composable
fun SwingMusicGradientCard(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String? = null,
    icon: ImageVector? = null,
    onClick: (() -> Unit)? = null,
    gradientColors: List<Color> = listOf(SwingMusicColors.GradientStart, SwingMusicColors.GradientEnd)
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(SwingMusicShapes.CornerLarge))
            .background(
                Brush.horizontalGradient(gradientColors)
            ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        ),
        onClick = onClick ?: {}
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(SwingMusicSpacing.MD),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(SwingMusicSpacing.MD)
        ) {
            icon?.let {
                Icon(
                    imageVector = it,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = SwingMusicColors.OnPrimary
                )
            }
            
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    style = SwingMusicTypography.TitleMedium,
                    color = SwingMusicColors.OnPrimary,
                    fontWeight = FontWeight.Medium
                )
                
                subtitle?.let {
                    Text(
                        text = it,
                        style = SwingMusicTypography.BodySmall,
                        color = SwingMusicColors.OnPrimary.copy(alpha = 0.8f),
                        modifier = Modifier.padding(top = SwingMusicSpacing.XS)
                    )
                }
            }
            
            if (onClick != null) {
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = SwingMusicColors.OnPrimary.copy(alpha = 0.8f),
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

enum class ButtonVariant {
    Primary, Secondary, Ghost
}

enum class ChipVariant {
    Primary, Secondary, Outline
}
