package com.android.swingmusic.settings.domain.model

data class UserSettings(
    // Appearance
    val theme: String = "dark", // dark, light, auto
    val accentColor: String = "blue", // blue, green, purple, etc.
    val useCircularArtistImages: Boolean = true,
    val showTrackNumbers: Boolean = true,
    val compactLayout: Boolean = false,
    
    // Audio
    val volume: Float = 1.0f,
    val isMuted: Boolean = false,
    val crossfadeDuration: Int = 1000, // milliseconds
    val useCrossfade: Boolean = false,
    val useSilenceSkip: Boolean = true,
    val streamingQuality: String = "original", // original, compressed
    
    // Library
    val defaultView: String = "albums", // albums, artists, folders, playlists
    val showAlbumsAsSingles: Boolean = false,
    val mergeAlbums: Boolean = false,
    val artistSeparators: List<String> = emptyList(),
    val cleanTrackTitles: Boolean = true,
    val hideRemasteredVersions: Boolean = true,
    
    // Player
    val repeatMode: String = "none", // none, one, all
    val autoPlay: Boolean = true,
    val showNowPlayingInTab: Boolean = true,
    val showLyricsByDefault: Boolean = true,
    
    // Interface
    val extendWidth: Boolean = false,
    val useSidebar: Boolean = false,
    val showInlineFavoriteIcon: Boolean = false,
    val highlightFavoriteTracks: Boolean = false,
    
    // Plugins
    val useLyricsPlugin: Boolean = false,
    val autoDownloadLyrics: Boolean = false,
    val overrideUnsyncedLyrics: Boolean = false,
    
    // Stats & Tracking
    val enableTracking: Boolean = true,
    val statsPeriod: String = "week", // week, month, year, all
    val statsGroup: String = "artists", // artists, albums, tracks, genres
    val lastfmApiKey: String = "",
    val lastfmApiSecret: String = "",
    val lastfmSessionKey: String = "",
    
    // Advanced
    val enablePeriodicScans: Boolean = false,
    val periodicScanInterval: Int = 3600, // seconds
    val enableWatchdog: Boolean = false,
    val rootDirectories: List<String> = emptyList(),
    
    // Notifications
    val enableNotifications: Boolean = true,
    val showPlayingNotification: Boolean = true,
    val showControlsInNotification: Boolean = true
)

data class SettingsCategory(
    val title: String,
    val description: String,
    val icon: Int,
    val settings: List<SettingItem>
)

data class SettingItem(
    val key: String,
    val title: String,
    val description: String,
    val type: SettingType,
    val value: Any,
    val options: List<String> = emptyList()
)

enum class SettingType {
    BOOLEAN,
    STRING,
    NUMBER,
    SELECTION,
    MULTI_SELECTION,
    SLIDER
}
