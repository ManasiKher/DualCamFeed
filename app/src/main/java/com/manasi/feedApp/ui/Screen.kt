package com.manasi.feedApp.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val label: String, val icon: ImageVector) {
    data object Feed : Screen("feed", "Feed", Icons.Filled.Home)
    data object Camera : Screen("camera", "Camera", Icons.Filled.PhotoCamera)
    data object Gallery : Screen("gallery", "Gallery", Icons.Filled.VideoLibrary)
}
