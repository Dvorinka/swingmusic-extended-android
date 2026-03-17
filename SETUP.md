# Android Development Setup Guide

## Local Development Setup

### 1. Install Android Studio
- Download and install [Android Studio](https://developer.android.com/studio)
- Follow the installation wizard for your operating system

### 2. Set up Android SDK
- Open Android Studio
- Go to **Tools → SDK Manager**
- Install the following:
  - **Android SDK Platform-Tools** (latest)
  - **Android 14 (API level 34)** or higher
  - **Android SDK Build-Tools 34.0.0** or higher
  - **NDK (Side by side)** 25.1.8937393 or higher

### 3. Configure Environment Variables
Add these to your shell profile (`~/.bashrc`, `~/.zshrc`, etc.):

```bash
# Android SDK
export ANDROID_HOME=$HOME/Library/Android/sdk  # macOS
# export ANDROID_HOME=$HOME/Android/Sdk       # Linux
# export ANDROID_HOME=C:\Users\%USERNAME%\AppData\Local\Android\Sdk  # Windows

export PATH=$PATH:$ANDROID_HOME/platform-tools
export PATH=$PATH:$ANDROID_HOME/tools
```

### 4. Update local.properties
If the automatic detection doesn't work, edit `local.properties`:

```properties
sdk.dir=/path/to/your/Android/Sdk
```

### 5. Build the Project
```bash
./gradlew clean
./gradlew assembleDebug    # For debug build
./gradlew assembleRelease  # For release build
```

## GitHub Actions (CI/CD)

The GitHub Actions workflow is now properly configured with:
- ✅ JDK 17 setup
- ✅ Android SDK setup (API 34, Build Tools 34.0.0, NDK 25.1.8937393)
- ✅ Gradle caching for faster builds
- ✅ Automated APK building and deployment

## Troubleshooting

### Common Issues

1. **"SDK location not found"**
   - Ensure `ANDROID_HOME` is set correctly
   - Check `local.properties` points to valid SDK path
   - Verify SDK is installed with required components

2. **"Failed to install NDK"**
   - Install NDK via Android Studio SDK Manager
   - Or download manually from [Android NDK downloads](https://developer.android.com/ndk/downloads)

3. **"Gradle build failed"**
   - Run `./gradlew clean` first
   - Check if all required SDK components are installed
   - Verify Java version (JDK 17 required)

4. **"License not accepted"**
   ```bash
   yes | sdkmanager --licenses
   ```

### Verify Setup
Run this command to verify everything is working:
```bash
./gradlew assembleDebug --info
```

## Project Structure
```
swingmusic-android/
├── app/                    # Main application module
├── auth/                   # Authentication feature
├── core/                   # Core utilities and models
├── database/               # Database layer
├── network/                # Network layer
├── uicomponent/            # Shared UI components
├── feature/
│   ├── home/              # Home screen feature
│   ├── settings/          # Settings feature
│   ├── player/            # Music player feature
│   ├── search/            # Search feature
│   ├── album/             # Album details feature
│   ├── artist/            # Artist details feature
│   └── folder/            # Folder browsing feature
└── .github/workflows/      # CI/CD configuration
```

## Development Workflow
1. Create feature branch from `main`
2. Make changes and test locally
3. Commit and push to GitHub
4. Pull request triggers automated build
5. Merge to `main` creates release automatically
