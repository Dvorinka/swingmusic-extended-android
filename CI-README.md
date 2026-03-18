# Swing Music Android - CI/CD Setup

## 🚀 Automated Builds

This repository includes automated CI/CD pipelines for the Android module using GitHub Actions.

### 📋 Workflows

#### 1. Android CI (`android-ci.yml`)
- **Triggers**: Push to main branch, Pull Requests, Manual dispatch
- **Jobs**: 
  - `test`: Runs unit tests and lint checks
  - `build`: Builds debug and release APKs
  - `release`: Creates GitHub releases (main branch only)

#### 2. Build and Deploy (`build-and-deploy.yml`)
- **Triggers**: Push to main branch, Pull Requests, Manual dispatch
- **Features**: QR code generation, artifact uploads

### 🔧 Build Process

1. **Environment Setup**
   - Java 17 (Temurin distribution)
   - Android SDK with API level 34
   - Gradle caching for faster builds

2. **Testing**
   - Unit tests with JUnit
   - Android lint checks
   - Code quality verification

3. **Build**
   - Debug APK for development
   - Release APK signed with debug keystore
   - Artifact upload to GitHub Actions

4. **Release** (main branch only)
   - Automatic GitHub release creation
   - APK attachment to release
   - Version tagging with run number

### 📱 Installing APKs

#### From GitHub Actions
1. Go to the **Actions** tab
2. Select the latest workflow run
3. Download artifacts from the **Artifacts** section
4. Install the APK on your Android device

#### From GitHub Releases
1. Go to the **Releases** tab
2. Download the latest APK from the release assets
3. Install on your Android device

### 🔑 Signing Configuration

The CI/CD uses Android's debug keystore for signing release builds automatically. For production releases, configure signing secrets:

```yaml
# GitHub Repository Secrets
KEYSTORE_FILE: Base64 encoded keystore file
KEYSTORE_PASSWORD: Keystore password
KEY_ALIAS: Key alias
KEY_PASSWORD: Key password
```

### 🛠️ Local Development

```bash
# Clone the repository
git clone https://github.com/Dvorinka/swingmusic-extended-android.git
cd swingmusic-android

# Build debug APK
./gradlew assembleDebug

# Build release APK (requires signing config)
./gradlew assembleRelease

# Run tests
./gradlew test

# Run lint
./gradlew lint
```

### 📊 Build Status

- ✅ **Debug builds**: Available on every push/PR
- ✅ **Release builds**: Available on every push/PR
- ✅ **GitHub releases**: Created automatically on main branch pushes
- ✅ **Artifacts**: Stored for 30-90 days

### 🐛 Troubleshooting

#### Build Failures
- Check the **Actions** tab for detailed logs
- Verify Android SDK and Gradle versions
- Ensure all dependencies are available

#### APK Installation Issues
- Enable "Unknown Sources" in Android settings
- Clear cache and retry download
- Check Android version compatibility (minSdk 26)

### 🔄 Workflow Updates

To modify the CI/CD pipeline:
1. Edit `.github/workflows/android-ci.yml`
2. Test changes in a feature branch
3. Create a pull request for review

### 📞 Support

For issues related to:
- **CI/CD pipeline**: Create an issue in this repository
- **App functionality**: Check the main SwingMusic repository
- **Build problems**: Include logs and error details in issues
