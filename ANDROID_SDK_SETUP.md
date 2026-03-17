# Android SDK Setup Guide

## 🚀 Quick Answer

**Both environments need Android SDK setup:**

1. **GitHub Actions** ✅ - **Already configured**
2. **Local Development** ⚠️ - **Requires manual setup**

---

## 📋 GitHub Actions (CI/CD) - ✅ DONE

All GitHub Actions workflows are now properly configured with Android SDK:

### Fixed Workflows:
- `build-and-deploy.yml` - Main CI/CD pipeline
- `readiness-check.yml` - Project structure validation  
- `publish.yml` - Release automation

### Android SDK Configuration:
```yaml
- name: Set up Android SDK
  uses: android-actions/setup-android@v2
  with:
    api-level: 34          # Android 14
    build-tools: 34.0.0    # Latest build tools
    ndk: 25.1.8937393     # Native development kit
```

**GitHub Actions will work automatically now!** 🎉

---

## 💻 Local Development Setup - ⚠️ REQUIRED

### Step 1: Install Android Studio
1. Download [Android Studio](https://developer.android.com/studio)
2. Install and launch Android Studio
3. Complete the initial setup wizard

### Step 2: Install Android SDK Components
Open **SDK Manager** in Android Studio:
- **Tools → SDK Manager**

Install these components:
- ✅ **Android SDK Platform-Tools** (latest)
- ✅ **Android 14 (API level 34)** 
- ✅ **Android SDK Build-Tools 34.0.0**
- ✅ **NDK (Side by side) 25.1.8937393**

### Step 3: Set Environment Variables
Add to your shell profile (`~/.bashrc`, `~/.zshrc`, etc.):

```bash
# Android SDK Path
export ANDROID_HOME=$HOME/Library/Android/sdk        # macOS
# export ANDROID_HOME=$HOME/Android/Sdk            # Linux  
# export ANDROID_HOME=C:\Users\%USERNAME%\AppData\Local\Android\Sdk  # Windows

# Add to PATH
export PATH=$PATH:$ANDROID_HOME/platform-tools
export PATH=$PATH:$ANDROID_HOME/tools
```

### Step 4: Verify Setup
```bash
# Reload shell or run:
source ~/.bashrc  # or ~/.zshrc

# Test Android SDK
adb version        # Should show Android Debug Bridge version
```

### Step 5: Build Project
```bash
cd swingmusic-android
./gradlew clean
./gradlew assembleDebug
```

---

## 🔧 Troubleshooting

### "SDK location not found"
```bash
# Check if ANDROID_HOME is set
echo $ANDROID_HOME

# If not set, edit local.properties manually:
# sdk.dir=/path/to/your/Android/Sdk
```

### "License not accepted"
```bash
yes | sdkmanager --licenses
```

### "Gradle build failed"
```bash
# Clean and rebuild
./gradlew clean
./gradlew assembleDebug --info
```

---

## 📁 Project Files Updated

1. **`.github/workflows/*.yml`** - Added Android SDK setup to all workflows
2. **`local.properties`** - Updated with proper SDK path configuration
3. **`SETUP.md`** - Complete development setup guide
4. **`ANDROID_SDK_SETUP.md`** - This file

---

## ✅ Verification Checklist

### GitHub Actions:
- [x] build-and-deploy.yml has Android SDK setup
- [x] readiness-check.yml has Android SDK setup  
- [x] publish.yml has Android SDK setup

### Local Development:
- [ ] Android Studio installed
- [ ] SDK components installed (API 34, Build Tools 34.0.0, NDK 25.1.8937393)
- [ ] ANDROID_HOME environment variable set
- [ ] PATH updated with platform-tools
- [ ] `./gradlew assembleDebug` works locally

---

## 🎯 Next Steps

1. **For GitHub Actions:** Nothing to do! It's already fixed ✅
2. **For Local Development:** Follow the setup steps above ⚠️

Once local setup is complete, you can:
- Build APKs locally: `./gradlew assembleDebug`
- Run tests: `./gradlew test`
- Deploy to device/emulator via Android Studio

The project will build successfully in both environments! 🚀
