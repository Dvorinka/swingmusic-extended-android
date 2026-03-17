package com.android.swingmusic.settings.presentation.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.android.swingmusic.settings.domain.model.SettingItem
import com.android.swingmusic.settings.domain.model.SettingType
import com.android.swingmusic.uicomponent.presentation.theme.SwingMusicTheme
import com.android.swingmusic.uicomponent.presentation.theme.webOnSurface
import com.android.swingmusic.uicomponent.presentation.theme.webSecondary

@Composable
fun SettingsButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        Color(0xFF006eff).copy(alpha = 0.8f),
                        Color(0xFF006eff).copy(alpha = 0.4f)
                    )
                )
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.Settings,
            contentDescription = "Settings",
            tint = Color.White,
            modifier = Modifier.size(24.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsItem(
    item: SettingItem,
    onValueChange: (String, Any) -> Unit
) {
    var value by remember { mutableStateOf(item.value) }
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = item.title,
                        style = MaterialTheme.typography.bodyLarge,
                        color = webOnSurface,
                        fontWeight = FontWeight.SemiBold
                    )
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    Text(
                        text = item.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = webSecondary,
                        maxLines = 2
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Render different input types based on setting type
            when (item.type) {
                SettingType.BOOLEAN -> {
                    Switch(
                        checked = value as Boolean,
                        onCheckedChange = { newValue ->
                            value = newValue
                            onValueChange(item.key, newValue)
                        },
                        colors = androidx.compose.material3.SwitchDefaults.colors(
                            checkedThumbColor = Color(0xFF006eff),
                            checkedTrackColor = Color(0xFF006eff).copy(alpha = 0.3f),
                            uncheckedThumbColor = webSecondary,
                            uncheckedTrackColor = webSecondary.copy(alpha = 0.3f)
                        )
                    )
                }
                
                SettingType.SELECTION -> {
                    // This would be a dropdown/dropdown menu
                    OutlinedTextField(
                        value = value.toString(),
                        onValueChange = { newValue ->
                            value = newValue
                            onValueChange(item.key, newValue)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        readOnly = true,
                        trailingIcon = {
                            Icon(Icons.Default.Settings, contentDescription = "Change")
                        }
                    )
                }
                
                SettingType.SLIDER -> {
                    val sliderValue = (value as? Float) ?: 0f
                    val animatedValue by animateFloatAsState(
                        targetValue = sliderValue,
                        animationSpec = tween(durationMillis = 300),
                        label = "slider_${item.key}"
                    )
                    
                    Column {
                        Text(
                            text = "${(animatedValue * 100).toInt()}%",
                            style = MaterialTheme.typography.bodyMedium,
                            color = webOnSurface,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.align(Alignment.End)
                        )
                        
                        Slider(
                            value = animatedValue,
                            onValueChange = { newValue ->
                                value = newValue
                                onValueChange(item.key, newValue)
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = androidx.compose.material3.SliderDefaults.colors(
                                thumbColor = Color(0xFF006eff),
                                activeTrackColor = Color(0xFF006eff).copy(alpha = 0.3f),
                                inactiveTrackColor = webSecondary.copy(alpha = 0.3f)
                            )
                        )
                    }
                }
                
                SettingType.NUMBER -> {
                    OutlinedTextField(
                        value = value.toString(),
                        onValueChange = { newValue ->
                            value = newValue
                            onValueChange(item.key, newValue)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }
                
                else -> {
                    OutlinedTextField(
                        value = value.toString(),
                        onValueChange = { newValue ->
                            value = newValue
                            onValueChange(item.key, newValue)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }
            }
        }
    }
}

@Composable
fun SettingsCategoryHeader(
    title: String,
    description: String,
    icon: ImageVector
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color(0xFF006eff),
                modifier = Modifier.size(28.dp)
            )
            
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineSmall,
                    color = webOnSurface,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = webSecondary,
                    maxLines = 2
                )
            }
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Box(
            modifier = Modifier
                .height(2.dp)
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color(0xFF006eff).copy(alpha = 0.3f)
                        )
                    )
                )
        )
    }
}
