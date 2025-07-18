# 🎥 Dual Camera Compose App

A lightweight Android app built with **Jetpack Compose**, **CameraX**, and **Material3**, featuring:

- A video **Feed** tab (demo UI)
- A **Dual Camera** screen with 15-second recording
- A **Gallery** showing locally saved videos
- Uses local storage (no backend required)
- Fully permission-aware for Android 12–14+

---

## 🚀 Features

### 📱 Feed Tab
- Placeholder for public video feed (e.g., Jelly)
- Autoplay-ready UI (customizable)

### 📷 Camera Tab
- Dual preview: front & back camera (CameraX)
- Records 15-second HD video from back camera
- Audio recording (if mic permission granted)
- Saves video to: `Movies/DualCameraApp/`
- Navigates to gallery automatically after save

### 🎞️ Gallery Tab
- Lists saved videos from local storage
- Video thumbnails (via Coil)
- Inline video playback (ExoPlayer)

---

## 🛠️ Tech Stack

- **Jetpack Compose**
- **Material3**
- **CameraX (Preview & VideoCapture)**
- **ExoPlayer** (Media3)
- **Navigation-Compose**
- **Accompanist-Permissions**
- **Coil** (for video thumbnails)

---

## 🔧 Setup

### ✅ Prerequisites
- Android Studio Hedgehog or later
- Min SDK: `21`
- Compile SDK: `34`
- Kotlin DSL enabled

### 📦 Dependencies (Kotlin DSL - `build.gradle.kts`)

```kotlin
implementation("androidx.camera:camera-camera2:1.3.0")
implementation("androidx.camera:camera-lifecycle:1.3.0")
implementation("androidx.camera:camera-view:1.3.0")
implementation("androidx.camera:camera-video:1.3.0")
implementation("androidx.navigation:navigation-compose:2.7.7")
implementation("androidx.compose.material3:material3:1.2.1")
implementation("androidx.compose.material:material-icons-extended:1.6.7")
implementation("com.google.accompanist:accompanist-permissions:0.35.0-alpha")
implementation("androidx.media3:media3-exoplayer:1.3.1")
implementation("androidx.media3:media3-ui:1.3.1")
implementation("io.coil-kt:coil-compose:2.4.0")


## 🔧 Add per permissions

- <uses-permission android:name="android.permission.CAMERA" />
- <uses-permission android:name="android.permission.RECORD_AUDIO" />
- <uses-permission android:name="android.permission.READ_MEDIA_VIDEO" />
- <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"
    android:maxSdkVersion="28" />

# DualCamFeed Architecture

- com.example.myapplication3/
- ├── MainActivity.kt
- ├── ui/
- │   ├── CameraScreen.kt
- │   ├── GalleryScreen.kt
- │   ├── FeedScreen.kt
- │   ├── MainNavigation.kt
- │   ├── PermissionUtils.kt
- │   ├── Screen.kt
- │   └── theme/
- │       ├── Theme.kt
- │       ├── Color.kt
- │       └── Type.kt

# Example

<img width="1080" height="2160" alt="image" src="https://github.com/user-attachments/assets/ba50eb5a-86bc-45fe-8f6c-eb1b1098f2fa" />


# APK is attached for testing
