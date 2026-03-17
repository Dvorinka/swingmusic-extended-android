# 📱 SwingMusic Extended Android

A feature-rich Android client for SwingMusic with modern UI and comprehensive functionality.

## ✨ Features

### Core Music Features
- **Full Library Access** - Browse tracks, albums, and artists
- **Advanced Search** - Search across your entire music library
- **Playlist Management** - Create and manage playlists
- **Folder View** - Navigate your music by directory structure
- **High-Quality Streaming** - Adaptive bitrate streaming

### Enhanced Features
- **Offline Mode** - Download music for offline listening
- **Analytics Dashboard** - Track your listening habits
- **Lyrics Display** - Real-time lyrics synchronization
- **Waveform Visualization** - Beautiful audio visualizations
- **Home Screen Widgets** - Quick access to your music
- **Recap Experience** - Year-end listening statistics

### User Experience
- **Material Design 3** - Modern, intuitive interface
- **Dark/Light Themes** - System-aware theming
- **Responsive Layout** - Optimized for all screen sizes
- **Gesture Controls** - Swipe and tap gestures
- **Background Playback** - Continue listening outside app

## 🚀 Quick Start

### Installation

1. **Download APK** from the [Releases page](https://github.com/Dvorinka/swingmusic-extended-android/releases)
2. **Enable Unknown Sources** in device settings
3. **Install APK** on your Android device
4. **Launch App** and connect to your SwingMusic server

### Server Connection

#### QR Code Method (Recommended)
1. Open SwingMusic web client
2. Go to `Settings > Pair Device`
3. Scan QR code with Android app

#### Manual Method
1. Open Android app
2. Enter server URL (e.g., `http://192.168.1.100:1970`)
3. Enter your credentials
4. Save and connect

## 🛠️ Development

### Prerequisites
- **Android Studio** Arctic Fox or newer
- **JDK** 17+
- **Kotlin** 1.9+
- **Gradle** 8.0+

### Setup Development Environment

```bash
# Clone repository
git clone https://github.com/Dvorinka/swingmusic-extended-android.git
cd swingmusic-extended-android

# Open in Android Studio
# or build via command line
./gradlew assembleDebug

# Install on connected device
./gradlew installDebug
```

### Build Variants

```bash
# Debug build
./gradlew assembleDebug

# Release build
./gradlew assembleRelease

# Bundle for Play Store
./gradlew bundleRelease
```

## 📱 App Architecture

### Project Structure
```
app/src/main/java/com/android/swingmusic/
├── presentation/
│   ├── activity/          # Activities
│   ├── screen/           # Compose screens
│   ├── component/        # UI components
│   ├── navigator/        # Navigation
│   └── theme/           # Theme and styling
├── domain/
│   ├── model/           # Data models
│   ├── repository/      # Repository interfaces
│   └── usecase/         # Business logic
├── data/
│   ├── remote/          # API services
│   ├── local/           # Local database
│   └── repository/      # Repository implementations
└── di/                # Dependency injection
```

### Technology Stack
- **Jetpack Compose** - Modern UI toolkit
- **Kotlin** - Programming language
- **Coroutines** - Asynchronous programming
- **Hilt** - Dependency injection
- **Room** - Local database
- **Retrofit** - API client
- **Coil** - Image loading
- **Navigation Compose** - Navigation

## 🔧 Configuration

### App Settings
- **Server URL** - SwingMusic server address
- **Audio Quality** - Streaming quality preferences
- **Download Quality** - Offline download quality
- **Theme** - Light/dark/auto theme
- **Cache Size** - Local storage management

### Network Settings
- **Connection Timeout** - Request timeout settings
- **Retry Policy** - Failed request handling
- **Offline Mode** - Offline playback settings

## 🎨 UI Components

### Main Screens
- **Home Screen** - Dashboard with quick access
- **Library Screen** - Browse music collection
- **Search Screen** - Advanced search interface
- **Player Screen** - Full-featured playback
- **Settings Screen** - App configuration

### Player Features
- **Full Player Controls** - Play, pause, skip, seek
- **Queue Management** - Reorder and manage queue
- **Shuffle/Repeat** - Playback modes
- **Volume Control** - System volume integration
- **Progress Bar** - Seekable progress indicator

## 📊 Features Deep Dive

### Analytics
- **Listening Statistics** - Tracks played, time listened
- **Top Artists/Albums** - Most played content
- **Genre Distribution** - Musical preferences
- **Listening Trends** - Activity over time

### Offline Mode
- **Smart Downloads** - Wi-Fi only downloads
- **Storage Management** - Cache and download limits
- **Sync Status** - Real-time sync indicators
- **Quality Settings** - Configurable download quality

## 🐛 Troubleshooting

### Common Issues

#### Connection Problems
```bash
# Check server connectivity
ping your-server-ip

# Verify server is running
curl http://your-server-ip:1970/api/health

# Check firewall settings
# Ensure port 1970 is open
```

#### Playback Issues
- Check internet connection
- Verify server URL is correct
- Clear app cache
- Restart the app

#### Build Issues
```bash
# Clean build
./gradlew clean

# Clear Gradle cache
rm -rf ~/.gradle/caches/

# Rebuild
./gradlew assembleDebug
```

## 🧪 Testing

### Unit Tests
```bash
# Run unit tests
./gradlew testDebugUnitTest

# Run with coverage
./gradlew testDebugUnitTestCoverage
```

### UI Tests
```bash
# Run UI tests
./gradlew connectedDebugAndroidTest

# Run specific test class
./gradlew connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.example.TestClass
```

## 📦 Release

### Preparing for Release

1. **Update Version** in `build.gradle.kts`
2. **Update Changelog** in `CHANGELOG.md`
3. **Sign APK** with release key
4. **Generate Bundle** for Play Store

### Build Commands
```bash
# Production APK
./gradlew assembleRelease

# Play Store Bundle
./gradlew bundleRelease
```

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Test thoroughly
5. Submit a pull request

### Code Style
- Follow Kotlin coding conventions
- Use Compose best practices
- Write unit tests for new features
- Update documentation

## 📄 License

This project is licensed under AGPL-3.0 License - see the [LICENSE](LICENSE) file for details.

## 🔗 Links

- **Backend**: [swingmusic-extended](https://github.com/Dvorinka/swingmusic-extended)
- **Web Client**: [swingmusic-extended-webclient](https://github.com/Dvorinka/swingmusic-extended-webclient)
- **Desktop App**: [swingmusic-extended-desktop](https://github.com/Dvorinka/swingmusic-extended-desktop)

---

**Built with ❤️ using Kotlin and Jetpack Compose**
