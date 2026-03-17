package com.android.swingmusic.uicomponent.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Color Palette
object SwingMusicColors {
    // Primary Colors
    val Primary = Color(0xFF6366F1) // Indigo
    val PrimaryVariant = Color(0xFF4F46E5) // Darker Indigo
    val OnPrimary = Color(0xFFFFFFFF) // White
    
    // Secondary Colors
    val Secondary = Color(0xFF8B5CF6) // Light Blue
    val SecondaryVariant = Color(0xFF6366F1) // Indigo
    val OnSecondary = Color(0xFFFFFFFF) // White
    
    // Surface Colors
    val Surface = Color(0xFFFAFAFA) // Very Light Gray
    val SurfaceVariant = Color(0xFFF5F5F5) // Light Gray
    val OnSurface = Color(0xFF1C1C1C) // Dark Gray
    val OnSurfaceVariant = Color(0xFF49454F) // Medium Gray
    
    // Background Colors
    val Background = Color(0xFFFFFFFF) // White
    val OnBackground = Color(0xFF1C1C1C) // Dark Gray
    
    // Accent Colors
    val Accent = Color(0xFF10B981) // Emerald
    val AccentVariant = Color(0xFF059669) // Darker Emerald
    val OnAccent = Color(0xFFFFFFFF) // White
    
    // Status Colors
    val Success = Color(0xFF10B981) // Emerald
    val Warning = Color(0xFFF59E0B) // Amber
    val Error = Color(0xFFEF4444) // Red
    val Info = Color(0xFF3B82F6) // Blue
    
    // Special Colors
    val GradientStart = Color(0xFF6366F1) // Indigo
    val GradientEnd = Color(0xFF8B5CF6) // Light Blue
    val Shadow = Color(0x1A000000) // Semi-transparent Black
    val Border = Color(0xFFE5E7EB) // Light Gray Border
    val Divider = Color(0xFFF3F4F6) // Very Light Gray
}

// Typography
object SwingMusicTypography {
    // Display Styles
    val DisplayLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 57.sp,
        lineHeight = 64.sp
    )
    
    val DisplayMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 45.sp,
        lineHeight = 52.sp
    )
    
    val DisplaySmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 36.sp,
        lineHeight = 44.sp
    )
    
    // Headline Styles
    val HeadlineLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.SemiBold,
        fontSize = 32.sp,
        lineHeight = 40.sp
    )
    
    val HeadlineMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.SemiBold,
        fontSize = 28.sp,
        lineHeight = 36.sp
    )
    
    val HeadlineSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp,
        lineHeight = 32.sp
    )
    
    // Title Styles
    val TitleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 22.sp,
        lineHeight = 28.sp
    )
    
    val TitleMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 24.sp
    )
    
    val TitleSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp
    )
    
    // Body Styles
    val BodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp
    )
    
    val BodyMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp
    )
    
    val BodySmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp
    )
    
    // Label Styles
    val LabelLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp
    )
    
    val LabelMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp
    )
    
    val LabelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp
    )
}

// Spacing
object SwingMusicSpacing {
    val XS = 4.dp
    val SM = 8.dp
    val MD = 16.dp
    val LG = 24.dp
    val XL = 32.dp
    val XXL = 48.dp
    val XXXL = 64.dp
}

// Shapes
object SwingMusicShapes {
    val CornerSmall = RoundedCornerShape(4.dp)
    val CornerMedium = RoundedCornerShape(8.dp)
    val CornerLarge = RoundedCornerShape(12.dp)
    val CornerExtraLarge = RoundedCornerShape(16.dp)
    val CornerFull = RoundedCornerShape(50)
    
    val CardShape = RoundedCornerShape(12.dp)
    val ButtonShape = RoundedCornerShape(8.dp)
    val TextFieldShape = RoundedCornerShape(8.dp)
    val BottomNavShape = RoundedCornerShape(16.dp)
}

// Elevation
object SwingMusicElevation {
    val None = 0.dp
    val XS = 2.dp
    val SM = 4.dp
    val MD = 8.dp
    val LG = 12.dp
    val XL = 16.dp
    val XXL = 24.dp
}
